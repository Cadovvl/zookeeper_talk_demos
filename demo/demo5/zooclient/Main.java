import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Semaphore;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.WatchedEvent;

public class Main
    implements Watcher {
  final static String LOCK_NAME = "/javann_demo_three";
  final static String LOCK_PREFIX = "prefix-";
  private Semaphore local_lock = new Semaphore(1);
  
  static void doSmth() throws InterruptedException {
    for (int i = 0; i < 6; ++i) {
        System.err.println("+ Doing some staff...");
        Thread.sleep(500);
    }
    System.err.println("+ Done...");
  }

  @Override 
  public void process(WatchedEvent event) {
    if (event.getType() == Event.EventType.NodeChildrenChanged) { 
      local_lock.release();
    } 
  } 

  static Integer extractNodeNumber(final String name) {
    String number = name.replaceAll("[^\\d.]", "");
    return Integer.parseInt(number);
  }

  static boolean isNumberMinimal(final List<String> names, Integer number) {
    List<Integer> numbers = new ArrayList<Integer>();
    for (final String name : names) {
      numbers.add(extractNodeNumber(name));
    }
    return Collections.min(numbers) == number;
  }
  
  public String lock(ZooKeeper zk) {
    try {
      if (zk.exists(LOCK_NAME, null) == null) {
        System.err.println("+ Attempt to create lock node, if not exists");
        zk.create(LOCK_NAME, new byte[16], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
      }

      // Create new child 
      String nodeName = zk.create(LOCK_NAME + "/" + LOCK_PREFIX, 
                                  new byte[16], Ids.OPEN_ACL_UNSAFE, 
                CreateMode.EPHEMERAL_SEQUENTIAL); 
      Integer nodeNumber = extractNodeNumber(nodeName);
      local_lock.acquire();

      while(true) {
        List<String> children = zk.getChildren(LOCK_NAME, this);
        if (isNumberMinimal(children, nodeNumber)) {
          System.err.println("- Got a lock: " + nodeName);
          return nodeName;
        }
        local_lock.acquire();
      }
      
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public void unlock(ZooKeeper zk, String name) {
    try {
      zk.delete(name, -1);
    } catch (Exception e) {
      System.err.println("x Exception while unlock: " + e);
    }
  }
  
  public static void main(String[] args) {
    if (args.length < 1) { // NB!!
      System.err.println("x Usage: ./run connection_string"); // NB!!
      System.exit(1); // NB!!
    } // NB!!
    Random rand = new Random();
    System.out.println("Initializing keeper");
    Main remoteLock = new Main();
    try {
      ZooKeeper zk;
      zk = new ZooKeeper(args[0], 3000, null); // NB!!
      for (int i = 0; i < 10; ++i) {
        System.err.println("- Wanna do some staff...");
        String lockName = remoteLock.lock(zk);
        doSmth();
        remoteLock.unlock(zk, lockName); 
        System.err.println("- Procrastinating...");
        Thread.sleep(Math.abs(rand.nextLong()) % 500 + 700);
      } 
    } catch (Exception e) {
      System.err.println("Exception while init: " + e);
    }
  }
}

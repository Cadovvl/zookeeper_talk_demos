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
    implements Watcher { // NB!!
  final static String LOCK_NAME = "/javann_demo_three";
  final static String LOCK_PREFIX = "prefix-";
  private Semaphore local_lock = new Semaphore(1); // NB!
  
  static void doSmth() throws InterruptedException {
    for (int i = 0; i < 6; ++i) {
        System.err.println("+ Doing some staff...");
        Thread.sleep(500);
    }
    System.err.println("+ Done...");
  }

  @Override // NB!!
  public void process(WatchedEvent event) { // NB!!
    if (event.getType() == Event.EventType.NodeChildrenChanged) { // NB!!
      local_lock.release(); // NB!!
    } // NB!!
  } // NB!!

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
  
  public String lock(ZooKeeper zk) { // NB!!!
    try {
      if (zk.exists(LOCK_NAME, null) == null) {
        System.err.println("+ Attempt to create lock node, if not exists");
        zk.create(LOCK_NAME, new byte[16], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
      }

      // Create new child  NB!!!
      String nodeName = zk.create(LOCK_NAME + "/" + LOCK_PREFIX, // NB!!!
                                  new byte[16], Ids.OPEN_ACL_UNSAFE, // NB!!!
                CreateMode.EPHEMERAL_SEQUENTIAL); // NB!
      Integer nodeNumber = extractNodeNumber(nodeName); // NB!!!
      local_lock.acquire(); // NB!!!

      while(true) { // NB!!!
        List<String> children = zk.getChildren(LOCK_NAME, this); // NB!
        if (isNumberMinimal(children, nodeNumber)) { // NB!!!
          System.err.println("- Got a lock: " + nodeName); // NB!!!
          return nodeName; // NB!!!
        } // NB!!!
        local_lock.acquire(); // NB!!!
      } // NB!!!
      
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public void unlock(ZooKeeper zk, String name) { // NB!!!
    try {
      zk.delete(name, -1); // NB!!!
    } catch (Exception e) {
      System.err.println("x Exception while unlock: " + e);
    }
  } // NB!!!
  
  public static void main(String[] args) {
    Random rand = new Random();
    System.out.println("Initializing keeper");
    Main remoteLock = new Main(); // NB!!!!
    try {
      ZooKeeper zk;
      zk = new ZooKeeper("localhost:3661", 3000, null);
      for (int i = 0; i < 10; ++i) {
        System.err.println("- Wanna do some staff...");
        String lockName = remoteLock.lock(zk); // NB!!!!
        doSmth(); // NB!!!!
        remoteLock.unlock(zk, lockName); // NB!!!!
        System.err.println("- Procrastinating...");
        Thread.sleep(Math.abs(rand.nextLong()) % 500 + 700);
      } 
    } catch (Exception e) {
      System.err.println("Exception while init: " + e);
    }

  }
}

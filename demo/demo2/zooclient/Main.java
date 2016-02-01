import java.util.Random;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;

public class Main {

  static void doSmth() throws InterruptedException {
    for (int i = 0; i < 6; ++i) {
        System.err.println("+ Doing some staff...");
        Thread.sleep(500);
    }
    System.err.println("+ Done...");
  }
  
  final static String LOCK_NAME = "/javann_demo2"; // NB!!!
  
  static boolean lock(ZooKeeper zk, String name) { 
    boolean success = false;
    try {
      String res = zk.create(name, new byte[16], Ids.OPEN_ACL_UNSAFE,
                             CreateMode.EPHEMERAL // Ephemeral example // NB!!!!
                             );
      System.err.println("+ Succesfully locked " + res);
      success = true;
    } catch (Exception e) {
      System.err.println("x Unable to lock: " + name);
    }
    return success;
  }
  
  static void unlock(ZooKeeper zk, String name) {
    try {
      zk.delete(name, -1);
    } catch (Exception e) {
      System.err.println("x Exception while unlock: " + e);
    }
  }
  
  public static void main(String[] args) {
    Random rand = new Random();
    System.out.println("Initializing keeper");
    try {
      ZooKeeper zk;
      zk = new ZooKeeper("localhost:3661", 3000, null);
      for (int i = 0; i < 10; ++i) {
        System.err.println("- Wanna do some staff...");
        while (!lock(zk, LOCK_NAME)) {
          Thread.sleep(Math.abs(rand.nextLong()) % 500 + 500);
        }
        doSmth();
        unlock(zk, LOCK_NAME);
        System.err.println("- Procrastinating...");
        Thread.sleep(Math.abs(rand.nextLong()) % 500 + 700);
      } 
    } catch (Exception e) {
      System.err.println("Exception while init: " + e);
    }

  }
}

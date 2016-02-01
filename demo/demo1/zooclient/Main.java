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
  
  final static String LOCK_NAME = "/javann_demo1";
  
  static boolean lock(ZooKeeper zk, String name) { // NB!
    //System.err.println("- Attempt to lock " + name);
    boolean success = false;
    try { // NB!
      String res = zk.create(name // Node path // NB!
                              , new byte[16] // Some strange boolshit // NB!
                              , Ids.OPEN_ACL_UNSAFE // Authorization // NB!
                              , CreateMode.PERSISTENT // Future example // NB!
                              ); // NB!
      System.err.println("+ Succesfully locked " + res);
      success = true;
    } catch (Exception e) { // NB!
      System.err.println("x Unable to lock: " + name); // NB!
    } // NB!
    return success;
  }
  
  static void unlock(ZooKeeper zk, String name) { // NB!!
    try { // NB!!
      zk.delete(  name // Node path // NB!!
                  , -1 // Node version. -1 == any version of node // NB!!
                ); // NB!!
    } catch (Exception e) { // NB!!
      System.err.println("x Exception while unlock: " + e); // NB!!
    } // NB!!
  }
  
  public static void main(String[] args) {
    Random rand = new Random();
    System.out.println("Initializing keeper");
    try {
      ZooKeeper zk;
      zk = new ZooKeeper(  "localhost:3661" // address
                           , 3000 // timeout
                           , null // Watcher: not actually need right now
                           );
      
      for (int i = 0; i < 10; ++i) { // NB!!!
        System.err.println("- Wanna do some staff...");
        while (!lock(zk, LOCK_NAME)) { // NB!!!
          Thread.sleep(Math.abs(rand.nextLong()) % 500 + 500); // NB!!!
        } // NB!!!
        doSmth(); // NB!!!
        unlock(zk, LOCK_NAME); // NB!!!
        System.err.println("- Procrastinating...");
        Thread.sleep(Math.abs(rand.nextLong()) % 500 + 700); // NB!!!
      } // NB!!!
    } catch (Exception e) {
      System.err.println("Exception while init: " + e);
    }

  }
}

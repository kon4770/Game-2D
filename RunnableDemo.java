
class RunnableDemo implements Runnable {
   private Thread t;
   private String threadName;
   int sum =0;
   //private Color c = new Color(10,10,10);
   
   public RunnableDemo( String name) {
      threadName = name;
      System.out.println("Creating " +  threadName );
   }
   
   public void run() {
      System.out.println("Running " +  threadName );
      try {
         for(int i = 4; i > 0; i--) {
            System.out.println("Thread: " + threadName + ", " + i);
            sum++;
            // Let the thread sleep for a while.
            Thread.sleep(0);
         }
      } catch (InterruptedException e) {
         System.out.println("Thread " +  threadName + " interrupted.");
      }
      System.out.println("Thread " +  threadName + " exiting.");
   }
   
   public void start () {
      System.out.println("Starting " +  threadName );
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
      System.out.println("Starting 2 " +  threadName );
   }
   
   public int getSum() {
	   return sum;
   }
}

import java.util.concurrent.Semaphore;
import java.util.*;

public class Q3A extends Thread {
  
  private int id;
  private static int N;
  private final Random rand = new Random();
  static volatile Semaphore [] B = new Semaphore [N];
  static {
    for(int i = 0; i < N; i++) {
      B[i] = new Semaphore(0, false);
    }   
  }
  static volatile int[] R = new int[N];
  static volatile int counter = 0;
  
  /*
   * Create a new process with given id
   */ 
  public Q3A(int id) {
    this.id = id;
  }
  
  /*
   * run() constitutes the main entry for thread execution
   */ 
  public void run() {
    newSignal(this.id);
    for(int j = 0; j < N; j++) {
      newWait(this.id);
      // Critical section
      System.out.println("P" + this.id + " is in the CS for iteration " + j);
      delay(0, 20);
      newSignal(this.id);
    }
  }
  
  /*
   * signal() is used by a process when it is done with its Baphore
   */ 
  public void signal(Semaphore B) {
    B.release();
  }
  
  /*
   * newSignal() is used by a process when it wishes to signal 
   * the "priority Baphore"
   */
  public void newSignal(int i) { 
    R[i] = 0;
    counter--; 
    if(counter > 0) { 
      int max = max(); 
      signal(B[max]); 
    } 
  } 
  
  /*
   * wait() is used by a process when it wishes to aquire a Baphore
   */ 
  public void wait(Semaphore B){
    try {
      B.acquire();
    }
    catch(InterruptedException e) {
    }
  }
  
  /*
   * newWait() is used by a process when it wishes to wait on 
   * the "priority Baphore"
   */
  public void newWait(int i) { 
    R[i] = i;  
    counter++; 
    if (counter > 1){ 
      wait(B[i]);  
    } 
  } 
  
  /*
   * max() finds Baphore with the highest priority in R[]
   * (i.e. the "priority Baphore")
   */ 
  public int max() {
    int maxPriority = R[0];
    for(int i=1; i<N; i++) {
      if(R[i] > maxPriority) {
        maxPriority = R[i];
      }
    }
    return maxPriority;
  }
  
  /*
   * delay() puts the process to sleep for some
   * random time in interval given (i.e. 0-20)
   */ 
  public void delay(int min, int max) {
    try {
      int randomTime = rand.nextInt((max - min) + 1) + min;
      sleep(randomTime);
    } 
    catch (InterruptedException e) { }
  }
  
  /*
   * Spawns and runs N threads concurrently 
   */
  public static void main(String[] args) {
    final int N = 0;
    Q3A[] p = new Q3A[N];
    for (int i = 0; i < N; i++) {
      p[i] = new Q3A(i);
      p[i].start();
    }
  }

}
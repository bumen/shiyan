## 锁

### 可重入死锁
    ```
    public class Lock{
      private boolean isLocked = false;
      public synchronized void lock()
        throws InterruptedException{
        while(isLocked){
          wait();
        }
        isLocked = true;
      }
    
      public synchronized void unlock(){
        isLocked = false;
        notify();
      }
    }
    ```
 + 如果一个线程在两次调用lock()间没有调用unlock()方法，那么第二次调用lock()就会被阻塞，就出现了重入锁死。
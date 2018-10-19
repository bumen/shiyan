## java lock

### AQS
 * 两种模式
   + 独占模式
   + 共享模式
 * 具体资源的获取/释放方式交由自定义子类去实现
   + tryAcquire
   + tryRelease
   + tryAcquireShared
   返回负数表示获取失败，进入等待队列
   返回0表示获取成功，没有剩余资源。
   返回正数表示获取成功，还有剩余资源，其它线程还可以去获取。
   + tryReleaseShared
   
 * 等待队列
   + head结点：表示当前获取到资源的结点。如果为null则没有等待队列
   + 第二结点：即head.next结点。
   该结点有资格去尝试获取资源（可能是head结点释放完资源，或可能是中断唤醒，或是jvm无缘唤醒）
   + 等待过程: 
   整个流程中，如果前驱结点的状态不是SIGNAL，那么自己就不能安心去休息.
   需要去找个安心的休息点，同时可以再尝试下看有没有机会轮到自己拿号
     ```
       private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
           int ws = pred.waitStatus;//拿到前驱的状态
           if (ws == Node.SIGNAL)
               //如果已经告诉前驱拿完号后通知自己一下，那就可以安心休息了
               return true;
           if (ws > 0) {
               /*
                * 如果前驱放弃了，那就一直往前找，直到找到最近一个正常等待的状态，并排在它的后边。
                * 注意：那些放弃的结点，由于被自己“加塞”到它们前边，它们相当于形成一个无引用链，稍后就会被保安大叔赶走了(GC回收)！
                */
               do {
                   node.prev = pred = pred.prev;
               } while (pred.waitStatus > 0);
               pred.next = node;
           } else {
                //如果前驱正常，那就把前驱的状态设置成SIGNAL，告诉它拿完号后通知自己一下。有可能失败，人家说不定刚刚释放完呢！
               compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
           }
           return false;
       }
     ```
 
   * release
     + 唤醒等待队列中最前边（第二结点）的那个未放弃线程
     ``` 
      private void unparkSuccessor(Node node) {
          //这里，node一般为当前线程所在的结点。
          int ws = node.waitStatus;
          if (ws < 0)//置零当前线程所在的结点状态，允许失败。
              compareAndSetWaitStatus(node, ws, 0);
      
          Node s = node.next;//找到下一个需要唤醒的结点s
          if (s == null || s.waitStatus > 0) {//如果为空或已取消
              s = null;
              for (Node t = tail; t != null && t != node; t = t.prev)
                  if (t.waitStatus <= 0)//从这里可以看出，<=0的结点，都是还有效的结点。
                      s = t;
          }
          //即使唤醒的是中间某个结点去获取锁。则这个结点会在acquireQueued方法中的shouldParkAfterFailedAcquire做调整，调整为第二结点
          if (s != null)
              LockSupport.unpark(s.thread);//唤醒
      }
     ```
   
   * doAcquireShared
     + 注意  
     （1）：如果第二结点自动被唤醒，如果此时有可用资源，则也会获取成功，同时唤醒后继结点
     （2）：如果第二结点被唤醒，此时可用资源10，但第二结点需要11，第三结点需要2，第四结点需要3
     由于第二结点获取失败，导致后继结点即变有可用资源也获取不了。但是新加入的结点可能获取成功。
     ``` 
       for (;;) {
          final Node p = node.predecessor();//前驱
          if (p == head) {//如果到head的下一个，因为head是拿到资源的线程，此时node被唤醒，很可能是head用完资源来唤醒自己的
              int r = tryAcquireShared(arg);//尝试获取资源
              if (r >= 0) {//成功
                  setHeadAndPropagate(node, r);//将head指向自己，还有剩余资源可以再唤醒之后的线程
                  p.next = null; // help GC
                  if (interrupted)//如果等待过程中被打断过，此时将中断补上。
                      selfInterrupt();
                  failed = false;
                  return;
              }
          }
          
          //判断状态，寻找安全点，进入waiting状态，等着被unpark()或interrupt()
          if (shouldParkAfterFailedAcquire(p, node) &&
              parkAndCheckInterrupt())
              interrupted = true;
       }
     ```
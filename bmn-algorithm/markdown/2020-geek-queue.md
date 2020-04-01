### 队列
 * 先进先出
 * 操作
   1. 入队
   2. 出队
 * 也是一种操作受限的线性表数据结构
 * 实现
   1. 数组实现为顺序队列
   2. 链表实现为链式队列
   
   
 
``` 

public class CircularQueue {
  // 数组：items，数组大小：n
  private String[] items;
  private int n = 0;
  // head表示队头下标，tail表示队尾下标
  private int head = 0;
  private int tail = 0;

  // 申请一个大小为capacity的数组
  public CircularQueue(int capacity) {
    items = new String[capacity];
    n = capacity;
  }

  // 入队
  public boolean enqueue(String item) {
    // 队列满了
    if ((tail + 1) % n == head) return false;
    items[tail] = item;
    tail = (tail + 1) % n;
    return true;
  }

  // 出队
  public String dequeue() {
    // 如果head == tail 表示队列为空
    if (head == tail) return null;
    String ret = items[head];
    head = (head + 1) % n;
    return ret;
  }
}
```
 * 循环队列：队列满的表达式
  + 这里讲一下，这个表达式是怎么来的。
  在一般情况下，我们可以看出来，当队列满时，tail+1=head。
  但是，有个特殊情况，就是tail=n-1，而head=0时，
  这时候，tail+1=n，而head=0，所以用(tail+1)%n == n%n == 0。
  而且，tail+1最大的情况就是 n ，不会大于 n，
  这样tail+1 除了最大情况，不然怎么余 n 都是 tail+1 本身，也就是 head。
  这样，表达式就出现了。

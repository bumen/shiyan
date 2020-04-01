### 二分查找
 * 二分查找针对的是一个有序的数据集合，查找思想有点类似分治思想。
 每次都通过跟区间的中间元素对比，将待查找的区间缩小为之前的一半，直到找到要查找的元素，或者区间被缩小为 0。
 
 
#### 时间复杂度O(logn)
 * 我们假设数据大小是 n，每次查找后数据都会缩小为原来的一半，也就是会除以 2。最坏情况下，直到查找区间被缩小为空，才停止。
   + n, n/2, n/4, n/8, ... , n/2^k
   + 这是一个等比数列，其中n/2^k = 1时，k的值就是总引人缩小次数
   + 而每一次缩小操作只涉及两个数据的大小比较，所以，经过了 k 次区间缩小操作，时间复杂度就是 O(k)。
   + 通过 n/2k=1，我们可以求得 k=log2n，所以时间复杂度就是 O(logn)。
   
 * 这是一种极其高效的时间复杂度，有的时候甚至比时间复杂度是常量级 O(1) 的算法还要高效。为什么这么说呢？
   + 因为 logn 是一个非常“恐怖”的数量级，即便 n 非常非常大，对应的 logn 也很小。比如 n 等于 2 的 32 次方，这个数很大了吧？
   大约是 42 亿。也就是说，如果我们在 42 亿个数据中用二分查找一个数据，最多需要比较 32 次
   
   + 常量级时间复杂度的算法来说，O(1) 有可能表示的是一个非常大的常量值
     - O(1000)、O(10000)。所以，常量级时间复杂度的算法有时候可能还没有 O(logn) 的算法执行效率高

#### 二分查找的递归与非递归实现
 * 最简单的情况就是有序数组中不存在重复元素
   + 代码
   ``` 
        public int bsearch(int[] a, int n, int value) {
          int low = 0;
          int high = n - 1;
        
          while (low <= high) {
            int mid = (low + high) / 2;
            if (a[mid] == value) {
              return mid;
            } else if (a[mid] < value) {
              low = mid + 1;
            } else {
              high = mid - 1;
            }
          }
        
          return -1;
        }
   ```
   + 注意
      1. 循环退出条件
        + 注意是 low<=high，而不是 low
      2.mid 的取值
        + 实际上，mid=(low+high)/2 这种写法是有问题的。因为如果 low 和 high 比较大的话，两者之和就有可能会溢出。
        + 改进的方法是将 mid 的计算方式写成 low+(high-low)/2。
        + 更进一步，如果要将性能优化到极致的话，我们可以将这里的除以 2 操作转化成位运算 low+((high-low)>>1)。
        因为相比除法运算来说，计算机处理位运算要快得多
      3.low 和 high 的更新
        + low=mid+1，high=mid-1。注意这里的 +1 和 -1，如果直接写成 low=mid 或者 high=mid，就可能会发生死循环。
        + 比如，当 high=3，low=3 时，如果 a[3]不等于 value，就会导致一直循环不退出。

   + 递归实现
   ``` 
        // 二分查找的递归实现
        public int bsearch(int[] a, int n, int val) {
          return bsearchInternally(a, 0, n - 1, val);
        }
        
        private int bsearchInternally(int[] a, int low, int high, int value) {
          if (low > high) return -1;
        
          int mid =  low + ((high - low) >> 1);
          if (a[mid] == value) {
            return mid;
          } else if (a[mid] < value) {
            return bsearchInternally(a, mid+1, high, value);
          } else {
            return bsearchInternally(a, low, mid-1, value);
          }
        }
   ```
   
#### 二分查找应用场景的局限性
 * 首先，二分查找依赖的是顺序表结构，简单点说就是数组。
   + 比如链表。答案是不可以的
   
 * 其次，二分查找针对的是有序数据。
   + 二分查找对这一点的要求比较苛刻，数据必须是有序的。如果数据没有序，我们需要先排序
   
 * 再次，数据量太小不适合二分查找。
 
 * 最后，数据量太大也不适合二分查找。
   + 二分查找的底层需要依赖数组这种数据结构，而数组为了支持随机访问的特性，要求内存空间连续，对内存的要求比较苛刻。

#### 变体一：查找第一个值等于给定值的元素
 * 有序数据集合中存在重复的数据，我们希望找到第一个值等于给定值的数据
 * 代码
 ``` 
 
    public int bsearch(int[] a, int n, int value) {
      int low = 0;
      int high = n - 1;
      while (low <= high) {
        int mid =  low + ((high - low) >> 1);
        if (a[mid] > value) {
          high = mid - 1;
        } else if (a[mid] < value) {
          low = mid + 1;
        } else {
          // 等于情况
          if ((mid == 0) || (a[mid - 1] != value)) return mid;
          else high = mid - 1;
        }
      }
      return -1;
    }
 ```
 * 如果 mid 等于 0，那这个元素已经是数组的第一个元素，那它肯定是我们要找的；
 * 如果 mid 不等于 0，但 a[mid]的前一个元素 a[mid-1]不等于 value，那也说明 a[mid]就是我们要找的第一个值等于给定值的元素。
 * 如果经过检查之后发现 a[mid]前面的一个元素 a[mid-1]也等于 value，那说明此时的 a[mid]肯定不是我们要查找的第一个值等于给定值的元素。
 那我们就更新 high=mid-1，因为要找的元素肯定出现在[low, mid-1]之间


#### 变体二：查找最后一个值等于给定值的元素
``` 

public int bsearch(int[] a, int n, int value) {
  int low = 0;
  int high = n - 1;
  while (low <= high) {
    int mid =  low + ((high - low) >> 1);
    if (a[mid] > value) {
      high = mid - 1;
    } else if (a[mid] < value) {
      low = mid + 1;
    } else {
      if ((mid == n - 1) || (a[mid + 1] != value)) return mid;
      else low = mid + 1;
    }
  }
  return -1;
}
```
 * 如果 a[mid]这个元素已经是数组中的最后一个元素了，那它肯定是我们要找的；
 * 如果 a[mid]的后一个元素 a[mid+1]不等于 value，那也说明 a[mid]就是我们要找的最后一个值等于给定值的元素。
 * 如果我们经过检查之后，发现 a[mid]后面的一个元素 a[mid+1]也等于 value，那说明当前的这个 a[mid]并不是最后一个值等于给定值的元素。
 我们就更新 low=mid+1，因为要找的元素肯定出现在[mid+1, high]之间。

#### 变体三：查找第一个大于等于给定值的元素
 * 代码
 ``` 
    
public int bsearch(int[] a, int n, int value) {
  int low = 0;
  int high = n - 1;
  while (low <= high) {
    int mid =  low + ((high - low) >> 1);
    if (a[mid] >= value) {
      if ((mid == 0) || (a[mid - 1] < value)) return mid;
      else high = mid - 1;
    } else {
      low = mid + 1;
    }
  }
  return -1;
}
 ```
 * 如果 a[mid]前面已经没有元素，或者前面一个元素小于要查找的值 value，那 a[mid]就是我们要找的元素。
 * 如果 a[mid-1]也大于等于要查找的值 value，那说明要查找的元素在[low, mid-1]之间，所以，我们将 high 更新为 mid-1。

#### 变体四：查找最后一个小于等于给定值的元素
 * 代码
 ``` 
    
public int bsearch7(int[] a, int n, int value) {
  int low = 0;
  int high = n - 1;
  while (low <= high) {
    int mid =  low + ((high - low) >> 1);
    if (a[mid] > value) {
      high = mid - 1;
    } else {
      if ((mid == n - 1) || (a[mid + 1] > value)) return mid;
      else low = mid + 1;
    }
  }
  return -1;
}
 ```

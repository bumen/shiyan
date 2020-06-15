### 线性排序即时间复杂度为O(n)
 * 桶排序
 * 计数排序
 * 基数排序
 > 之所以能做到线性的时间复杂度，
 主要原因是，这三个算法是非基于比较的排序算法，都不涉及元素之间的比较操作
 
 
#### 桶排序
 * 定义
   + 核心思想是将要排序的数据分到几个有序的桶里，每个桶里的数据再单独进行排序。
   桶内排完序之后，再把每个桶里的数据按照顺序依次取出，组成的序列就是有序的了。
 * 时间复杂度
   1. 如果要排序的数据有 n 个，我们把它们均匀地划分到 m 个桶内，每个桶里就有 k=n/m 个元素。
   2. 每个桶内部使用快速排序，时间复杂度为 O(k * logk)
   3. m 个桶排序的时间复杂度就是 O(m * k * logk)，因为 k=n/m，所以整个桶排序的时间复杂度就是 O(n*log(n/m))。
   4. 当桶的个数 m 接近数据个数 n 时，log(n/m) 就是一个非常小的常量，这个时候桶排序的时间复杂度接近 O(n)。
   
 * 注意
   + 数据在各个桶之间的分布是比较均匀的。
   如果数据经过桶的划分之后，有些桶里的数据非常多，有些非常少，很不平均，那桶内数据排序的时间复杂度就不是常量级了。
   在极端情况下，如果数据都被划分到一个桶里，那就退化为 O(nlogn) 的排序算法了。
   
 * 使用
   + 桶排序比较适合用在外部排序中
   + 所谓的外部排序就是数据存储在外部磁盘中，数据量比较大，内存有限，无法将数据全部加载到内存中
   
 * 例子
   + 我们有 10GB 的订单数据，我们希望按订单金额（假设金额都是正整数）进行排序，
   但是我们的内存有限，只有几百 MB，没办法一次性把 10GB 的数据都加载到内存中。
   这个时候该怎么办呢？
   
   1. 我们可以先扫描一遍文件，看订单金额所处的数据范围。
   假设经过扫描之后我们得到，订单金额最小是 1 元，最大是 10 万元
   2. 我们将所有订单根据金额划分到 100 个桶里，第一个桶我们存储金额在 1 元到 1000 元之内的订单
   第二桶存储金额在 1001 元到 2000 元之内的订单，以此类推。每一个桶对应一个文件，
   并且按照金额范围的大小顺序编号命名（00，01，02…99）。
   3. 每个文件大约100MB, 我们就可以将这 100 个小文件依次放到内存中，用快排来排序。
   4. 等所有文件都排好序之后，我们只需要按照文件编号，从小到大依次读取每个小文件中的订单数据，
   并将其写入到一个文件中，那这个文件中存储的就是按照金额从小到大排序的订单数据了。
   > 可能数据不一定是均匀分布的，划分之后对应的文件就会很大，没法一次性读入内存
   5. 针对这些划分之后还是比较大的文件，我们可以继续划分
   
#### 计数排序
 * 当要排序的 n 个数据，所处的范围并不大的时候，比如最大值是 k，我们就可以把数据划分成 k 个桶。
 每个桶内的数据值都是相同的，省掉了桶内排序的时间。
 * 代码
 ``` 
    
    // 计数排序，a是数组，n是数组大小。假设数组中存储的都是非负整数。
    public void countingSort(int[] a, int n) {
      if (n <= 1) return;
    
      // 查找数组中数据的范围
      // max就是k个桶
      int max = a[0]; 
      for (int i = 1; i < n; ++i) {
        if (max < a[i]) {
          max = a[i];
        }
      }
    
      int[] c = new int[max + 1]; // 申请一个计数数组c，下标大小[0,max]
      for (int i = 0; i <= max; ++i) {
        c[i] = 0;
      }
    
      // 计算每个元素的个数，放入c中
      for (int i = 0; i < n; ++i) {
        c[a[i]]++;
      }
    
      // 依次累加
      for (int i = 1; i <= max; ++i) {
        c[i] = c[i-1] + c[i];
      }
    
      // 临时数组r，存储排序之后的结果
      int[] r = new int[n];
      // 计算排序的关键步骤，有点难理解
      for (int i = n - 1; i >= 0; --i) {
        int index = c[a[i]]-1;
        r[index] = a[i];
        c[a[i]]--;
      }
    
      // 将结果拷贝给a数组
      for (int i = 0; i < n; ++i) {
        a[i] = r[i];
      }
    }
 ```
 
 * 注意
   + 我总结一下，计数排序只能用在数据范围不大的场景中，
   如果数据范围 k 比要排序的数据 n 大很多，就不适合用计数排序了。
   而且，计数排序只能给非负整数排序，
   如果要排序的数据是其他类型的，要将其在不改变相对大小的情况下，转化为非负整数。
   
 * 使用
   + 要排的数据很多，但依赖的排序属性值在一个很小的范围时可以使用
 * 例子
   + 我们都经历过高考，高考查分数系统你还记得吗？我们查分数的时候，系统会显示我们的成绩以及所在省的排名。
   如果你所在的省有 50 万考生，如何通过成绩快速排序得出名次呢？
   + 考生的满分是 900 分，最小是 0 分，这个数据的范围很小，所以我们可以分成 901 个桶，对应分数从 0 分到 900 分。
   根据考生的成绩，我们将这 50 万考生划分到这 901 个桶里。
   桶内的数据都是分数相同的考生，所以并不需要再进行排序。
   我们只需要依次扫描每个桶，将桶内的考生依次输出到一个数组中，就实现了 50 万考生的排序。
   因为只涉及扫描遍历操作，所以时间复杂度是 O(n)。
   
 #### 基数排序（Radix sort）
   
 * 先按照最后一位来排序手机号码，然后，再按照倒数第二位重新排序，以此类推，最后按照第一位重新排序。
 经过 11 次排序之后，手机号码就都有序了。
   + 根据每一位来排序，我们可以用刚讲过的桶排序或者计数排序，它们的时间复杂度可以做到 O(n)。
   + 如果要排序的数据有 k 位，那我们就需要 k 次桶排序或者计数排序，总的时间复杂度是 O(k*n)。
   + 当 k 不大的时候，比如手机号码排序的例子，k 最大就是 11，所以基数排序的时间复杂度就近似于 O(n)。
 
 * 注意
   + 这里按照每位来排序的排序算法要是稳定的，否则这个实现思路就是不正确的。
    因为如果是非稳定排序算法，那最后一次排序只会考虑最高位的大小顺序，
    完全不管其他位的大小关系，那么低位的排序就完全没有意义了。
    
 * 使用
   + 基数排序对要排序的数据是有要求的，需要可以分割出独立的“位”来比较，而且位之间有递进的关系，
   如果 a 数据的高位比 b 数据大，那剩下的低位就不用比较了。
   + 除此之外，每一位的数据范围不能太大，要可以用线性排序算法来排序
   ，否则，基数排序的时间复杂度就无法做到 O(n) 了。
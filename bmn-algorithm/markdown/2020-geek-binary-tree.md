### 二叉树

#### 树
 * 有根节点，子节点，叶子节点
 * 关系
   + 父子
   + 兄弟
   
 * 树的高度
   + 根节点的高度
 * 节点高度
   + 节点到叶子节点的最长路径（边数）
   + 从树下往树尖上数，叶子高度为0，每往加一层就加1
 * 节点深度
   + 根节点到这个节点所经历的边的个数
   + 从树尖上往树下数，根节点深度为0，每往下一层就加1
 * 节点的层
   + 节点的深度 + 1
   + 从根节点往下数，根节点为第1层，每往下一层就加1
   
 ``` 
                              高度    深度    层
                E              3       0     1
                
           A        B          2       1     2
        
       C     D   F     G       1       2     3 
      - -             -       
    I    G           H         0       3     4
 
 ```
   
#### 二叉树
 * 一个节点，最多只有两个子节点。左节点+右节点
 * 满二叉树
   + 叶子节点全都在最底层，除了叶子节点之外，每个节点都有左右两个子节点
 * 完全二叉树
   + 叶子节点都在最底下两层，最后一层的叶子节点都靠左排列
   + 并且除了最后一层，其他层的节点个数都要达到最大
   
 * 为什么特殊拿出完全二叉树来说
   + 完全二叉树数组是顺序存储的
   + 如果是非完全二叉树存储时会浪费较多的存储空间
   
 * 想要存储一棵二叉树，我们有两种方法
   + 一种是基于指针或者引用的二叉链式存储法
   + 一种是基于数组的顺序存储法
     - 节点坐标递推公式
     - 根节点下标为i= 1, 则左节点为：2*i=2, 右节点为:2*i+1= 3
     - 父节点下标：当前节点下标/2
     
 * 遍历
   + 先根
   + 中根
   + 后根
   + 实际遍历就是一个递归过程，**写递归代码的关键，就是看能不能写出递推公式**
   + 时间复杂度
     - 每个节点，被访问一次所是O(n)
     
   
#### 二叉查找树
 * 它不仅仅支持快速查找一个数据，还支持快速插入、删除一个数据。
 * 二叉查找树要求
   1. 在树中的任意一个节点，其左子树中的每个节点的值，都要小于这个节点的值
   2. 右子树节点的值都大于这个节点的值
   ``` 
         13
     10     16
    9  11  14
    左，     右
   ```
   
 * 查找与插入操作相似
 * 删除操作
   + 第一种情况
     - 如果要删除的节点没有子节点，我们只需要直接将父节点中，指向要删除节点的指针置为 null
   + 第二种情况
     - 如果要删除的节点只有一个子节点（只有左子节点或者右子节点），我们只需要更新父节点中，指向要删除节点的指针，
     让它指向要删除节点的子节点就可以了
     
   + 第三种情况
     - 如果要删除的节点有两个子节点
     - 我们需要找到这个节点的右子树中的最小节点，把它替换到要删除的节点上。
     - 然后再删除掉这个最小节点，因为最小节点肯定没有左子节点
  
   + 关于二叉查找树的删除操作，还有个非常简单、取巧的方法
     - 就是单纯将要删除的节点标记为“已删除”，但是并不真正从树中将这个节点去掉。
     - 这样原本删除的节点还需要存储在内存中，比较浪费内存空间，但是删除操作就变得简单了很多
     - 而且，这种处理方法也并没有增加插入、查找操作代码实现的难度。
     
 * 其它操作
   + 快速地查找最大节点
   + 最小节点
   + 前驱节点
   + 后继节点
   
 * 重要特性
   + 中序遍历二叉查找树，可以输出有序的数据序列，时间复杂度是 O(n)，非常高效。因此，二叉查找树也叫作二叉排序树
 
 * 支持重复数据的二叉查找树
   + 第一种方法比较容易
     - 二叉查找树中每一个节点不仅会存储一个数据，因此我们通过链表和支持动态扩容的数组等数据结构，
     把值相同的数据都存储在同一个节点上
   + 第二种方法比较不好理解，不过更加优雅
     - 每个节点仍然只存储一个数据
     - 在查找插入位置的过程中，如果碰到一个节点的值，与要插入数据的值相同，
     我们就将这个要插入的数据放到这个节点的右子树
     - 也就是说，把这个新插入的数据当作大于这个节点的值来处理。
     - 当要查找数据的时候，遇到值相同的节点，我们并不停止查找操作，
     而是继续在右子树中查找，直到遇到叶子节点，才停止。这样就可以把键值等于要查找值的所有节点都找出来
     - 对于删除操作，我们也需要先查找到每个要删除的节点,然后再按前面讲的删除操作的方法，依次删除。
   > 卫星数据: 在算法导论里，指的是一条纪录（一个对象中）中除了关键字key以外的其他数据
     
 * 二叉查找树的时间复杂度分析
   + 不管是查找，插入，删除都与树的高度成正比，也就是O(height)。所以要求出完全二叉树的高度
   + 如果不是平衡二叉树最坏是O(n)
   + 最好是完全二叉树或满二叉树，其时查找时间就是树的高度
   + 树的高度就等于最大层数减一
   + 包含 n 个节点的完全二叉树中，第一层包含 1 个节点，第二层包含 2 个节点，第三层包含 4 个节点，依次类推，
   下面一层节点个数是上一层的 2 倍，第 K 层包含的节点个数就是 2^(K-1)。
   + 不过，对于完全二叉树来说，最后一层的节点个数有点儿不遵守上面的规律了。
   它包含的节点个数在 1 个到 2^(L-1) 个之间（我们假设最大层数是 L）
   + 如果我们把每一层的节点个数加起来就是总的节点个数 n。也就是说，如果节点的个数是 n，那么 n 满足这样一个关系：
     - n >= 1+2+4+8+...+2^(L-2)+1（最后一层假定一个节点）
     - n <= 1+2+4+8+...+2^(L-2)+2^(L-1) (最后一层假定2^(L-1)个节点)
     ``` 
     
       等比数列前n项求合公式: S = a1(1-q^n)/(1-q) (q!=1, q是公比)
       n >= 1*(1-2^(L-1))/(1-2) + 1    (2^(L-1), L-1表示前几项, 因为最后一项是2^(n-1)时用q^n, 所以它的前一项则q^(n-1))
         >= 2^(L-1) - 1  + 1
         >= 2^(L-1)
       log2(n) >= L - 1
       log2(n) + 1 >= L
       
       
       n <= 1*(1-2^L)/(1-2)
       n <= 2^L -1
       n + 1 <= 2^L
       log2(n+1) <= L
       
       所以： log2(n+1) <= L <= log2(n) + 1
               
     ```
   + 借助等比数列的求和公式，我们可以计算出，L 的范围是[log2(n+1), log2n +1]。
   完全二叉树的层数小于等于 log2n +1，也就是说，完全二叉树的高度小于等于 log2n
   + 另一思路
      - 二叉查找树类似二分法搜索，每次缩小一半的区间，而二分查找法时间复杂度就是logN
     
     
 * 与散列表对比优点
   1. 散列表数据是无序的，如果要输出顺序，则需要先排序。二叉查找树如果中序遍历就可以在O(n)时间复杂度内输出有序数列。
   2. 散列扩容耗时很多，当散列冲突时，性能不稳定。通常平衡二叉查找树的性能非常很定，时间复杂度很定在O(log(n))
   3. 如果哈希冲突，可能查找复杂度常量值不一定比log(n)小
   4. 散列表构造比二叉查找树要复杂，需要考虑的东西很多，如散列函数，冲突解决，扩容，缩容。而二叉查找树只要考试平衡
   5. 为了避免过多散列冲突，散列表装载因子不能太大，特别是基于开放寻址解决冲突的散列表，不然会浪费一定的存储空间 
  
    
    

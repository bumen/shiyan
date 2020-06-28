### 第6章

### 结构指针
 * 使用
   + struct Point p1,  *pp;
   + pp = &p1;
   + 访问成员变量：(*pp).x, (*pp).y
     - 这种方式写起来比较复杂
   + 简写方式
     - pp->x, pp->y
   
   
 * 结构声明
  ``` 
    struct {
        int len;
        chat *str;
    } *p;
  ```
   + ++p->len: 因为->优先级高，所以等价于++(p->len)，将len值加1
   + (++p)->len: 将先执行p加1，再对len操作
   + (p++)->len: 将先执行len操作，再执行p加1
   
   + *p->str: 取str指向的值
   + *p->str++ = *str++: 取str值，再将str + 1
   + (*p->str)++: 取str值再加1
   + *p++->str: 取str值，再将p加1
   
### 指针引用
 * struct point tab[100];
   + struct point *low = &tab[0];
   + struct point *hight = &tab[n];
   + &tab[-1]和&tab[n]都超出了数组 tab 的范围。前者是绝对非法的，
   而对后者的间接引用也是非法的。但是，C 语言的定义保证数组末尾之后的第一个元素（即
   &tab[n]）的指针算术运算可以正确执行
   
### 函数返回复杂类型
 * 当函数的返回值类型比较复杂时  
 `struct key *binsearch(char *word, struct key *tab, int n)`
   + 很难看出函数名，也不太容易使用文本编辑器找到函数名
 * 我们可以采用另一种格式书写上述语句：  
 ``` 
    struct key *
    binsearch(char *word, struct key *tab, int n)
 ```
 
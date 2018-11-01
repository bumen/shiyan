package com.bmn.algorithm.impl;

/**
 * 桶排序
 *
 * 有[min, max] 且内部数据分布均匀
 *
 * 分成n个桶来存放数据，每个桶都是有一个数字范围如：int[10], int[0]  = 0-9, int[1] = 10-29, int[2]=30-39, int[n]=10*i-10*i+9;
 *
 * 分在一个桶里的数据，使用插入顺序放置
 *
 * 最后每个桶里的数据都是有序的。
 *
 * 最终。从0-10 遍历n个桶。然后就是有序的
 *
 */
public class BucketSort {
}

package com.ourpalm.tank.app.map.astar;

import java.util.Comparator;
import java.util.List;

/**
 * 定义了一个堆接口
 */
/**
 * @author zhix
 *
 * @param <E>
 */
public interface Heap<E> {

	/**
	 * 添加一个元素至堆集合中
	 *
	 * @param e 元素
	 */
	void push(E e);

	/**
	 * 将堆顶元素从堆中推出
	 *
	 * @return 原堆顶元素
	 */
	E shift();

	/**
	 * 预览堆顶元素但不取出
	 *
	 * @return 堆顶元素
	 */
	E peek();

	/**
	 * 返回堆的大小
	 *
	 * @return 堆的大小
	 */
	int size();

	/**
	 * 返回堆现有的比较器
	 *
	 * @return 堆现有的比较器
	 */
	Comparator<E> getComparator();

	/**
	 * 将堆转化为等效的列表视图
	 *
	 * @return 转的等效列表视图
	 */
	List<E> toList();
}
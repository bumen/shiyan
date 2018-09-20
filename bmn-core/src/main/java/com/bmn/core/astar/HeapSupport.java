package com.ourpalm.tank.app.map.astar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HeapSupport<E> extends ArrayList<E> implements Heap<E> {

	private static final long serialVersionUID = 7888928584533854727L;

	private final Comparator<E> heapComparator;

	public HeapSupport(Comparator<E> heapComparator) {
		this.heapComparator = heapComparator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E peek() {
		return isEmpty() ? null : get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void push(E e) {
		int size = size();
		add(e);
		if (size > 1) {
			siftUp(size);
		}
	}

	// 向上调整
	private void siftUp(int start) {

		int currIdx   = start;              // 当前节点(current)的位置
		int parentIdx = (currIdx - 1) >> 1; // 父(parent)结点的位置
		E currElement = get(currIdx);       // 当前节点(current)的大小

		while (currIdx > 0) {
			int cmp = heapComparator.compare(get(parentIdx), currElement);
			if (cmp >= 0)
				break;
			else {
				set(currIdx, get(parentIdx));
				currIdx = parentIdx;
				parentIdx = (parentIdx - 1) >> 1;
			}
		}

		set(currIdx, currElement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E shift() {
		if (isEmpty()) {
			return null;
		}

		E first  = peek();

		int size = size();
		set(0, get(size - 1));
		remove(size - 1);

		if (size() > 1) {
			siftDown(0, size() - 1);
		}

		return first;
	}

	// 向下调整
	private void siftDown(int start, int end) {

		int currIdx      = start;              // 当前(current)节点的位置
		int leftChildIdx = (currIdx << 1) + 1; // 左(left)孩子的位置
		E   currElement  = get(currIdx);       // 当前(current)节点的大小

		while (leftChildIdx <= end) {
			int cmp;
			if (leftChildIdx != end) {
				cmp = heapComparator.compare(get(leftChildIdx), get(leftChildIdx + 1));
				// "l"是左孩子，"l+1"是右孩子
				if (leftChildIdx < end && cmp < 0)
					leftChildIdx++; // 左右两孩子中选择较大者，即mHeap[l+1]
			}
			cmp = heapComparator.compare(currElement, get(leftChildIdx));
			if (cmp >= 0)
				break; // 调整结束
			else {
				set(currIdx, get(leftChildIdx));
				currIdx = leftChildIdx;
				leftChildIdx = (leftChildIdx << 1) + 1;
			}
		}

		set(currIdx, currElement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<E> toList() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Comparator<E> getComparator() {
		return heapComparator;
	}
}

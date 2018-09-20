package com.bmn.core.dfa.bean;

import java.util.HashMap;
import java.util.Map;

public class CharNode {
	private char key;	//敏感字符 
	private boolean isEnd;	////表示敏感词的最后一个字符
	private Map<Character, CharNode> next;	//敏感字符key后的一个字符的集合
	
	public CharNode(char key) {
		this.key = key;
		this.next = new HashMap<>();
	}
	
	public char getKey() {
		return key;
	}
	public void setKey(char key) {
		this.key = key;
	}
	public boolean isEnd() {
		return isEnd;
	}
	public void setEnd() {
		this.isEnd = true;
	}

	public Map<Character, CharNode> getNext() {
		return next;
	}

	public void setNext(Map<Character, CharNode> next) {
		this.next = next;
	}

	public CharNode get(char key) {
		CharNode node = this.next.get(key);
		if(node == null) {
			node = new CharNode(key);
			this.next.put(key, node);
		}
		return node;
	}
	
	public CharNode match(char key) {
		return this.next.get(key);
	}
	
}

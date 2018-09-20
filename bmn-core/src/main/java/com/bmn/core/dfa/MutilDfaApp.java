package com.bmn.core.dfa;

import java.util.Set;


public interface MutilDfaApp {

	 void start();

	 void stop();

	
	public Set<String> matchSensitiveWord(String txt);
	
	/**
	 * 把敏感词，替换为指定字符 
	 * @param txt
	 * @param replaceStr
	 * @return
	 */
	public String replaceSensitiveWord(String txt, String replaceStr);
}

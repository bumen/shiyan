package com.bmn.core.dfa;

import com.bmn.core.dfa.bean.CharNode;
import com.bmn.util.StringUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;


public class MutilDfaAppImpl implements MutilDfaApp{

	private static final String ENCODING = "UTF-8";
	
	private String path;
	
	private CharNode root;
	private int maxDeep;	//最长的敏感词，长度
	
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public void start() {
		try {
			loadSensitiveWord(readSensitiveWordFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadSensitiveWord(Set<String> keyWordSet) {
		root = new CharNode("r".charAt(0));
		
		if(StringUtils.isEmpty(keyWordSet)) {
			return;
		}
		
		String key = null;   
		int count = 0;
		CharNode end = null;
		
        Iterator<String> iterator = keyWordSet.iterator();  
        while(iterator.hasNext()){  
            key = iterator.next();    //关键字  
            count = key.length();
            end = root;		//每个敏感词，都从根节点开始分解
            
            for(int i = 0; i < count; i++){  
                char keyChar = key.charAt(i);       //转换成char型  
                CharNode node = end.get(keyChar);       //获取  
                end = node;
            }  
            
            maxDeep = Math.max(maxDeep, count);
            
            end.setEnd();	//表示敏感词的最后一个字符
        }  
	}
	
	@Override
	public Set<String> matchSensitiveWord(String txt){  
		txt = StringUtils.trimAllWhitespace(txt);
		txt = txt.trim().toLowerCase();		//全已小写匹配
		 
		Set<String> set = new HashSet<>();
        char word = 0;  
        CharNode end  = null;
        int maxEndIndex = -1;
        for(int i = 0, l = txt.length(); i < l; i++){  
            word = txt.charAt(i);  
            end = root.match(word);     //获取指定key  
            
            if(end == null)
            	continue;
            
            if(end.isEnd()) {
            	maxEndIndex = i;
            }
            
            for(int j = 1, ll = Math.min(maxDeep, l - i); j < ll; j++) {
            	end = end.match(txt.charAt(i +j));
            	
            	//匹配结束
            	if(end == null) {
            		break;
            	}
            	
            	//找到一个敏感词
            	if(end.isEnd()){       
            		maxEndIndex = i + j;	//记录敏感词结束索引
	            }  
            }
            
            if(maxEndIndex >= 0) {
            	set.add(txt.substring(i, maxEndIndex + 1));
            	i += maxEndIndex;
            	maxEndIndex = -1;	//避免只匹配了第1个敏感字
            }
            
        }  
        return set;  
	 }  
	
	@Override
	public String replaceSensitiveWord(String txt, String replaceStr){  
		txt = txt.trim().toLowerCase();		//全已小写匹配
		StringBuilder sb = new StringBuilder(txt);
		
		int whitespaceCount = StringUtils.countOccurrencesOf(txt, " ");
        char word = 0;  
        CharNode end  = null;
        int maxEndIndex = -1;
        for(int i = 0, l = txt.length(); i < l; i++){  
            word = txt.charAt(i);  
            if(Character.isWhitespace(word)) {
            	continue;
            }
            
            end = root.match(word);     //每次从根节点开始找，获取指定key  
            //未匹配首字符
            if(end == null) {
            	continue;
            }
            
            //已经是一个敏感词
            if(end.isEnd()) {
            	maxEndIndex = i;
            }
            
            //找到第一个敏感字后，开始向后查找最长敏感词
            for(int j = 1, ll = Math.min(maxDeep + whitespaceCount, l - i); j < ll; j++) {
            	word = txt.charAt(i +j);
            	//空格过滤
            	if(Character.isWhitespace(word)) {
            		continue;
            	}
            	
            	end = end.match(word);
            	//匹配结束
            	if(end == null) {
            		break;
            	}
            	
            	//找到一个敏感词
            	if(end.isEnd()){       
            		maxEndIndex = i + j;	//记录敏感词结束索引
	            }  
            }
            
            if(maxEndIndex >= 0) {
            	sb.replace(i, maxEndIndex + 1, replaceStr);
            	i += maxEndIndex;
            	maxEndIndex = -1;	//避免只匹配了第1个敏感字
            }
        }  
        return sb.toString();  
	 }  
	
	private Set<String> readSensitiveWordFile() throws Exception {
		Set<String> set = null;
		
		//ClassPathResource res = new ClassPathResource(path);
		File file = new File("");//res.getFile();
		InputStreamReader read = new InputStreamReader(new FileInputStream(file), ENCODING);
		BufferedReader br = null;
		try {
			set = new HashSet<String>();
			br = new BufferedReader(read);
			String txt = null;
			while((txt = br.readLine()) != null){    //读取文件，将文件内容放入到set中
				if(txt == null || txt.equals("")) {
					continue;
				}
				set.add(txt.trim().toLowerCase());
		    }
		} catch (Exception e) {
			// logger.error("加载敏感词库发生异常...", e);
		}finally{
			if(br != null){
				br.close();
			}
			read.close();     //关闭文件流
		}
		return set;
	}

	@Override
	public void stop() {
		
	}
}

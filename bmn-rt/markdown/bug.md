### gof 配置解释bug

 * 使用
 ``` 
    public Map<String, String> getChildrens(String key, boolean recursive, boolean onlyDir) {
    		m_valuesLock.readLock().tryLock();
    		try {
    			if (!key.endsWith(kDelimiterString))
    				key = key + kDelimiter;
    			key = m_root + key;
    			int deep = Utils.countChar(key, kDelimiter);
    			Map<String, String> result = new HashMap<>();
                // 先分析下面的Utils方法后，得出特殊情况下会出现问题。但这里使用都是以kDelimiter结尾是不会出现问题
                // 所以其它地方谨慎什么此Utils方法

                // 本意是：
                // 取出一个路径所所有key
    			for (Map.Entry<String, String> entry : m_values.subMap(key, Utils.prefixEndOf(key)).entrySet()) {
    				if (onlyDir && !entry.getValue().equals(s_DirectoryIdentity))
    					continue;
    				if (!onlyDir && entry.getValue().equals(s_DirectoryIdentity))
    					continue;
    
    				String ekey = entry.getKey().replaceAll(m_root, "");
    				if (recursive)
    					result.put(ekey, entry.getValue());
    				else if (Utils.countChar(entry.getKey(), kDelimiter) == deep) {
    					result.put(ekey, entry.getValue());
    				}
    			}
    			return result;
    		} finally {
    			m_valuesLock.readLock().unlock();
    		}
    	}

	public static final String prefixEndOf(String prefix) {
        // 第二个问题， String.getBytes()方法是会把字符串编码为utf-8字节数组后返回
        // 如果给最后一个字节增加1时，可能会导致再new String(Arrays.copyOf(endKey, i+1))时utf-8解码失败
        // 如：“汿”字的utf-8编码后的为3个字节，最后一个字节为：10FFFFFF，把最后一个字节加1后为11000000
        // 再把修改后的字节数组解码为字符串后，出现解码畸形。

        // 如果把此接口功能描述为把一个给定字符串加1（其它任何情况下使用都是不正确的），则解决思路
        // 取出最后一个字符，然后拿到字符码点，把码点加1
		byte[] endKey = prefix.getBytes().clone();
		for (int i = endKey.length - 1; i >= 0; i--) {

            // 第一个问题
            // 首先这个判断是多余的，因为byte比较时是转换成int比较
            // byte(-128-127) < 0xff(255)
			if (endKey[i] < 0xff) {
				endKey[i] = (byte) (endKey[i] + 1);
				return new String(Arrays.copyOf(endKey, i + 1));
			}
		}
		throw new SysException("NO_PREFIX_END");
	} 

    // jdk11 new String()时，对于utf-8编码畸形处理
       1. 把畸形字符，默认替换为unicode字符码点：65533所表示的字符（即?，一个问题形状）
       注意这是一个很大的字符码点，比较时注意
       2. 跳过此畸形字符，继续解码后续字节
```

 * 实验代码
 ``` 
    public static void main(String[] args) {
    		String key = "asdf/as中/asf2";
    		String key1 = "asdf/as中/asf3";
    		String key2 = "asdf/as中/asf4/s2/123/122";
    		String key3 = "asdf/as中";
    		String key4 = "asdf/as马";
    
    		TreeMap<String, String> ks = new TreeMap<>();
    		// ks.put(key, "1");
    		// ks.put(key1, "2");
    		// ks.put(key2, "3");
    		ks.put(key3, "4");
    		ks.put(key4, "5");
    
    		String xxx = "汉";
    
    		byte[] ss = xxx.getBytes();
                
            // 将最后一个字节改为1011FFFF = 191
    		ss[2] = (byte) (191 & 0xFF);

            //ss[2] = (byte)(192 & 0xFF);, 此时就是一个畸形字节，此编码解码失败

    		for (byte b : ss) {
    			System.out.println(Integer.toHexString(b));
    		}
    
    		String u =  new String(ss) + "汉";
    		System.out.println(u);
    
    		ks.put(xxx, "sfd");
    
    		ks.put(u, "dd");
    
    		System.out.println( Utils.prefixEndOf(u));
    
    
    
    
    
    		int x = "中".codePointBefore(1) + 1;
    
    		String vv = Character.toString(x);
    
    		System.out.println(Utils.prefixEndOf(key3));
    
    		System.out.println(ks.subMap(key3, Utils.prefixEndOf(key3)));
    	} 
```
package com.bmn.rt.spi;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

public abstract class StringProvider {
	
	private static StringProvider provider;

	private static boolean loadProviderAsService() {
		 ServiceLoader<StringProvider> sl =
		            ServiceLoader.load(StringProvider.class,
		                               ClassLoader.getSystemClassLoader());
		 
		 Iterator<StringProvider> i = sl.iterator();
	        for (;;) {
	            try {
	                if (!i.hasNext())
	                    return false;
	                provider = i.next();
	                return true;
	            } catch (ServiceConfigurationError sce) {
	                if (sce.getCause() instanceof SecurityException) {
	                    // Ignore the security exception, try the next provider
	                    continue;
	                }
	                throw sce;
	            }
	        }
	}
	
	public static StringProvider provider() {
		 if(provider != null)
			return provider;
		
		 if (loadProviderAsService())
             return provider;
		 
		 provider = new com.bmn.rt.spi.DefaultStringProvider();
		 return provider;
	}
	
	
	abstract String getTT();
}

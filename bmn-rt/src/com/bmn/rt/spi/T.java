package com.bmn.rt.spi;

public class T {

	public static void main(String[] args) {
		
		String cn = "spi.WindowsStringProvider";
		/*try {
			Class<?> c = Class.forName(cn);
			
			c.newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		System.out.println(StringProvider.class.getName());

		StringProvider p = StringProvider.provider();
		
		System.out.println(p.getTT());
	}

}

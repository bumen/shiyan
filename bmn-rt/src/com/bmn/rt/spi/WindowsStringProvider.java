package com.bmn.rt.spi;

public class WindowsStringProvider extends StringProvider{

	{
		System.out.println("init WindowsStringProvider");
	}
	
	static {
		System.out.println("static init windowsStringProvider");
	}
	
	public WindowsStringProvider() {
		System.out.println("new WindowsStringProvider");
	}
	
	
	public String getTT() {
		return "windows spi";
	}

}

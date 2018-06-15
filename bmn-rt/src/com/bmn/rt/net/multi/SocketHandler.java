package com.bmn.rt.net.multi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketHandler implements Runnable{
	
	private final Socket socket;
	
	public SocketHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		
		try {
			BufferedReader sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			PrintWriter sout = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			try {
				String str = sin.readLine();
				System.out.println("server first get : " + str);
				while(!str.equals("bye")) {
					sout.println("server send :" + str);
					sout.flush();
					
					str = sin.readLine();
					System.out.println("server get : " + str);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				sin.close();
				sout.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				socket.close();
				System.out.println("server close: ");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}

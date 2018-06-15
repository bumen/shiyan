package com.bmn.rt.net.single;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SingleClient {
	private static final String host = "127.0.0.1";
	private static final int port = 8081;
	
	private Socket socket;
	
	private void start() {
		InetAddress adr = new InetSocketAddress(host, port).getAddress();
		
		try {
			socket = new Socket(adr, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void request() {
		try {
			start();
			
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			
			
			String msg = "[client] hello";
			out.println(msg);
			out.flush();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			msg = reader.readLine();

			out.println("bye");
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		SingleClient c = new SingleClient();
		c.request();
	}
}

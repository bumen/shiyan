package com.bmn.rt.net.single;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SingleServer {
	private static final int PORT = 8081;
	private ServerSocket server;
	private Map<String, Socket> socketMap = new HashMap<>();
	
	private Socket socket;
	
	
	private void start() throws IOException {
		InetAddress d = InetAddress.getLocalHost();
		InetAddress dd = InetAddress.getByName("localhost");
		server = new ServerSocket(PORT, -1, dd);
		InetAddress d1 = server.getInetAddress();
		InetSocketAddress d2 = (InetSocketAddress)server.getLocalSocketAddress();
		InetAddress d3 = d2.getAddress();
		
	}
	
	public void startOneSocket() {
		
		try {
			start();
			socket = server.accept();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter write = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			
			while(true) {
				String str = reader.readLine();
				if(str.equals("bye")) {
					break;
				}
				write.println("[server] world");
				write.flush();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		
	}
	
	
	public void startMutilSocket() {
		
	}

	public static void main(String[] args) {
		SingleServer server = new SingleServer();
		server.startOneSocket();
	}
}

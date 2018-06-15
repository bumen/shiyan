package com.bmn.rt.net.multi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MutilServer {

	public void start(int port) {
		try {
			ServerSocket server = new ServerSocket(port);
			
			while(true) {
				Socket socket = server.accept();
				
				SocketHandler h = new SocketHandler(socket);
				
				Thread t = new Thread(h);
				t.start();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		MutilServer s = new MutilServer();
		s.start(8080);
	}
}

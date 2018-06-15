package com.bmn.rt.net.multi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class MutilClient {
	
	private Socket socket;
	private final int port;
	
	public MutilClient(int port) {
		this.port = port;
	}
	
	public void start() {
		try {
			this.socket = new Socket();
			this.socket.connect(new InetSocketAddress("localhost", port));
			run();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
				try {
					if(socket != null)
						socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}

	public void run() {
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
			BufferedReader sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			PrintWriter sout = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			try {
				String str = in.readLine();
				while(!str.equals("bye")) {
					sout.println(str);
					sout.flush();
					System.out.println("client send :" + str);
					
					System.out.println(sin.readLine());
					
					str = in.readLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				in.close();
				sin.close();
				sout.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		MutilClient cli = new MutilClient(8080);
		cli.start();
	}
}

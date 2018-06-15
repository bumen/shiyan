package com.bmn.rt.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class T {

	public static void main(String[] args) throws IOException, InterruptedException {
		/*ServerSocketChannel channel = ServerSocketChannel.open();
		
		channel.bind(new InetSocketAddress("127.0.0.1", 8089));
		

		System.out.println(channel.isOpen());
		
		SocketChannel c = SocketChannel.open();
		c.configureBlocking(false);
		System.out.println(c.isOpen());
		
		Selector selector = Selector.open();
		
		c.register(selector, SelectionKey.OP_CONNECT);
		
		System.out.println(c.isRegistered());
		
		
		for(SelectionKey k : selector.keys()) {
			k.cancel();
		}*/
		
		Thread t = new Thread(new ServerT());
		t.start();
		
		
		Thread.sleep(1000);
		Thread c = new Thread(new ClientT());
		
		c.start();
		
	}
	
	public static  class ClientT implements Runnable {

		@Override
		public void run() {
			SocketChannel c;
			try {
				c = SocketChannel.open();
				//c.configureBlocking(false);
				
				
				c.connect(new InetSocketAddress(8089));
				
				while(!c.finishConnect()) {
					System.out.println(c.isConnected());
				}
				
				ByteBuffer buf = ByteBuffer.allocate(1);
				byte b = 0xb;
				buf.put(b);
				buf.flip();
				c.write(buf);
				
				
				
				buf.clear();
				int r  = c.read(buf);
				if(r > 0) {
					System.out.println("client : get value: ");
					System.out.println("client : " + c.isConnected());
					
					c.close();
				}
				
				
				while(c.isConnected()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("is connect");
					c.close();
				}
				
				
				System.out.println("client end");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		
	}
	
	public static  class ServerT implements Runnable{
		
		
		public void start() throws IOException {
			
			final Selector s = Selector.open();
			
			ServerSocketChannel channel = ServerSocketChannel.open();
			
			channel.configureBlocking(false);
			channel.socket().bind(new InetSocketAddress(8089));
			
			channel.register(s, SelectionKey.OP_ACCEPT);
			
			SocketChannel cc = null;
			while(true) {
				
				try{
					int i = s.select(1000);
					
					if(i > 0) {
						
						Iterator<SelectionKey> it = s.selectedKeys().iterator();
						while(it.hasNext()) {
							SelectionKey key = it.next();
							
							if(key.isValid()) {
								if(key.isAcceptable()) {
									ServerSocketChannel c = (ServerSocketChannel)key.channel();
									SocketChannel clientChannel = c.accept();
									clientChannel.configureBlocking(false);
									clientChannel.register(s, SelectionKey.OP_READ);
								} else if(key.isReadable()) {
									cc = (SocketChannel)key.channel();
									
									ByteBuffer buf = ByteBuffer.allocate(1);
									cc.read(buf);
									buf.flip();
									String v = new String(buf.array());
									System.out.println("read value: " + v);
									
									//key.cancel();
									cc.close();
									s.wakeup();
									System.out.println("had wakup");
								}
							}
							it.remove();
						}
						
					} else {
						
						SelectionKey k = cc.keyFor(s);
						System.out.println("key is : " + k.isValid());
						
						System.out.println("client is connected : " +cc.isConnected());
						if(cc.isConnected()) {
							ByteBuffer buf = ByteBuffer.allocate(1);
							byte b = 0xa;
							buf.put(b);
							buf.flip();
							cc.write(buf);
							
							//cc.close();
						} else {
							
							System.out.println("is c " + s.keys().size());
						}
						
						
					}
					
					
				} catch(Exception e) {
					
				}
				
			}
			
			
		}

		@Override
		public void run() {
			try {
				start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

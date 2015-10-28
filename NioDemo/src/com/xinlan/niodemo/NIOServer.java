package com.xinlan.niodemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer {
	private Selector selector;

	public static void main(String[] args) throws IOException {
		NIOServer server = new NIOServer();
		server.initServer(8964);
		server.listen();
	}

	public void initServer(int port) throws IOException {
		// 获取一个通道
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		// 设置通道为非阻塞的
		serverChannel.configureBlocking(false);
		// 通道对应的Socket 绑定端口
		serverChannel.socket().bind(new InetSocketAddress(port));
		// 获取通道管理器
		this.selector = Selector.open();
		// 注册通道到selcctor管理器中
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
	}

	/**
	 * 监听selector上是否有感兴趣的事件
	 * 
	 * @throws IOException
	 */
	public void listen() throws IOException {
		System.out.println("NIOServer 启动成功...");

		while (true) {
			selector.select();// 该方法阻塞 当注册事件到达时返回

			Iterator<SelectionKey> iter = this.selector.selectedKeys().iterator();
			while (iter.hasNext()) {
				SelectionKey key = iter.next();
				iter.remove();
				
				if (key.isAcceptable()) {//建立连接
					accept(key);
				} else if (key.isReadable()) {
					read(key);
				} // end if
			} // end for while
		} // end while
	}
	
	public void accept(SelectionKey key) throws IOException{
		ServerSocketChannel server = (ServerSocketChannel)key.channel();
		SocketChannel channel = server.accept();
		channel.configureBlocking(false);
		
		channel.write(ByteBuffer.wrap(new String("Hello Client...").getBytes()));
		
		channel.register(this.selector, SelectionKey.OP_READ);
	}

	//读取数据  并回显数据
	public void read(SelectionKey key) throws IOException {
			SocketChannel channel = (SocketChannel)key.channel();
			
			ByteBuffer buffer = ByteBuffer.allocate(100);//缓冲区
			channel.read(buffer);//从通道中读取数据 放入缓存中去
			byte[] data = buffer.array();
			String msg = new String(data).trim();
			System.out.println("服务端收到信息："+msg); 
			ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
			channel.write(outBuffer);
	}

}// end class

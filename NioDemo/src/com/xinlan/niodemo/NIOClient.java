package com.xinlan.niodemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOClient {
	private Selector selector;

	public static void main(String[] args) throws IOException {
		NIOClient client = new NIOClient();
		client.initClient("10.24.131.158", 8964);
		client.listen();
	}

	public void initClient(String ip, int port) throws IOException {
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		this.selector = Selector.open();
		channel.bind(new InetSocketAddress(ip, port));
		channel.register(selector, SelectionKey.OP_CONNECT);
	}

	public void listen() throws IOException {
		while (true) {
			selector.select();
			Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
			while (iter.hasNext()) {
				SelectionKey key = iter.next();
				iter.remove();
				if (key.isConnectable()) {
					connect(key);
				} else if (key.isReadable()) {
					read(key);
				} // end if

			} // end while for each

		} // end while
	}

	public void connect(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		if (channel.isConnectionPending()) {
			channel.finishConnect();// 完成连接
		}
		channel.configureBlocking(false);
		channel.write(ByteBuffer.wrap(new String("Hello Server My name is Client!").getBytes()));
		channel.register(selector, SelectionKey.OP_READ);
	}

	// 读取数据 并回显数据
	public void read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();

		ByteBuffer buffer = ByteBuffer.allocate(100);// 缓冲区
		channel.read(buffer);// 从通道中读取数据 放入缓存中去
		byte[] data = buffer.array();
		String msg = new String(data).trim();
		System.out.println("服务端收到信息：" + msg);
		// ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
		// channel.write(outBuffer);
	}
}// end class

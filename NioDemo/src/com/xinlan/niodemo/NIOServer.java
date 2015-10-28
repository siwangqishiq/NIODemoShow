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
		// ��ȡһ��ͨ��
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		// ����ͨ��Ϊ��������
		serverChannel.configureBlocking(false);
		// ͨ����Ӧ��Socket �󶨶˿�
		serverChannel.socket().bind(new InetSocketAddress(port));
		// ��ȡͨ��������
		this.selector = Selector.open();
		// ע��ͨ����selcctor��������
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
	}

	/**
	 * ����selector���Ƿ��и���Ȥ���¼�
	 * 
	 * @throws IOException
	 */
	public void listen() throws IOException {
		System.out.println("NIOServer �����ɹ�...");

		while (true) {
			selector.select();// �÷������� ��ע���¼�����ʱ����

			Iterator<SelectionKey> iter = this.selector.selectedKeys().iterator();
			while (iter.hasNext()) {
				SelectionKey key = iter.next();
				iter.remove();
				
				if (key.isAcceptable()) {//��������
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

	//��ȡ����  ����������
	public void read(SelectionKey key) throws IOException {
			SocketChannel channel = (SocketChannel)key.channel();
			
			ByteBuffer buffer = ByteBuffer.allocate(100);//������
			channel.read(buffer);//��ͨ���ж�ȡ���� ���뻺����ȥ
			byte[] data = buffer.array();
			String msg = new String(data).trim();
			System.out.println("������յ���Ϣ��"+msg); 
			ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
			channel.write(outBuffer);
	}

}// end class

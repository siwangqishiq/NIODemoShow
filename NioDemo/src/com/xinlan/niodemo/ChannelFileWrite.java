package com.xinlan.niodemo;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelFileWrite {
	public static String content = "Hello World!ÄãºÃ ÊÀ½ç!";
	public static byte[] datas = content.getBytes();

	static {
	}

	public static void main(String[] agrs) throws Exception {
		System.out.println(Byte.MAX_VALUE);
		FileOutputStream fos = new FileOutputStream("write.txt");
		FileChannel channel = fos.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		for (int i = 0; i < datas.length; i++) {
			buffer.put(datas[i]);
		} // end for i
		buffer.flip();
		channel.write(buffer);
		
		fos.close();
	}
}// end class

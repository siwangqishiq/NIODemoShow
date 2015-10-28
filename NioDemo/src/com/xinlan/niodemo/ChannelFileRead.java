package com.xinlan.niodemo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

/**
 * 通道读取数据
 * 
 * @author Administrator
 *
 */
public class ChannelFileRead {
	public static void main(String[] agrs) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("my.txt");
			FileChannel channel = fis.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			channel.read(buffer);
			String msg = new String(buffer.array()).trim();
			System.out.println("content---->" + msg);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}// end class

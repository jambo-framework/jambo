package com.jambo.tools.codegen.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

/**
 * <p>
 * Description: 提供基于channels的读写操作. 本Class并不处理各种异常, 而是将异常外抛.
 * </p>
 * 
 * @author Steven
 * @version 1.0
 */
public class WriteFile {
	private static final Log log = LogFactoryImpl.getLog(WriteFile.class);
	private static final int BUFFER_MAX_SIZE = 1024*150 ;

	/**
	 * 
	 */
	private WriteFile(){}

	/**
	 * 把source的内容写入到合法的dest文件中去(非路劲).
	 * @param dest
	 * 				目标文件地址
	 * @param source
	 * 				要写入文件的数据
	 * @return File 
	 * 				返回新生成的文件. 
	 * @throws IOException
	 * 				把一切可能抛出的IO异常抛出
	 */
	public static File write(String dest
			, StringBuffer source ) throws IOException {
		File file = new File(dest);

		if (file.exists()) {
			throw new IllegalArgumentException("文件已存在") ;
		}

		FileOutputStream out = null;
		ByteArrayInputStream input = null;
		try {
			out = new FileOutputStream(file);
			byte[] bytes = source.toString().getBytes();
			input = new ByteArrayInputStream(bytes);
			writeNotFile(input, out) ;
			log.debug("写入完成") ;
		} finally {
			if (null != input){
				input.close();
			}
			if (null != out){
				out.close();
			}
			if (null != source){
				source.delete(0, source.length());
			}
			log.debug("关闭通道资源") ;
		}
		return file ;
	}

	/**
	 * 将非文件Input的内容根据文件输出流写到文件中去. 
	 * 如果要写入的文件已存在, 是否覆盖其中的内容, 取决于提供的流定义.
	 * 如果文件不允许读/写操作, 责抛出IO异常 
	 * @param input
	 * 				非文件的输入流
	 * @param output
	 * 				制定文件的输出流
	 * @throws IOException
	 * 				抛出一切可能发生的IO异常
	 */
	public static void writeNotFile(InputStream input
			, FileOutputStream output) throws IOException {
		FileChannel channel = null;
		ReadableByteChannel read = null;
		try {
			channel = output.getChannel() ;
			read = Channels.newChannel(input);
//			ByteBuffer buffer = ByteBuffer.allocate(resetBufferSize(input.available()));
//			while (read.read(buffer) != -1) {
//				buffer.flip();
//				channel.write(buffer);
//				buffer.clear();
//			}
			channel.transferFrom(read, 0, input.available()) ;
		} finally {
			if (null != read){
				read.close();
			}
			if (null != channel){
				channel.close();
			}
		}
	}

	/**
	 * 将一个文件的内容拷贝到另一个文件中.  
	 * 如果要写入的文件已存在, 是否覆盖其中的内容, 取决于提供的流定义.
	 * 如果文件不允许读/写操作, 责抛出IO异常
	 * @param input
	 * 				要读取的文件流
	 * @param output
	 * 				要写入的文件流
	 * @throws IOException
	 * 				抛出一切可能发生的IO异常
	 */
	public static void write(FileInputStream input
			, FileOutputStream output) throws IOException {
		FileChannel channel = null;
		FileChannel read = null;
		try {
			channel = output.getChannel() ;
			read = input.getChannel() ;
//			ByteBuffer buffer = ByteBuffer.allocate(resetBufferSize(input.available()));
//			while (read.read(buffer) != -1) {
//				buffer.flip();
//				channel.write(buffer);
//				buffer.clear() ;
//			}
			int max_size = input.available() ;
			int read_size = 0 ;
			while (read_size<max_size){
				read_size += read.transferTo(read_size, resetBufferSize(input.available()), channel) ;
			}
		} finally {
			if (null != read){
				read.close();
			}
			if (null != channel){
				channel.close();
			}
		}
	}

	/**
	 * 读取文件内容组装成StringBuffer, 由于是将文件内容直接写入StringBuffer
	 * 中, 不建议读取大文件内容(如上100M的文件)
	 * @param file
	 * 				待读取的文件.
	 * @return StringBuffer
	 * 				返回一个包含全文件内容的StringBuffer
	 * @throws IOException
	 * 				抛出一切可能发生的IO操作异常
	 */
	public static StringBuffer read(String file) throws IOException {
		FileInputStream input = null ;
		try {
			input = new FileInputStream(file) ;
			return read(input) ;
		} finally {
			if (null != input){
				input.close() ;
			}
		}
	}

	/**
	 * 读取输入流中的字节码组装成StringBuffer, 由于是将文件内容直接写入StringBuffer
	 * 中, 不建议读取太大的输入流
	 * @param input
	 * 				提供的输入流
	 * @return StringBuffer
	 * 				返回一个包含输入流内容的StringBuffer
	 * @throws IOException
	 * 				抛出一切可能发生的IO操作异常
	 */
	public static StringBuffer read(InputStream input) 
			throws IOException {
		ReadableByteChannel channel = null;
		StringBuffer sb = null ;
		try {
			channel = Channels.newChannel(input);
			sb = new StringBuffer(input.available()) ;
			ByteBuffer bb = ByteBuffer.allocate(resetBufferSize(input.available())) ;
			while (channel.read(bb)!=-1){
				bb.flip() ;
				bb.limit(bb.remaining()) ;
				sb.append(readLimit(bb.remaining(),bb.array())) ;
			}
			bb.clear() ;
		} finally {
			if (null != channel){
				channel.close() ;
			}
		}
		return sb ;
	}

	/**
	 * 按照实际byte[]大小组装字符串
	 * @param limit
	 * 				实际有效长度.
	 * @param bytes
	 * 				待组装成字符串的byte[]
	 * @return String
	 * 				返回一个装入bytes内容后的String
	 */
	private static String readLimit(int limit, byte[] bytes) {
		return new String(bytes,0 , limit) ;
	}

	/**
	 * 确定缓冲区大小, 目前只是简单的根据输入流的长度
	 * 与类的默认块区大小进行对比, 大于默认块区大小的, 
	 * 以默认的块区大小进行读取/写入操作, 否则以流长度
	 * 作为缓冲区长度进行操作
	 * @param input
	 * 				输入流的长度
	 * @return int 
	 * 				返回确定的缓冲区大小
	 */
	private static int resetBufferSize(int input) {
		return input > BUFFER_MAX_SIZE? BUFFER_MAX_SIZE: input ;
	}

	/**
	 * 测试的操作方法, 生成一个符合规定大小的文件, 等待操作
	 * 本方法不负责该文件的回收, 消毁工作
	 * @param input
	 * @param output
	 * @param bufferLength
	 * @param count
	 * @throws IOException
	 */
	public static void write(InputStream input, OutputStream output
			, int bufferLength , int count) throws IOException {
		WritableByteChannel channel = null;
		ReadableByteChannel read = null;
		try {
			channel = Channels.newChannel(output) ;
			read = Channels.newChannel(input);
			ByteBuffer buffer = ByteBuffer.allocate(bufferLength);
			while (read.read(buffer) != -1) {
				for (int i = 0; i < count; i++){
					buffer.flip();
					channel.write(buffer);
				}
				buffer.clear();
			}
		} finally {
			if (null != read){
				read.close();
			}
			if (null != channel){
				channel.close();
			}
		}
	}

}
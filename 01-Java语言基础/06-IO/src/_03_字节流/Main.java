package _03_字节流;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		test1();
		test2();
	}
	
	/*
	 * 万能文件输出流
	 */
	public static void test1() throws IOException {
		/*
		 * 我们要往一个文件里写数据，那数据相对于我们的程序来说是输出出去了，所以得用输出流
		 * 给定文件的路径
		 * 如果我们是要往文件里追加内容而非覆盖文件的内容，则把后面的append传个true即可，覆盖直接不写就行
		 */                    
		FileOutputStream fos = new FileOutputStream("/Users/ineyee/Desktop/123.txt", true);
		
		/*
		 * write()方法可以接收一个int类型，int类型占用4个字节，但其实这里只会把int类型的低8位————即1个字节的数据给写进去，注意IO流读写的数据都直接是二进制，默认都是UTF-8编码
		 * 77这1个字节的数据在ASCII码表里对应的字符为大写字母M
		 * 74这1个字节的数据在ASCII码表里对应的字符为大写字母J
		 * 所以你此时可以去用文本编辑器打开之后，会直接看到MJ，而不是77、74这两个数字，文本编辑器会自动解码给我们呈现
		 */
		fos.write(77);
		fos.write(74);
		
		/*
		 * write()方法也可以接收一个byte数组，然后一个字节一个字节写进去
		 */
		byte[] bytes = "你好Hello".getBytes();
		fos.write(bytes);
		
		/*
		 * 写完东西后，记得关闭一下输出流
		 */
		fos.close();
	}
	
	/*
	 * 万能文件输入流
	 */
	public static void test2() throws IOException {
		/*
		 * 我们要从一个文件里读数据，那数据相对于我们的程序来说是输入进来了，所以得用输入流
		 * 给定文件的路径
		 */                    
		FileInputStream fis = new FileInputStream("/Users/ineyee/Desktop/123.txt");
		
		/*
		 * read()方法可以直接返回一个int类型，int类型占用4个字节，但其实这里只会把int类型的低8位————即1个字节的数据给返回出来，注意IO流读写的数据都直接是二进制，这里不会自动解码
		 * 每调用一次read()方法就是读取一个字节的数据
		 */
		int byte1 = fis.read();
		System.out.println(byte1); // 77，UTF-8 decode之后对应的是大写字母M
		int byte2 = fis.read();
		System.out.println(byte2); // 74，UTF-8 decode之后对应的是大写字母J
		
		/*
		 * read()方法也可以接收一个byte数组，然后从文件里读取数据一个字节一个字节写进byte数组
		 * 创建一个100字节的数组，尝试读取100个字节的东西存到这个数组里，如果超过100个字节则只读取100个字节，如果不够100个字节则全部读取出来然后数组空着剩余的字节
		 */
		byte[] bytes = new byte[100];
		int len = fis.read(bytes);
		System.out.println("真正读取到的字节数：" + len);
		
		/*
		 * 因为我们知道存储的是字符串，所以直接把二进制数据decode成字符串
		 * 但直接使用String(bytes)转的话，可能100个字节里有空的东西
		 * 所以我们可以指定把数组里那些字节转成字符串：bytes数组，从第0位（注意这个数是位不是字节）开始，一共len个字节，采用"UTF-8"编码，decode
		 */
//		String string = new String(bytes);
		String string = new String(bytes, 0, len, "UTF-8");
		System.out.println("读取到的字符串：" + string);
		
		/*
		 * 读完东西后，记得关闭一下输入流
		 */
		fis.close();
	}
	
}

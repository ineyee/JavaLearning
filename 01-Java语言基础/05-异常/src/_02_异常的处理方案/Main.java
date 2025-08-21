package _02_异常的处理方案;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/*
 * 程序产生了异常，我们一般称之为：抛出了异常。不管是检查型异常还是非检查型异常，都有两种处理方式：
 * 1、用try-catch捕获异常
 * 2、用throws把异常往上抛
 */
public class Main {
	
	public static void main(String[] args) {
		test1();
//		try {
//			test2();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
	}
	
	/*
	 * 一、用try-catch捕获异常
	 * 前端、移动端开发的时候我们很少写try-catch，因为那些平台里的异常都是非检查型异常，也就是说就算我们不处理编译器也不会报错，
	 * 并且只要我们的代码写得严谨点健壮点，那些异常在运行时就不会发生了，所以我们很少写
	 * 
	 * 但是Java开发里就得大量写try-catch，因为Java里有检查型异常这种东西，系统和很多三方库的API都会抛出检查型异常，
	 * 所以开发者不得不写大量try-catch来处理，否则编译都不通过、代码跑都跑不起来
	 * 
	 * 因此在Java开发里大胆地去写try-catch吧，不要觉得写起来很累赘、不要觉得恶心 
	 * 
	 *	try {
	 *		把可能会抛出异常的代码写在try里
	 *	} catch (Throwable e) {
	 *		然后在catch里看看你想对异常做些什么处理，一般我们都是做这三种处理：
	 *		 1、	System.out.println(e.getMessage()); // 打印异常描述
	 *		 2、System.out.println(e); // 打印异常名称 + 异常描述
	 *		 3、e.printStackTrace(); // 最常用：打印抛异常的堆栈信息来调试异常，这样我们就可以在控制台直接点到报错的位置
	 *	}
	 *
	 *	try {
	 *		把可能会抛出异常的代码写在try里
	 *	} catch (Throwable e) {
	 *		然后在catch里看看你想对异常做些什么处理，一般我们都是做这三种处理：
	 *		 1、	System.out.println(e.getMessage()); // 打印异常描述
	 *		 2、System.out.println(e); // 打印异常名称 + 异常描述
	 *		 3、e.printStackTrace(); // 最常用：打印抛异常的堆栈信息来调试异常，这样我们就可以在控制台直接点到报错的位置
	 *	} finally {
	 *		程序不出现异常，即只走了try，也会走到这里执行代码
	 *		程序出现异常，即走了try和catch，也会走到这里执行代码
	 *		我们一般会在这里编写一些关闭、释放资源的代码（比如关闭文件）
	 *	}
	 */
	public static void test1() {
		/*
		 * 如果没用try-catch的话：
		 * 1、数组没越界的话：正常打印1、2、1、3、4、5
		 * 2、数组越界的话：打印完1、2，程序就崩了，不会打印后面的3、4、5
		 * 
		 * 如果用了try-catch的话：
		 * 1、数组没越界的话：正常打印1、2、1、3、5
		 * 2、数组越界的话：打印完1、2，因为2的地方已经抛异常了，所以不会在走3的代码，而是直接走catch打印4，然后再打印5，程序就不会崩掉
		 */
		
		// 没用try-catch
//		System.out.println(1);
//		System.out.println(2);
//		int[] arr = { 1, 2, 3 };
////		System.out.println(arr[0]); // 没越界，不抛异常
//		System.out.println(arr[3]); // 越界，抛异常
//		System.out.println(3);
//		System.out.println(4);
//		System.out.println(5);
		
		// 用了try-catch
		System.out.println(1);
		try {
			System.out.println(2);

			int[] arr = { 1, 2, 3 };
//			System.out.println(arr[0]); // 没越界，不抛异常
			System.out.println(arr[3]); // 越界，抛异常
			
			System.out.println(3);
		} catch (Throwable e) {
			e.printStackTrace(); // 打印抛异常的堆栈信息来调试异常
			
			System.out.println(4);
		}
		System.out.println(5);
	}

	/*
	 * 二、用throws把异常往上抛
	 * 
	 * 用法就是在方法的参数列表和执行体中间throws一下会抛出的异常（这个地方最好写出具体的异常，而不要直接throws一个父类Exception，
	 * 这样别人再调用这个方法的时候才能更精准地针对不同的异常做不同的catch）
	 * 
	 * 这么做的话，test3就会把异常抛给调用它的方法test2，
	 * test2如果不处理还是会报错，所以test2也可以将异常抛给调用它的方法main，
	 * 但是main如果不处理的话还是会报错，所以main方法也可以将异常继续往上抛，
	 * 此时就是抛给JVM了，
	 * 异常抛给JVM后，运行时这个异常一旦发生，程序就会崩溃
	 * 
	 * 所以如果我们要通过throws把异常往上抛，建议最晚要在main函数里try-catch一下来捕获异常处理掉，坚决不能抛给JVM
	 */
	public static void test2() throws FileNotFoundException {
		test3();
	}
	public static void test3() throws FileNotFoundException {
		String filePath = "C://text.txt";
		FileOutputStream fos = new FileOutputStream(filePath);
	}
	
}

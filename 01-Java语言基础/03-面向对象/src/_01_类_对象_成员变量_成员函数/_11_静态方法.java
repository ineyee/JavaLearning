package _01_类_对象_成员变量_成员函数;

public class _11_静态方法 {	
	/*
	 * 1、被static修饰的方法我们称之为类方法或者静态方法
	 * 2、类方法是通过类直接访问：Person.run()
	 * 3、类方法在内存中只有一份，存储在方法区（实例方法都存储在方法区了，更何况类方法）
	 */
	public static void run() {
		System.out.println("Person - run");
	}
}

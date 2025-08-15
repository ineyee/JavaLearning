package _01_类_对象_成员变量_成员函数;

public class _10_静态属性 {
	/*
	 * 1、被static修饰的属性我们称之为类属性或者静态属性
	 * 2、类属性是通过类直接访问：Person.age
	 * 3、类属性在内存中只有一份，存储在方法区（不是静态全局区喔）
	 */
	public static int age = 0;
}

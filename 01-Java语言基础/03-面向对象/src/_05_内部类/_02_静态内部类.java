package _05_内部类;

public class _02_静态内部类 {
	
	public static void main(String[] args) {
		Person02.Cat cat = new Person02.Cat();
		cat.run();
	}
	
}

/*
 * 一、我们再来专门说一下静态内部类
 * 
 * 1、用static修饰的内部类就是静态内部类
 * 
 * 2、一个普通的类内部可以定义实例成员和类成员，
 * 静态内部类内部也可以定义实例成员和类成员，就当它是个普通类一样看待就好了
 * 
 * 3、静态内部类跟类属性、类方法一样都是类的类成员，所以它们都得用外部类的类名才能访问
 * 
 * 所以我们可以把静态内部类看做是少了某些自由（只能用外部类的类名才能访问）、但是换来了某些特殊权
 * 限（能直接访问外部类的类成员，即便外部类的类成员被定义成private）的一个类
 */
// 外部类
class Person02 {
	// 静态属性
	private static String name = "张三";
	// 静态方法
	private static void eat() {
		System.out.println("Person static eat() " + name);
	}
	
	
	// 静态内部类
	static class Cat {
		// 实例属性
		String name = "Tom";
		// 实例方法
	 	void run() {
	 		System.out.println("Cat run() " + name); // Cat run() Tom
	 		
			// 静态内部类内部可以直接访问外部类的类成员，即便定义为私有的
	 		// 如果重名了，那就用外部类.xxx来访问
			System.out.println(Person02.name); // 张三
			eat(); // Person static eat() 张三
		}
	}
}

/*
 * 二、静态内部类和它外部类的关系：
 * 
 * 静态内部类和它的外部类完全没有内存上的关系，
 * 静态内部类完全就是一个自由自在的类，只不过在写代码的时候看起来是写在了一个类内部而已
 */
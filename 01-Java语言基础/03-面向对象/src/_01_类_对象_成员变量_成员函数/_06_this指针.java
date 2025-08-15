package _01_类_对象_成员变量_成员函数;

public class _06_this指针 {

	/*
	 * 一、隐式参数 this 指针
	 * 
	 * 前面我们说到“成员变量的值存储在对象内部，成员函数存储在代码区”，也就是说下面的代码中10存储在person1对象内部、20存储在person2对象内部，run成员函数存储在代码区只有一份
	 * 那么问题来了，既然run成员函数不存储在person1对象和person2对象内部，那run成员函数执行体里是怎么知道person1对象调用的时候就打印10、person2对象调用的时候就打印20的呢
	 * 也就是说代码区的run到底是怎么访问到person1、person2这两块堆区的内存的呢？
	 * 
	 * 其实编译器会为每个成员函数都添加一个隐式参数——this指针——并且这个隐式参数永远位于参数列表的第一位
	 * 外界某个对象通过点语法调用成员函数这种高级语言里的写法，在编译的时候编译器其实就是把外界这个对象的内存地址传递给了成员函数的this指针，于是this指针就指向了成员函数的调用者
	 * 因此我们就可以在成员函数执行体里通过this指针来访问某个具体对象的成员变量、成员函数了，所有的面向对象语言里对象调用成员函数都是这么设计的
	 */
	public static void main(String[] args) {		
		Person06 p1 = new Person06(10, 1.88);
		p1.age = 10;
		p1.run(); // run() 10 1.88
		
		Person06 p2 = new Person06(20, 1.99);
		p2.age = 20;
		p2.run(); // run() 20 1.99
	}

}

/*
 * 二、this 指针的应用场景
 * 
 * Java 里大多数时候我们都可以省略 this 指针，直接调用属性和方法，只有几种场景必须显式写 this：
 * 	区分成员变量与局部变量​
 * 	在构造方法里调用其他构造方法
 */
class Person06 {
	int age;
	double height;
	
	Person06(int age, double height) {
		// 区分成员变量与局部变量​
		this.age = age;
		this.height = height;
	}
	
	Person06(int age) {
		// 在构造方法中调用其他构造方法
		this(age, 0);
	}
	
	void run() {
		System.out.println("run()" + " " + age + " " + height);
	}
}
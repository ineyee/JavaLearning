package _02_封装_继承_多态;

public class _02_继承 {

	public static void main(String[] args) {
		Person02 person = new Person02();
		person.age = 18;
		person.run(); // run() 18
		Person02.height = 1.8;
		Person02.eat(); // static eat() 1.8
		
		Student02 student = new Student02();
		student.age = 14;
		student.run(); // run() 14
		Student02.height = 1.4;
		Student02.eat(); // static eat() 1.4
	}

}

/*
 * 一、继承是指子类拥有了父类所有的属性、方法、静态属性、静态方法，用extends关键字来实现
 * 
 * 注意：Java里所有的类最终都继承自基类Object
 * 也就是说下面的Person类其实就继承自Object：public class Person extends Object { ... }
 * 只不过已经默认了，我们不需要显性地写出来继承自Object而已
 */
class Person02 {
	int age = 0;
	static double height = 0.6;
	
	void run() {
		System.out.println("run()" + " " + age);
	}
	
	static void eat() {
		System.out.println("static eat()" + " " + height);
	}
}

class Student02 extends Person02 {
	
}

/*
 * 二、构造方法的补充
 * 
 * 1、如果子类不重写父类的构造方法，是没有问题的，创建子类的时候无非是默认调用父类的构造方法，就像上面的 Person02 和 Student02 那样
 * 2、如果子类有自己的构造方法——即重写了父类的构造方法，那子类的构造方法里就必须在第一行首先调用一下super()方法————即父类的构造函数，来完成子类继承于父类那部分资源的初始化，然后再做子类自己自定义的内容
 */
class Person0201 {
	int age;
	
	Person0201(int age) {
		this.age = age;
	}
	
	void run() {
		System.out.println("run()" + " " + age);
	}
}

class Student0201 extends Person0201 {
	int no;
	
	Student0201(int no) {
		super(0);
		
		this.no = no;
	}
}

/*
 * 三、方法的重写和调用父类的方法
 * 1、Java里子类重写父类的方法直接重写就行，然后再加个@Override注解来修饰
 * 2、那重写之后怎么调用父类的方法呢？Java里有super关键字来调用父类的方法
 */
/*
 * 顺带了解下注解，Java里以@符号开头的东西就是注解，系统提供了很多注解，我们也可以自己定义注解。常见的注解：
 * 
 * 1、@Override：代表是子类重写了父类的方法
 * 2、@SuppressWarnings({"警告类别"})：消除警告用
 */
class Person0202 {
	void run() {
		System.out.println("Person run()");
	}
}

class Student0202 extends Person0202 {
	@Override
	void run() {
		super.run();
		
		System.out.println("Student run()");
	}
}

/*
 * 四、super关键字
 * 1、super关键字出现在子类的构造方法里时，代表的是调用父类的构造方法
 * 2、super关键字出现在子类的实例方法里时，代表的是调用父类的同名方法
 * 3、Java里子类不能重写父类的类方法，所以super关键字不可能出现在子类的类方法里
 */








package _01_类_对象_成员变量_成员函数;

public class _01_类_对象_成员变量_成员函数 {

	public static void main(String[] args) {
		// 创建一个person对象（必须在构造方法前面加个new关键字）
		Person person = new Person();
		person.age = 10;
		person.run(); // run() 10
	}

}

// 用class关键字定义一个Person类
class Person {
	// 属性
	int age;
	
	// 方法
	void run() {
		System.out.println("run()" + " " + age);
	}
}

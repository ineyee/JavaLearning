package _03数据类型;

/*
 * 泛型的本质就是【数据类型参数化】，也就是说把所操作的数据类型指定为一个参数
 * 这种数据类型可以用在类、接口和方法的创建中，分别称为【泛型类、泛型接口、泛型方法】，从而编写出可重用的类、接口和方法，以便提高代码复用率
 * 
 * 建议的泛型名称为：
 * 	T：Type
 * 	E：Element
 * 	K：Key
 * 	V：Value
 * 	S、U、V：2nd、3rd、4th types
 */
public class _05泛型 {

	public static void main(String[] args) {
		swapInteger(10, 20);
		swapDouble(30.1, 40.1);
		swapNumber(10, 40.1);
		
		Student1 stu1 = new Student1();
		stu1.score = 99.5;
		System.out.println(stu1.score);
		
		Student2 stu2 = new Student2();
		stu2.score = "A";
		System.out.println(stu2.score);
		
		Student<Double> stu3 = new Student<>();
		stu3.score = 80.0;
		System.out.println(stu3.score);
		
		Student<String> stu4 = new Student<>();
		stu4.score = "B";
		System.out.println(stu4.score);
		
		Person person = new Person();
		person.run("Math assignment");
		
		Dog dog = new Dog();
		dog.run(10.20);
		
	}
	
	/*
	 * 一、泛型方法举例
	 */
	public static void swapInteger(int num1, int num2) {
		int temp = num1;
		num1 = num2;
		num2 = temp;
		System.out.println(num1);
		System.out.println(num2);
	}
	
	public static void swapDouble(double num1, double num2) {
		double temp = num1;
		num1 = num2;
		num2 = temp;
		System.out.println(num1);
		System.out.println(num2);
	}
	
	// 泛型方法的<T>要写在方法的返回值前面，而不是方法名的后面
	// 我们可以通过 extends 关键字来限定泛型必须继承自某个类或者实现了某个接口
	public static <T extends Number> void swapNumber(T num1, T num2) {
		T temp = num1;
		num1 = num2;
		num2 = temp;
		System.out.println(num1);
		System.out.println(num2);
	}

}

/*
 * 二、泛型类举例
 */
class Student1 {
	// 数字分数制
	Double score;
}

class Student2 {
	// 等级分数制
	String score;
}

class Student<T> {
	T score;
}

/*
 * 三、泛型类举例
 */
interface Runnable<T> {
	void run(T v);
}

class Person implements Runnable<String> {

	@Override
	public void run(String v) {
		System.out.println("Person is running to complete homework: " + v);
	}
	
}

class Dog implements Runnable<Double> {

	@Override
	public void run(Double v) {
		System.out.println("Dog is running at: "  + v + "km/h");
	}
	
}


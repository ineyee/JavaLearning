package _02_封装_继承_多态;

public class _01_封装 {
	
	public static void main(String[] args) {
		Person01 p = new Person01();
		p.setAge(-10);
		System.out.println(p); // 0
	}
	
}

/*
 * 我们把众多变量和函数都放到一个类里，将来通过类和对象来访问这些属性和方法，而不再是零散地独立地访问众多变量和函数，这可以称之为封装
 * 
 * 通常情况下，我们还会把属性私有化，然后提供公开的 getter 方法和 setter 方法供外界访问属性 
 */
class Person01 {
	private int age = 0;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		if (age < 0) {
			this.age = 0;
		} else {
			this.age = age;
		}
	}

	@Override
	public String toString() {
		return "Person01 [age=" + age + "]";
	}
}
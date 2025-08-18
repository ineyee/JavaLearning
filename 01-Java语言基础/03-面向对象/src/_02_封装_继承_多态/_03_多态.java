package _02_封装_继承_多态;

public class _03_多态 {

	public static void main(String[] args) {
		Animal animal = new Animal();
		Animal animal1 = new Cat();
		Animal animal2 = new Dog();
		
		animal.run(); // Animal run
		animal1.run(); // Cat run
		animal2.run(); // Dog run
	}

}

/*
 * 多态是指子类重写父类的方法、然后父类指针指向子类对象、然后用父类指针调用子类重写的方法，不同的子类就会产生不同的执行结果
 * 
 * Java里父类可以指向子类对象，这是多态，接口也是可以指向实现类的对象，这也是多态
 */

class Animal {
	void run() {
		System.out.println("Animal run");
	}
}

class Cat extends Animal {
	@Override
	void run() {
		System.out.println("Cat run");
	}
}

class Dog extends Animal {
	@Override
	void run() {
		System.out.println("Dog run");
	}
}
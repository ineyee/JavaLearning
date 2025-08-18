package _05_内部类;

import _05_内部类.Person03.Cat;

public class _03_非静态内部类 {

	public static void main(String[] args) {
		Person03 person = new Person03();

		Cat cat = person.new Cat();
		cat.run();
	}

}

/*
 * 一、我们再来专门说一下非静态内部类
 * 
 * 1、没有用static修饰的内部类就是非静态内部类
 * 
 * 2、一个普通的类内部可以定义实例成员和类成员，
 * 而一个非静态内部类内部则只能定义实例成员，不能定义任何类成员，也就是说不能定义任何用static修饰的东西
 * 
 * 3、非静态内部类跟实例属性、实例方法一样都是类的实例成员，所以它们都得用外部类的实例才能访问，
 * 也就是说如果你想使用非静态内部类，那么你就必须得先创建一个外部类的实例，然后再用这个外部类的实例去访问非静态内部类来使用
 * 
 * 所以我们可以把非静态内部类看做是少了某些自由（只能用外部类的实例才能访问 + 不能定义类成员）、但是换来了某些特殊权
 * 限（能直接访问外部类的成员（实例成员和静态成员都可以），即便外部类的成员被定义成private（我们知道这是因为
 * 非静态内部类的实例内存中会有个指针指向外部类的实例））的一个类
 */
class Person03 {
	// 实例属性
	private String name = "张三";
	// 实例方法
	private void eat() {
		System.out.println("Person eat() " + name);
	}
	
	// 非静态内部类
	class Cat {	
		// 实例属性
		String name = "Tom";
		// 实例方法
	 	void run() {
	 		System.out.println("Cat run() " + name); // Cat run() Tom
	 		
			// 非静态内部类内部可以直接访问外部类的实例成员，即便定义为私有的
	 		// 如果重名了，那就用外部类.this.xxx来访问
			System.out.println(Person03.this.name); // 张三
			eat(); // Person eat() 张三
		}
	}
}

/*
 * 二、内部类和它外部类的关系：
 * 
 * 1、我们定义了这么一个Person类，那将来我们拿这个Person类创建一个person对象的话，person对象在堆区的内存里当然是只有name这个属性的值张三，
 * eat方法是存储在代码区的，Cat这个内部类当然也是存储在代码区的，就像Person这个类本身是存储在代码区的一样，Cat类不可能存储在person对象里，
 * 类的结构本身在程序里只要一份就够了嘛
 * 
 * 2、但是如果我们拿Cat类创建了一个cat对象，那cat对象在堆区的内存里肯定有name这个属性的值Tom，
 * 此外编译器还会默认给这个cat对象内存中搞一个指针，指向使用这个内部类Cat的那个person对象，编译器为什
 * 么要这么做呢？我们之前已经说过了要想使用内部类就必须得先有一个外部类的实例，所以编译器这么做就是为了
 * 让cat对象持有person对象，这样在cat对象销毁之前person对象就不可能被销毁了，只有person对象不销毁，
 * Cat类才能被使用嘛！
 */


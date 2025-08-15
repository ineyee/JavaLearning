package _01_类_对象_成员变量_成员函数;

public class _07_构造方法 {

	public static void main(String[] args) {
		Person0701 p1 = new Person0701();
		System.out.println(p1.age + " " + p1.height); // 0 0.0
		
		Person0702 p2 = new Person0702(10, 1.88);
		System.out.println(p2.age + " " + p2.height); // 10 1.88
	}

}

/*
 * 对象创建的时候会自动调用它的构造函数，一般用来完成对象的初始化工作——如为成员变量赋初值等。Java 里构造函数的特点是：
 * 	函数名与类名相同
 * 	可以有参数也可以没有参数
 *  无返回值（void也不能写）
 *  支持重载——即可以有多个构造函数
 */

/*
 * 1、当我们不自定义构造方法的时候，编译器会默认给这个类生成一个无参的构造方法，即：Person0701() { super(); }
 * 注意：默认生成的这个无参构造方法并不负责成员变量的初始化，它的唯一用途就是用来调用一下父类的无参构造方法（super()）
 * 成员变量的初始化则是由 JVM 在对象分配内存时自动把默认值直接写到内存里的，与默认构造方法无关
 * 
 * 关于是否需要自定义构造函数：
 * 	我感觉Java和OC、Swift比价像，那就是不要去自定义，建议用默认提供的无参构造方法就好了，创建出对象来再一个一个给属性赋值就行了
 * 	因为外界在使用时大多数情况都是直接 new Person()，至于 person 的属性是什么值，稍后再说，没人愿意在调用构造方法的时候就得传一堆东西
 */
class Person0701 {
	int age;
	double height;
}

/*
 * 2、当我们自定义了构造方法时，那编译器就不会帮这个类自动生成无参的构造方法了，所以此时如果我们还想使用无参构造方法就得自己写一个了
 */
class Person0702 {
	public int age;
	public double height;
	
	// 全能构造方法，即参数最全的那个构造方法，其它构造方法都可以调用全能构造方法
	Person0702(int age, double height) {
		this.age = age;
		this.height = height;
	}
	
	// 部分参数构造方法
	Person0702(int age) {
		// 在一个构造方法里调用另一个构造方法时，必须把另一个构造方法的调用放在第一条语句来执行，然后才能执行其它语句，否则报错
		// 这也很好理解，肯定是得等对象构造完了，才能去做别的事情
		this(age, 0);
	}
	
	// 部分参数构造方法
	Person0702(double height) {
		this(0, height);
	}
	
	// 无参构造方法
	Person0702() {
		this(0, 0);
	}
}

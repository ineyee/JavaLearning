package _01_类_对象_成员变量_成员函数;

public class _09_访问权限 {

	public static void main(String[] args) {
		/*
		 * Java里一共有4个级别的访问权限，从高到低依次是：
		 * 
		 * 1、public：在项目里的任何地方都能访问
		 * 2、protected：在当前包里 + 包内子类或包外子类里能访问（注意：子类也在当前包里那当然能访问，子类就算不在当前包里也能访问）
		 * 3、package-private（默认）：在当前包里 + 包内子类里能访问（注意：子类在当前包里才能访问，子类不在当前包里不能访问）
		 * 4、private：仅在当前类里能访问，子类里也不能访问
		 */
		
		/*
		 * 1、上面4个修饰符都可以修饰类的成员：属性、方法、内部类等
		 * 2、但是只有public和package-private才能修饰顶级类————也就是我们正常定义的类，而不是内部类
		 */
	}

}

/*
 * 一个文件里面可以定义多个顶级类
 * 但是前面我们说过用public修饰的类必须跟文件名同名，所以一个文件里的其它类就只能用package-private了（默认就是），也就是只能在自己的包内访问
 */
class Person09 {

}

class Student09 extends Person09 {

}

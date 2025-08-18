package _05_内部类;

/*
 * 本篇介绍3个概念：
 * 1、顶级类（Top-level Class）
 * 2、外部类（Outer Class）
 * 3、内部类（Nested Class）
 * 		──静态内部类（Static Nested Class）
 * 		——非静态内部类（Inner Class)
 */

/*
 * 1、什么是顶级类？
 * 最外层的类就是顶级类，例如下面的Person、Teacher、Student就是顶级类
 */

/*
 * 2、什么是外部类？
 * 内部类外层的类就是外部类，例如下面的Person相对于Cat、Dog来说就是外部类，Cat相对于Head、Body来说就是外部类
 */

/*
 * 3、什么是内部类？
 * 定义在其它类内部的类就是内部类，内部类和属性、方法一样都是类的成员
 * 例如下面的Cat、Dog相对于Person来说就是内部类，Head、Body相对于Cat来说就是内部类
 * 
 * 内部类又可以分为两种：
 * 		——静态内部类：用static关键字修饰的内部类就是静态内部类，例如下面的Cat、Head就是静态内部类
 * 		——非静态内部类：没用static关键字修饰的内部类就是非静态内部类，例如下面的Dog、Body就是非静态内部类
 */

/*
 * 至此我们就感受到了，内部类和外部类是一个相对的概念，例如下面的Cat就既是内部类也是外部类，只有顶级类才是个绝对的概念
 */

class Person {
	static class Cat {
		static class Head {
			
		}
		
		class Body {
			
		}
	}
	
	class Dog {		
		
	}
}

class Teacher {
	
}

class Student {
	
}

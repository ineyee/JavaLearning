package _27匿名类;

/*
 * 一、匿名类是什么？
 * 顾名思义，匿名类指的就是没有名字的类，它跟普通的类没什么区别，只是没有名字 + 不能定义类成员而已。
 * 
 * 匿名类的格式为：
 * {
 * 	// 在这个大括号里定义匿名类的属性和方法
 * }
 * 
 * 对照普通类的格式：
 * class NormalClass {
 * 	// 在这个大括号里定义普通类的属性和方法
 * }
 * 
 * 可见匿名类和普通类的区别就在于普通类是用“class 类名 {}”来定义一个类的，
 * 而匿名类则没有名字、直接是用“{}”来定义一个类的，也正是因为匿名类没有名字，
 * 所以我们在定义匿名类的同时就必须实例化掉它——即用匿名类创建掉对象，否则以后没名字你怎么使用它呢。
 * 
 * 我们可以认为“匿名类就是抽象类的子类或接口的实现类，只是没有名字、不需要显式定义了而已”，它就像我们定义出来 OnlyOnceClass1 和 OnlyOnceClass2 一样可以实例化
 * 我们就感受到了，“匿名类是依附于抽象类或接口的，如果没有抽象类和接口，匿名类是无法定义和创建对象的，也就是说要想用匿名类必须得现有抽象类或接口”
 * 匿名类定义和创建对象的格式为（你看的确得先有抽象类或接口，匿名类才能定义和创建对象吧）：
 * new 抽象类名()或接口名() {
 * 	// 在这个大括号里定义匿名类的属性和方法
 * }
 * 
 * 当然如果你想用一个变量持有匿名类实例化出来的对象（注意：变量持有的是用匿名类创建出来的对象，而非匿名类本身），写法为：
 * 抽象类名或接口名 对象名 = new 抽象类名()或接口名() {
 * 	// 在这个大括号里定义匿名类的属性和方法
 * }
 * 
 * 或者你想把匿名类创建出来的对象作为函数的参数传递（注意：传递的是用匿名类创建出来的对象，而非匿名类本身），写法为：
 * void func(抽象类名或接口名 形参名) {}
 * func(new 抽象类名()或接口名() {
 *	 // 在这个大括号里定义匿名类的属性和方法
 *	}
 * );
 * 
 * 以上三种写法在实际开发中都是非常常见的
 */

// 定义一个普通类
class NormalClass {
	void run() {
		System.out.println("NormalClass run()");
	}
}

// 定义一个抽象类（抽象类本身不能实例化）
abstract class Person {
	abstract void run();
}
// 这个类继承自 AbstractClass 抽象类、可以实例化，但只在一个地方使用
class OnlyOnceClass1 extends Person {
	@Override
	public void run() {
		System.out.println("OnlyOnceClass1 run()");
	}	
}

// 定义一个接口（接口本身不能实例化）
interface Runnable {
	void run();
}
// 这个类实现了 Runnable 接口、可以实例化，但只在一个地方使用
class OnlyOnceClass2 implements Runnable {
	@Override
	public void run() {
		System.out.println("OnlyOnceClass2 run()");
	}	
}


public class Main {
	
	public static void main(String[] args) {
		// 普通类创建对象
		NormalClass normalObj = new NormalClass();
		normalObj.run(); // NormalClass run()

		// 抽象类的子类创建对象
		Person onlyOnceObj1 = new OnlyOnceClass1();
		onlyOnceObj1.run(); // OnlyOnceClass1 run()
		
		// 接口的实现类创建对象
		Runnable onlyOnceObj2 = new OnlyOnceClass2();
		onlyOnceObj2.run(); // OnlyOnceClass2 run()
		
		
		// 像上面那样，定义出来抽象类的子类（比如 OnlyOnceClass1）或接口的实现类（比如 OnlyOnceClass2），然后用子类或实现类去创建对象，这种写法是非常标准的，没有任何问题
		// 但是如果抽象类的子类（比如 OnlyOnceClass1）或接口的实现类（比如 OnlyOnceClass2）只在极少数的地方创建了对象，那我们就大可不必定义出来抽象类的子类或接口的实现类、再去创建对象
		// 而是可以直接使用匿名类来创建对象，这样就省去了创建一大堆类文件，项目的目录结构也更加清晰
		// 
		// 换句话说，我们可以认为“匿名类就是抽象类的子类或接口的实现类，只是没有名字、不需要显式定义了而已”，它就像我们定义出来 OnlyOnceClass1 和 OnlyOnceClass2 一样可以实例化
		// 至此我们就感受到了，“匿名类是依附于抽象类或接口的，如果没有抽象类和接口，匿名类是无法定义和创建对象的，也就是说要想用匿名类必须得现有抽象类或接口”
		
		
		/*
		 * 这里我们可以认为匿名类
		 * {
		 * 		@Override
		 * 		void run() {
		 * 			System.out.println("抽象类的匿名类 runrunrun");
		 * 		}
		 * }
		 * 就是抽象类 Person 的子类，我们并没有显式定义 Person 的子类，而是直接用抽象类的匿名类创建了对象
		 */
		new Person() {
			@Override
			void run() {
				System.out.println("抽象类的匿名类 runrunrun");
			}
		}.run(); // 抽象类的匿名类 runrunrun
		
		/*
		 * 这里我们可以认为匿名类
		 * {
		 * 		@Override
		 * 		void run() {
		 * 			System.out.println("接口的匿名类 runrunrun");
		 * 		}
		 * }
		 * 就是接口 Runnable 的实现类，我们并没有显式定义 Runnable 的实现类，而是直接用接口的匿名类创建了对象
		 */
		Runnable onlyOnceObj4 = new Runnable() {
			@Override
			public void run() {
				System.out.println("接口的匿名类 runrunrun");
			}
		};
		onlyOnceObj4.run(); // 接口的匿名类 runrunrun
	
		/*
		 * 这里我们可以认为匿名类
		 * {
		 * 		@Override
		 * 		void run() {
		 * 			System.out.println("抽象类的匿名类 runrunrun");
		 * 		}
		 * }
		 * 就是抽象类 Person 的子类，我们并没有显式定义 Person 的子类，而是直接用抽象类的匿名类创建了对象，然后把这个对象传递给了函数的形参
		 */
		func(new Person() {
			@Override
			void run() {
				System.out.println("抽象类的匿名类 runrunrun");
			}
		});
	}
	
	static void func(Person person) {
		person.run(); // 抽象类的匿名类 runrunrun
	}
	
}


/*
 * 二、匿名类的使用场景，以及我们为什么要使用匿名类？
 * 1、当抽象类的子类或接口的实现类，在整个项目中只在极少数的地方创建了对象，建议使用匿名类
 * 
 * 因为这种情况下使用匿名类可以减少项目里类的数量以及项目目录的复杂结构，毕竟整个项目中不会再有
 * 别的地方使用这个类了，也就是说这个类不存在多个地方复用的情况，那索性就不专门创建这么个类了
 * 
 * 例子：见上面
 * 
 * 2、当我们需要回调函数时，必须使用匿名类 + 方法（方法依附于匿名类，匿名类又依附于抽象类或接口）
 * 
 * 在其它语言里，当我们需要回调函数时，我们可能会使用匿名函数或 block 这种东西。但是在 Java 里我们
 * 必须使用匿名类 + 方法，因为前面我们已经说过了“Java 的面向对象做得很彻底，必须得现有类后有方法”，
 * 匿名函数或 block 这种东西对应到 Java 里其实就是方法这一层，所以我们还得在方法外面套一个类，并且
 * 在这种使用场景下这个类没有别的实际用途，所以定义成匿名类就可以了
 * 
 * 其它语言比如Swift和Dart里函数都是一等公民，所以我们完全可以通过匿名函数的方式来实现代码传递，就算
 * 是OC里虽然函数不是一等公民，但也提供了Block这种对象来帮很简单地实现代码传递，而Java里函数不是一等
 * 公民，也没有提供Block那种数据类型，所以就转而让我们使用匿名类这种技术来实现代码传递，所有语言里类肯
 * 定是一等公民喽，这种做法当然感觉写起来不如直接传递函数那么方便，但也正因为传递的是一个类，而一个类里
 * 又能定义很多方法，所以好处就是多个相关联的方法可以通过传递一个类来搞定，而不用搞得一个方法的参数列表
 * 那么多
 * 
 * 例子：见example2
 */

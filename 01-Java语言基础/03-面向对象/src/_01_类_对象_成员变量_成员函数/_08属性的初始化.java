package _01_类_对象_成员变量_成员函数;

public class _08属性的初始化 {

	public static void main(String[] args) {
		Person08 mother = new Person08("女");
		mother.age = 31;
		mother.height = 1.58;
		
		Person08 p = new Person08("男");
		p.mother = mother;
		System.out.println(p);
		System.out.println(p.mother);
	}

}

/*
 * 属性在初始化之前，它里面存储的值是以前使用过的垃圾值，因此属性在使用之前必须初始化，避免因为垃圾值带来未知的错误
 * 尽管有些情况下编译器会自动帮我们给某些属性初始化一个默认值，但并非所有的情况都如此，所以还是建议手动初始化
 * 
 * 属性初始化通常有四种做法，无论是这四种里的哪种初始化做法其实都可以，只要能达到目的——保证每个成员变量在使用之前被初始化了就行：
 * 1、声明属性的时候能初始化的就直接初始化掉
 * 2、在构造方法里初始化属性（这种情况适合不可变且依赖外界的属性）
 * 比如用 final 修饰的变量因为不可变，所以必须在声明时初始化或者构造方法里初始化，但是这个值又不能一次性在声明时写死，而是会依赖外界传入啥就是啥，所以就最好在构造方法里初始化
 * 至于构造函数里给不给属性默认值其实是可选参数和必选参数的事情，不是属性初始化的事情，有的语言支持可选参数，有的语言不支持可选参数，但都支持构造方法初始化属性
 * 3、声明时不能初始化或不方便初始化的就 late 延迟初始化，但是一定要确保在使用成员变量之前它已经被初始化掉了
 * 声明时不能初始化是指有些属性的初始化必须依赖其它属性，而所有编程语言都会要求属性在初始化时不能依赖 this（因为当前对象未完全构造完成）
 * 声明时不方便初始化是指有些属性的初始化是一坨代码，放在声明时初始化看着会很恶心，就像 Swift 里在声明时就初始化掉一个 Button 那样
 * 4、如果明确希望属性为null，那就定义为可选项，这样成员变量默认就会被初始化为null了
 */

class Person08 {
	// 1、适合直接初始化
	// 因为所有的人出生后，年龄都为0、身高都大约为0.6米
	Integer age = 0;
	Double height = 0.6;
	
	// 2、不太适合直接初始化（虽然这里会默认初始化为 null，但是在空安全的语言里，这就是一个未初始化的属性，所以我们可以沿用空安全语言的思想，反正我们在使用的时候肯定会判空嘛）
	// 性别不可变，所以定义为 final，而且这个人出生时才能决定性别，所以满足不可变 + 依赖外界，因此放到构造方法里初始化
 	final String sex;
 	
 	// 3、这个就属于声明时不方便初始化，因为初始化的话会有一坨代码
 	Person08 mother;
 	
 	// 4、这个就属于明确希望属性为 null，因为 null 是有实际意义的，那就代表这个人还没有车
 	Car car;
 	
 	Person08(String sex) {
 		if (sex == null) {
			throw new IllegalArgumentException("性别为 null");
		}
 		this.sex = sex;
	}
 	
 	@Override
 	public String toString() {
 		return age + " " + height + " " + sex + " " + car;
 	}
}

class Car {
	double price = 0;
	String brand = "";
	
 	@Override
 	public String toString() {
 		return price + " " + brand;
 	}
}

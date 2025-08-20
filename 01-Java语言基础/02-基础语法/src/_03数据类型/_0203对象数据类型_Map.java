package _03数据类型;

import java.util.HashMap;
import java.util.Map;

public class _0203对象数据类型_Map {

	public static void main(String[] args) {
		test();
		test1();
		test2();
	}
	
	/*
	 * 1、Map的声明及初始化
	 * 
	 * HashMap类似于OC里的NSMutableDictionary
	 * HashMap不是线程安全的，对应的线程安全类为Hashtable
	 * 
	 * 等号的左边我们一般不会直接用HashMap，而是用Map。Map是个接口，HashMap等很多类都实现了这个接口
	 * 所以左边如果写成Map，将来如果我们右边想换一个Map，就直接换下右边的东西就可以了，左边不用动，这就是面向接口编程
	 * 
	 * 并且等号右边的泛型也可以省略，但是<>一定不能省略
	 */
	public static void test() {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map1 = new HashMap<>(Map.of(
			"name", "张三", // 注意这里 key-value 之间是逗号不是冒号
			"age", 18,
			"height", 1.88
		));
		System.err.println(map); // {}
		System.err.println(map1); // {name=张三, age=18, height=1.88}
	}
	
	/*
	 * 2、Map的基本操作
	 */
	public static void test1() {
		Map<String, Object> map = new HashMap<>();
		
		// 增
		map.put("name", "张三");
		map.put("age", 18);
		map.put("height", 1.88);
		System.err.println(map); // {name=张三, age=18, height=1.88}
		
		// 删
		map.remove("height");
		System.err.println(map); // {name=张三, age=18}
		
		// 改
		map.put("age", 19);
		System.err.println(map); // {name=张三, age=19}
		
		// 查
		Integer age = (Integer)map.get("age");
		System.out.println(age); // 19
	}
	
	/*
	 * 3、字典的遍历
	 */
	public static void test2() {
		Map<String, Object> map = new HashMap<>(Map.of(
			"name", "张三", // 注意这里 key-value 之间是逗号不是冒号
			"age", 18,
			"height", 1.88
		));
		map.forEach((key, value) -> {
			System.out.println(key + ": " + value);
		});
	}

}

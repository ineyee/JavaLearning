package _07Lambda表达式;

/*
 * 一、函数式接口
 * 在学习Lambda表达式之前，我们先学一个概念：函数式接口
 * 
 * 所谓函数式接口就是指：里面只有一个抽象方法的接口，default方法的数量不做要求
 * 我们可以在接口的上方加上 @FunctionalInterface 注解来表明该接口是一个函数式接口
 */
class NetworkUtil1 {
	// 回调函数要素一：接口（这个接口不是函数式接口，因为这个接口只在这个类内部使用，所以直接定义在类内部了，就像内部类一样）
	public interface Result {
		// 回调函数要素二：方法
		void successCallback(Object data);
		void failureCallback(String errorMsg);
		
		default void test1() {}
		default void test2() {}
	}
	
	public static void get(String url, Result result) {
		// 假设发起了请求...
		
		// 假设请求成功了
		result.successCallback("我是数据");
		
		// 假设请求失败了
		result.failureCallback("参数错误");
	}
}

class NetworkUtil2 {
	// 回调函数要素一：接口（这个接口是函数式接口，因为这个接口只在这个类内部使用，所以直接定义在类内部了，就像内部类一样）
	@FunctionalInterface
	public interface Result {
		// 回调函数要素二：方法
		void callback(Object data, String errorMsg);
		
		default void test1() {}
		default void test2() {}
	}
	
	public static void get(String url, Result result) {
		// 假设发起了请求...
		
		// 假设请求成功了
		result.callback("我是数据", null);
		
		// 假设请求失败了
		result.callback(null, "参数错误");
	}
}

/*
 * 二、Lambda表达式是什么？
 * 
 * Lambda表达式的格式为，用法很像其它语言里的箭头函数：
 * (参数列表) -> {
 * 	// 方法的执行体
 * }
 * 
 * Lambda表达式其实是”某种匿名类“的语法糖，只是为了简化”某种匿名类“的写法而已、写起来更加简洁
 * 我们知道匿名类可以依托于抽象类或接口，所以就有抽象类型匿名类和接口型匿名类之分，而接口又可以分为非函数式接口和函数式接口
 * 所以接口型匿名类又可以分为非函数式接口型匿名类和函数式接口型匿名类，而这里的”某种匿名类“指的就是函数式接口型匿名类
 * 而Lambda表达式的这个函数对应的就是函数式接口里唯一的那个抽象函数
 */

/*
 * 三、Lambda表达式的使用场景，为什么要使用Lambda表达式？
 * 
 * 当匿名类实现的是函数式接口时，我们就可以用Lambda表达式简化代码的写法，并且也只有这种情况下才能使用Lambda表达式
 * 可以省略 new 接口名() { 抽象方法的实现 } 这么一堆代码了，代码看起来更简洁
 */

public class Main {

	public static void main(String[] args) {
		test();
	}
	
	public static void test() {
		// 传统写法
		// 不是函数式接口型匿名类，所以不可以用Lambda表达式简化
		NetworkUtil1.get("http://xxx", new NetworkUtil1.Result() {
			@Override
			public void successCallback(Object data) {
				System.out.println("NetworkUtil1 请求成功了: " + data);
			}
			
			@Override
			public void failureCallback(String errorMsg) {
				System.out.println("NetworkUtil1 请求失败了: " + errorMsg);
			}
		});
		
		// 是函数式接口型匿名类，所以可以用Lambda表达式简化
		NetworkUtil2.get("http://xxx", new NetworkUtil2.Result() {
			@Override
			public void callback(Object data, String errorMsg) {
				if (errorMsg == null) {
					System.out.println("NetworkUtil2 请求成功了: " + data);
				} else {
					System.out.println("NetworkUtil2 请求失败了: " + errorMsg);
				}	
			}
		});
		
		/*
		 * Lambda表达式简化写法，看起来的确清爽了不少
		 */
		NetworkUtil2.get("http://xxx", (data, errorMsg) -> {
			if (errorMsg == null) {
				System.out.println("NetworkUtil2 又请求成功了: " + data);
			} else {
				System.out.println("NetworkUtil2 又请求失败了: " + errorMsg);
			}
		});
	}

}

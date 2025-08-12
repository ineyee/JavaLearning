package _03数据类型;

import java.util.HashSet;
import java.util.Set;

public class _0204对象数据类型_Set {

	public static void main(String[] args) {
		/*
		 * Set的特点：
		 * 无序，也就是说它里面存储的元素跟我们添加元素的顺序是不同的，所以我们不能通过下标来访问元素
		 * 元素不可重复，所以我们经常用Set来做数组的去重
		 * 
		 * 等号的左边我们一般不会直接用HashSet，而是用Set。Set是个接口，HashSet等很多类都实现了这个接口
		 * 所以左边如果写成Set，将来如果我们右边想换一个Set，就直接换下右边的东西就可以了，左边不用动，这就是面向接口编程
		 * 
		 * 并且等号右边的泛型也可以省略，但是<>一定不能省略
		 */
		Set<Integer> set = new HashSet<>();
		set.add(11);
		set.add(22);
		set.add(33);
		System.out.println(set); // [33, 22, 11]，而非[11, 22, 33]
	}

}

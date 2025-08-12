package _03数据类型;

import java.util.Stack;

public class _0205对象数据类型_Stack {

	public static void main(String[] args) {
		/*
		 * 栈|_|：先进后出
		 */
		Stack<Integer> stack = new Stack<>();
		
		// push入栈
		stack.push(11);
		stack.push(22);
		stack.push(33);
		System.out.println(stack); // [11, 22, 33]
		
		// pop出栈栈顶元素
		stack.pop();
		System.out.println(stack); // [11, 22]
		
		// peek拿到栈顶元素
		Integer num = stack.peek();
		System.out.println(num); // 22
		
		// size返回栈的大小
		System.out.println(stack.size()); // 2
		
		// isEmpty判断栈是不是空的
		System.out.println(stack.isEmpty()); // false
	}

}

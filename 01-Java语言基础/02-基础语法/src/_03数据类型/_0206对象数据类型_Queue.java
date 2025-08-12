package _03数据类型;

import java.util.LinkedList;
import java.util.Queue;

public class _0206对象数据类型_Queue {

	public static void main(String[] args) {
		/*
		 * 队列| |：先进先出，只允许尾部进头部出，不允许从中间增加和删除元素
		 * 
		 * LinkedList除了是个双向链表之外，也是一个队列，也就是说在Java里LinkedList就是队列
		 */
		Queue<Integer> queue = new LinkedList<>();
		
		// offer或add入队
		queue.offer(11);
		queue.offer(22);
		queue.add(33);
		System.out.println(queue); // [11, 22, 33]
		
		// poll或remove出队
		queue.poll();
		queue.remove();
		System.out.println(queue); // [33]
		
		// peek拿到队头元素
		Integer num = queue.peek();
		System.out.println(num); // 33
				
		// size返回队列的大小
		System.out.println(queue.size()); // 1
		
		// isEmpty判断队列是不是空的
		System.out.println(queue.isEmpty()); // false
	}

}

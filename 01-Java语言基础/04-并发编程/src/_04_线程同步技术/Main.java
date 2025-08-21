package _04_线程同步技术;

import java.util.concurrent.locks.ReentrantLock;

/*
 * 线程同步技术是专门用来处理线程安全问题的，线程同步技术最终的效果就是使得多条线程串行地访问同一个资源，都串行了所以就不存在数据竞争了
 * 
 * 当然因为线程同步技术会使得多条线程串行执行，所以会在一定程度上降低代码的执行效率，所以我们一定是在有线程安全问题的时候才使用线程同步
 * 技术，而不是说只要使用了多线程就得使用线程同步技术。比如多个线程同一时间访问同一资源，但是大家都只是进行读操作，那就不存在数据错乱问题，
 * 所以也就没必要使用线程同步技术，不使用线程同步技术时代码的执行效率会更高
 * 
 * 线程同步技术的方案之一就是加锁，加锁在 Java 里有三种具体实现：
 * 	同步语句（Synchronized Statement）
 *	同步方法（Synchronized Method）
 *  可重入锁（ReentranrLock，其实就是递归锁）
 */
public class Main {
	// 创建锁
	private static ReentrantLock lock = new ReentrantLock();
	
	// 总票数
	private static int totalTicketCount = 10; 
	
	public static void main(String[] args) {
		// 假设有10个窗口在卖票 --> 即多个线程
	    for (int i = 0; i < 10; i++) {
	    	Thread thread = new Thread(() -> {
	    		saleTicket();
			});
	    	thread.start();
	    }
	}
	
	/*
	 * 线程同步方案一：同步语句，类似于OC里的@synchronized，小括号里可以是任意一个Java对象
	 * 
	 * 同步语句的原理：每一个Java对象都有一个与之相关的内部锁或者叫监视器锁，那么当第一条线程
	 * 执行到synchronized的时候就会获取到这个Java对象的内部锁，然后把代码锁住，此时其它线程就无法
	 * 获取这个Java对象的内部锁了，除非代码执行完锁解开，然后阻塞的那些线程就会看运气谁先拿到锁谁就
	 * 先被执行
	 */
	// 任意创建一个Java对象作为锁对象，当然如果saleTicket是个实例方法的话，小括号里直接放this就行了
	// 但是注意小括号里不能直接写“new Object()”这种代码，因为这意味着每个线程进来都会创建一个新的Java对象，多个线程根本不是共用同一个Java对象的内部锁，所以根本锁不住
//	private static Object lock = new Object();
//	private static void saleTicket() {
//		synchronized (lock) {
//			// 卖一张票
//			if (totalTicketCount <= 0) {
//				System.out.println("没票了"  + " " + Thread.currentThread());
//			} else {
//				totalTicketCount--;
//				System.out.println("剩余票数：" + totalTicketCount + " " + Thread.currentThread());
//			}
//		}
//	}
	
	/*
	 * 线程同步方案二：同步方法，直接在方法的返回值前面加一个synchronized关键字就可以了
	 * 
	 * 同步方法的本质：
	 * 1、如果方法是个实例方法，那么同步方法完全等价于同步语句那种写法小括号里写了个(this)
	 * 2、如果方法是个静态方法，那么同步方法完全等价于同步语句那种写法小括号里写了个(当前类的类对象，例如Main.class，一个类的类对象只有一份)
	 *
	 * 同步方法明显没有同步语句灵活，因为同步方法会把整个方法里的代码都锁住，而同步语句则可以更精准地锁住我们需要被锁住的代码
	 */
//	private static synchronized void saleTicket() {
//		// 卖一张票
//		if (totalTicketCount <= 0) {
//			System.out.println("没票了"  + " " + Thread.currentThread());
//		} else {
//			totalTicketCount--;
//			System.out.println("剩余票数：" + totalTicketCount + " " + Thread.currentThread());
//		}
//	}
	
	/*
	 * 线程同步方案三：可重入锁
	 */
	private static synchronized void saleTicket() {
		try {
			// 加锁
			lock.lock();
			
			// 卖一张票
			if (totalTicketCount <= 0) {
				System.out.println("没票了"  + " " + Thread.currentThread());
			} else {
				totalTicketCount--;
				System.out.println("剩余票数：" + totalTicketCount + " " + Thread.currentThread());
			}
		} finally {
			// 解锁
			lock.unlock();
		}
	}
	
	/*
	 数据正常：
	 
	 剩余票数：9 Thread[Thread-0,5,main]
	 剩余票数：8 Thread[Thread-2,5,main]
	 剩余票数：7 Thread[Thread-9,5,main]
	 剩余票数：6 Thread[Thread-8,5,main]
	 剩余票数：5 Thread[Thread-7,5,main]
	 剩余票数：4 Thread[Thread-6,5,main]
	 剩余票数：3 Thread[Thread-3,5,main]
	 剩余票数：2 Thread[Thread-5,5,main]
	 剩余票数：1 Thread[Thread-4,5,main]
	 剩余票数：0 Thread[Thread-1,5,main]
	*/
	
}

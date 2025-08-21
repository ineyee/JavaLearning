package _03_线程安全问题;

/*
 * 使用多线程就必然会存在线程安全问题，线程安全问题是指：
 * 
 * 多个线程同一时间访问同一资源（如变量、对象、文件等），且至少有一个线程是在进行写操作，那就很容易引发数据错乱问题
 */
public class Main {
	
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

	private static void saleTicket() {
		// 卖一张票
		if (totalTicketCount <= 0) {
			System.out.println("没票了"  + " " + Thread.currentThread());
		} else {
			totalTicketCount--;
			System.out.println("剩余票数：" + totalTicketCount + " " + Thread.currentThread());
		}
	}
	
	/*
	 数据错乱：
	 
	 剩余票数：7 Thread[Thread-0,5,main]
	 剩余票数：0 Thread[Thread-1,5,main]
 	 剩余票数：4 Thread[Thread-6,5,main]
	 剩余票数：0 Thread[Thread-2,5,main]
	 剩余票数：2 Thread[Thread-8,5,main]
	 剩余票数：3 Thread[Thread-7,5,main]
	 剩余票数：0 Thread[Thread-3,5,main]
	 剩余票数：1 Thread[Thread-9,5,main]
	 剩余票数：6 Thread[Thread-4,5,main]
	 剩余票数：5 Thread[Thread-5,5,main]
	*/
	
}

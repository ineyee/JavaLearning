package _02_基本使用;

public class Main {
	
	public static void main(String[] args) {
		test1();
		test2();
		
		test3();
		test4();
		test5();
	}
	
	// 1.1 【创建子线程和启动子线程】方式一
	public static void test1() {
		// 创建新线程（Runnable 是个函数式接口，所以这里也可以用 Lambda 表达式简化）
		Thread thread = new Thread(new Runnable() {
			// 新线程里想做的事
			@Override
			public void run() {
				System.out.println(Thread.currentThread());
			}
		});
		thread.setName("新线程666");
		thread.setPriority(3);
		// 启动新线程，会自动分配新线程所需要的资源，并自动调用run方法
		thread.start(); // Thread[新线程666,3,main]
	}
	
	// 1.2 【创建子线程和启动子线程】方式二
	public static void test2() {
		// 创建新线程
		Thread thread = new MyThread();
		thread.setName("新线程777");
		thread.setPriority(5);
		// 启动新线程，会自动分配新线程所需要的资源，并自动调用run方法
		thread.start(); // Thread[新线程777,5,main]
	}
	
	/*
	 * 2.1 线程的6种状态：
	 * 
	 * NEW: 新建的线程，尚未启动
	 * 
	 * RUNNABLE: 可运行的线程或正在运行的线程，之所以说是“可运行的线程”，是因为如果是单核CPU，那么这个线程就很
	 * 有可能是在等待别的线程执行完毕，也就是说这条线程就是处于可运行但未运行的状态，但实际上如果是多核CPU，那么这个
	 * 状态就是指“正在运行的线程”
	 * 
	 * BLOCKED：正在阻塞的线程，正在等待监视器锁（内部锁）
	 * 
	 * WAITING：正在等待另一条线程
     * 
     * TIMED_WAITING：定时等待状态
     * 
     * TERMINATED：已经执行完毕的线程，终止状态
	 */
	public static void test3() {
		Thread mainThread = Thread.currentThread();
		System.err.println(mainThread.getState()); // RUNNABLE
		
		Thread thread = new Thread();
		System.err.println(thread.getState()); // NEW
	}
	
	/*
	 * 2.2 线程的2个方法
	 * 
	 * Thread.sleep(ms)：调用 Thread.sleep 方法可以暂停当前线程，当前线程会进入 WAITING 状态
	 * 		如果线程 sleep 期间，不小心被打断了，就会抛出 Interrupted 异常，所以我们得 try-catch 一下
	 * 
	 * Thread.join(ms)：调用 thread.join 方法可以卡主当前线程，等待 thread 执行 ms 时间，ms 不传的话就是等待 thread 线程执行完毕，然后再执行当前线程
	 * 		如果线程 join 期间，不小心被打断了，就会抛出 Interrupted 异常，所以我们得 try-catch 一下
	 */
	public static void test4() {
		System.out.println("1"); // 先立即打印 1
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("2"); // 5s 后才打印 2
	}
	
	public static void test5() {
		Thread thread = new Thread(() -> {
			System.out.println("3");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		thread.start(); // 启动子线程后，先立即打印 3
				
		try {
			thread.join(5000); // 这句代码本身是在主线程里执行的，所以是卡主当前线程————主线程 5s，等待子线程执行
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("4"); // 5s 后才打印 4
	}
	
}

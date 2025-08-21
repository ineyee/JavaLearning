package _06_线程间通信;

/*
 * 线程间通信是指：多个线程间通过互相打招呼，从而按照约定的顺序执行任务
 * 也就是说线程间通信解决的是多线程并发时任务执行顺序的问题
 * 
 * 比如：
 * 👉 线程 1 执行完一个任务，停下来等待，并告诉线程 2 “我做好了，你可以开始了”
 * 👉 线程 2 开始执行任务，执行完后停下来等待，并告诉线程 2 “现在又轮到你了”
 * 👉 如此循环...
 */

/*
 * Java 里我们使用 obj.wait()、obj.notify()、obj.notifyAll() 来实现线程间通信，这三个方法来自于 Object 类，所以说所有的 Java 对象都可以调用这三个方法
 * 	obj.wait()：调用后，当前线程会卡主，开始等待 obj.notify() 或 obj.notifyAll() 的唤醒
 *  obj.notify()：调用后，随机唤醒一个因为调用 obj.wait() 而卡主的线程
 *  obj.notifyAll()：调用后，唤醒所有因为调用 obj.wait() 而卡主的线程
 * 
 * 需要注意的是：
 * 1、既然是线程间通信，这就意味着 obj.wait() 和 obj.notify()、obj.notifyAll() 肯定是在不同的线程里调用
 * 2、调用 wait() 的 obj 对象和调用 notify()、notifyAll() 的 obj 对象必须是同一个对象，否则无法实现等待、唤醒
 * 3、要想在线程里正常调用 obj.wait()、obj.notify()、obj.notifyAll() 方法，那么该线程必须获取到 obj 对象的内部锁，否则报错
 * 4、Java 的 wait()、notify()、notifyAll() 有个机制是：如果线程 2 先执行了 notify()，线程 1 里才 wait()，那这次 notify() 信号就丢失了，后续不会再补上这次信号，这就是所谓的假唤醒
 * 所以我们一定要搞个标记位来判断线程到底应不应该进入 wait 来避免假唤醒问题
 */
public class Main {
	
	// 举个例子，我们创建两个子线程来打印[0, n)之间的整数，一个子线程用来打印偶数，另一个子线程用来打印奇数，要求先打印偶数、接下来就奇数偶数一个一个交替打印
    public static void main(String[] args) {
        int n = 10;
        
        // 定义一个对象，用它来调用 obj.wait()、obj.notify()、obj.notifyAll()
        // （注意点二已就位）
        Object lock = new Object();
        
        // 引入标记位，防止假唤醒
        // 默认是偶数线程轮次
        // 用数组包装，方便在lambda里修改
        //
        // 如果没有标记位的话，很有可能偶数线程执行很快，都执行了 lock.notify、然后开始 wait，此时才开始执行奇数线程，奇数线程进去后会直接开始 wait，但是来自偶数线程
        // 的上一次 notify 已经错过了，所以奇数线程就不可能被唤醒，导致两个线程都处于 wait 状态，这就是假唤醒
        // 
        // 而有了标记位的话，就算偶数线程执行很快，都执行了 lock.notify、然后开始 wait，此时才开始执行奇数线程，奇数线程进去后会首先判断改不改 wait，因为标记位此时是
        // 不该等，所以奇数线程也会顺利往下执行，从而避免假唤醒问题
        boolean[] evenTurn = {true};
        
        // 打印偶数线程，第一条线程（注意点一已就位）
        // 一次打印相当于一个任务
        Thread evenThread = new Thread(() -> {
        	// 同步语句的原理就是执行到synchronized的时候就会获取到这个lock对象的内部锁
        	// （注意点三已就位）
        	synchronized (lock) {
        		for (int i = 0; i < n; i += 2) {
        			// 如果不是偶数线程的轮次，就等，默认就是偶数线程轮次，所以第一次进来肯定不用等，而是直接执行后面的代码
        			// Java 官方文档要求必须把 wait() 放在 while 循环里检查条件，而不是 if，这样可以保证在唤醒后再次检查条件是否真得满足
        			// （注意点四已就位）
        			while (evenTurn[0] == false) {
        				try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
					}
        			
                    // 因为要求先打印偶数，所以这个线程一进来就应该开始执行任务————即开始打印，同时还必须得让打印奇数线程一进去就开始等待
                    // 这样无论是这个线程先执行、还是这个线程和打印奇数线程同时执行、还是这个线程比打印奇数线程后执行，都可以保证先执行这里的任务————即先打印偶数
            		System.out.println("偶数线程：" + Thread.currentThread() + " " + i);
            		
                    // 打印完偶数，通知一下打印奇数线程的wait可以往下执行了
            		// 这里结束后就会进入下一次循环，开始wait
            		evenTurn[0] = false; // 轮到奇数线程，修改标志位
            		lock.notifyAll();           		
            	}
			}
        });
        
        // 打印奇数线程，第二条线程（注意点一已就位）
        // 一次打印相当于一个任务
        Thread oddThread = new Thread(() -> {
        	// 同步语句的原理就是执行到synchronized的时候就会获取到这个lock对象的内部锁
        	// （注意点三已就位）
        	synchronized (lock) {
	        	for (int i = 1; i < n; i += 2) {
                	// 如果不是奇数线程的轮次，就等，默认是偶数线程轮次，所以第一次进来肯定会卡在这里等
                	// 因为要求先打印偶数，所以这个线程一进来就应该开始等待，而不应该开始执行任务————即开始打印，因为有可能是这个线程先执行或者这个线程和打印偶数线程同时执行，如果不等待的话就会先打印奇数了
	        		// Java 官方文档要求必须把 wait() 放在 while 循环里检查条件，而不是 if，这样可以保证在唤醒后再次检查条件是否真得满足
	        		// （注意点四已就位）
	        		while (evenTurn[0] == true) {
	                	try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
	        		
	                // 被唤醒后，会来到这里执行任务————即打印奇数
	        		System.out.println("奇数线程：" + Thread.currentThread() + " " + i);
	        		
	        		// 打印完奇数，通知一下打印偶数线程的wait可以往下执行了
	        		// 这里结束后就会进入下一次循环，开始wait
	        		evenTurn[0] = true; // 轮到偶数线程，修改标志位
	        		lock.notifyAll();
	            }
        	}
        });  
        
        evenThread.start();
        oddThread.start();
    }
    
}

package _03_主动抛出异常;

/*
 * 前面说的异常都是系统自动抛出的，我们也可以用throw主动抛出一个异常。
 */
public class Main {

	public static void main(String[] args) {
		try {
			double result =  div(20, 0);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 比方说我们定义了一个除法的函数，是num1 / num2，要求num2不能为0
	 * 
	 * 1、但如果我们仅仅是打印一下，开发者可能不会注意，外界可能还是会把num2传0进来
	 * 2、所以我们可以用throw主动抛出一个检查型异常，并且通过throws把这个异常往上抛——也就是外界调用的地方，
	 * 这样外界在使用这个方法的时候就会被抛检查型异常，那外界不处理的话编译就通不过，也就是会强制要求外界处理，所以他们就会点进来看一下，一看就知道什么情况下会抛异常，从而在外面
	 * try-catch规避掉这种情况了，这样程序就不会崩了，系统和很多三方库的API就是这么抛检查型异常的
	 */
	public static double div(double num1, double num2) throws Exception {
//		if (num2 == 0) {
//			System.out.println("num2不能为0");
//			return 0;
//		} else {
//			return num1 / num2;
//		}
		
		if (num2 == 0) {
			throw new Exception("num2不能为0");
		} else {
			return num1 / num2;
		}
	}
	
}

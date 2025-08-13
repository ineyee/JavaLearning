package _03数据类型;

/*
 * 跟很多其它语言一样，如果一个变量的取值只可能是固定的几个值，可以考虑使用枚举类型，枚举由一组预定义的常量构成，所以全大写加_
 */
enum Season {
	SPRING,
	SUMMER,
	AUTUMN,
	WINTER,
}

public class _04枚举 {

	public static void main(String[] args) {
		Season season = Season.AUTUMN;
		switch (season) {
		case SPRING:
			System.out.println("春天");
			break;
		case SUMMER:
			System.out.println("夏天");
			break;
		case AUTUMN:
			System.out.println("秋天");
			break;
		case WINTER:
			System.out.println("冬天");
			break;
		}
	}

}

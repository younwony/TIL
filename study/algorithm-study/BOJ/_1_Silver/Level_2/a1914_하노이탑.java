/**
 * @작성자 wony
 * @작성일 2019. 10. 24.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_2;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 10. 24.
 * @사용처
 * @Todo
 */

public class a1914_하노이탑 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 24.
	 * @사용처 하노이탑
	 * @param args
	 * @Todo
	 * 1. n-1 개의 탑을 middle에 옮긴다.
	 * 2. 1개의 탑을 종료지점에 옮긴다.
	 * 3. middle에 있는 n-1개의 탑을 end에 옮긴다.
	 * 중요 
	 * 4. 횟수를 출력하기 위해서는 큰수 출력 로직이 필요한데 Java내 BigInteger 함수가 있기에 해당 함수를 사용 -> BigInteger에대해 더 자세히 알아보는 공부하기!. and 큰수출력 로직 짜보기
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);

		int hanoiCount = scanner.nextInt();
		BigInteger hanoiReslutCount = new BigInteger("2");
		System.out.println(hanoiReslutCount.pow(hanoiCount).subtract(BigInteger.ONE));
		if(hanoiCount <= 20){
			hanoi(hanoiCount, 1, 2, 3);
		}
	}

	public static void hanoi(int n, int start, int middle, int end) {
		
		if(n==1) {
			System.out.println(start + " " + end);
		}else {
			hanoi(n-1, start, end, middle); // n-1 개를 middle로 옮긴다.
			System.out.println(start + " " + end); // 맨 밑의 판을 종료지점으로 옮긴다.
			hanoi(n-1, middle, start, end);// middle의 n-1개의 탑을 종료지점으로 옮긴다.
		}
		

	}

}

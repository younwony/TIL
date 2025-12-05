/**
 * @작성자 wony
 * @작성일 2019. 10. 23.
 * @사용처
 *
 */
package _1_Silver.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 10. 23.
 * @사용처
 * 
 */
public class a11729_하노이탑이동순서 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 23.
	 * @사용처 하노이 탑
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.close();
		
		System.out.println((int)Math.pow(2, n)-1); //개수
		hanoi(n, 1, 2, 3);
	}
	
	public static void hanoi(int n, int start, int middle, int end){
		if(n==1) {
			System.out.println(start + " " + end);
			return ;
		}
		hanoi(n-1, start, end, middle);// 가장 아래판을 제외한 나머지판을 중앙으로 옮기기
		System.out.println(start + " " + end); //가장 아래판을 종료지점으로 옮기기
		hanoi(n-1, middle, start, end);// 중앙의 판을 종료지점으로 옮기기
	}

}

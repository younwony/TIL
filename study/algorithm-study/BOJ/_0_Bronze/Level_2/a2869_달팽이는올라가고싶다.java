/**
 * @작성자 wony
 * @작성일 2019. 11. 28.
 * @사용처
 *
 */
package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 11. 28.
 * @사용처
 * 
 */
public class a2869_달팽이는올라가고싶다 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 28.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int A = scanner.nextInt(); //낮에 올라가는 거리
		int B = scanner.nextInt(); //밤에  내려가는 거리
		int V = scanner.nextInt(); //총 이동해야하는 거리
		
		scanner.close();
		
		System.out.println((int)Math.ceil((double)(V-B)/(A-B)));
	}

}

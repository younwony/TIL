package _0_Bronze.Level_3;

import java.util.Arrays;
import java.util.Scanner;

public class a10817_세수 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 9. 29.
	 * @사용처 세수 비교
	 * @param args
	 * @Todo Arryas.sort(abc)를 이용해 abc[1] 중간값을 가져오게 하는 간단한 방법도 존재하였다.
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int[] abc = new int[3];
		int max;
		
		abc[0] = scanner.nextInt();
		abc[1] = scanner.nextInt();
		abc[2] = scanner.nextInt();
		
		max = Math.max(abc[0], Math.max(abc[1], abc[2]));
		
		for(int a = 0; a < abc.length; a++) {
			if(abc[a] == max) {
				abc[a] = -1;
				break;
			}
		}
		
		max = Math.max(abc[0], Math.max(abc[1], abc[2]));
		
		System.out.println(max);
	}

}

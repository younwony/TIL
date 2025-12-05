/**
 * @작성자 wony
 * @작성일 2020. 2. 27.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 27.
 * @사용처
 * @Todo
 */

public class a1940_주몽 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 27.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int testCase = scanner.nextInt();
		int needNum = scanner.nextInt();
		
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		
		for(int i = 0; i < testCase; i++) {
			arrayList.add(scanner.nextInt());
		}
		
		scanner.close();
		
		int count = 0;
		
		arrayList.sort(null);
		
		int index;
		for(int i = 0; i < testCase; i++) {
			
			if(arrayList.get(i) > needNum/2) {
				break;
			}
			
			index = arrayList.indexOf(needNum - arrayList.get(i));
			if(index != -1 && index != i) {
				count++;
			}
			
		}
		
		System.out.println(count);
		
	}

}

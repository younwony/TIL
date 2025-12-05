/**
 * @작성자 wony
 * @작성일 2019. 11. 15.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 11. 15.
 * @사용처 중복 제외 정렬
 * 
 */
public class a10867_중복빼고정렬하기 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 15.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCount = scanner.nextInt();
		
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		
		int data;
		
		for(int i = 0; i< testCount; i++){
			data = scanner.nextInt();
			if(arrayList.indexOf(data) == -1)
				arrayList.add(data);
		}
		
		arrayList.sort(null);
		
		for(int out : arrayList)
			System.out.print(out + " ");
	}

	/**
	 *
	 *  2020.07.26 Again Study
	 *
	 *  public static void main(String[] args) {
	 *         Scanner scanner = new Scanner(System.in);
	 *
	 *         int n = scanner.nextInt();
	 *         scanner.nextLine();
	 *         String[] inputData = scanner.nextLine().split(" ");
	 *
	 *         boolean[] isNumber = new boolean[2001];
	 *
	 *         for(String str : inputData){
	 *             isNumber[Integer.valueOf(str) + 1000] = true;
	 *         }
	 *
	 *         StringBuilder result = new StringBuilder();
	 *
	 *         for(int i = 0 ; i < isNumber.length; i++){
	 *             if(isNumber[i]){
	 *                 result.append(i - 1000 + " ");
	 *             }
	 *         }
	 *
	 *         System.out.println(result.toString());
	 *
	 *     }
	 */

}

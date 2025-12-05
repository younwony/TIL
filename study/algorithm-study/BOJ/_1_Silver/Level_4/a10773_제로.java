/**
 * @작성자 wony
 * @작성일 2020. 2. 19.
 * @사용처
 *
 */
package _1_Silver.Level_4;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 19.
 * @사용처
 * 
 */
public class a10773_제로 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 19.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCase = scanner.nextInt();
		
		int index = -1;
		
		int data;
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		
		for(int i = 0; i < testCase; i++){
			data = scanner.nextInt();
			if(data == 0){
				arrayList.remove(index--);
			}else{
				arrayList.add(data);
				index++;
			}
		}
		
		scanner.close();
		
		int sum = 0;
		for(int i : arrayList){
			sum += i;
		}
		
		System.out.println(sum);
	}

}

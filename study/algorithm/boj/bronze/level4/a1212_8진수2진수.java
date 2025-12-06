/**
 * @작성자 wony
 * @작성일 2020. 1. 12.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 12.
 * @사용처
 * @Todo
 */

public class a1212_8진수2진수 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 12.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();
		String[] firstArray = {"0","1","10","11","100","101","110","111"};
		String[] afterArray = {"000","001","010","011","100","101","110","111"};
		
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < str.length(); i++){
			if(i == 0){
				builder.append(firstArray[str.charAt(i)-'0']);
			}else{
				builder.append(afterArray[str.charAt(i)-'0']);
			}
		}
		System.out.println(builder.toString());
		
		scanner.close();
	}

}

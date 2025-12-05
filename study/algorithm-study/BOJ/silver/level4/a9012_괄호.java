/**
 * @작성자 wony
 * @작성일 2019. 11. 16.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 11. 16.
 * @사용처 괄호
 * @Todo
 */

public class a9012_괄호 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 16.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCount = scanner.nextInt();
		scanner.nextLine();
		
		for(int i = 0; i < testCount; i++) {
				System.out.println(VPS(scanner.nextLine()));
		}
		scanner.close();
	}
	
	public static String VPS(String data) {
		if("".equals(data))
			return "YES";
		else {
			if(data.indexOf("()") >= 0)
				return VPS(data.replace("()", ""));
			else
				return "NO";
		}
	}

}

/**
 * @작성자 wony
 * @작성일 2019. 12. 9.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_2;

import java.util.HashMap;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 9.
 * @사용처
 * @Todo
 */

public class a1076_저항 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 9.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		String[] result = {"black","brown","red","orange","yellow","green","blue","violet","grey","white"};
		
		int x = 0;
		int y = 0;
		int z = 0;
		String a,b,c;
		a = scanner.nextLine();
		b = scanner.nextLine();
		c = scanner.nextLine();
		for(int i = 0; i < result.length; i++) {
			if(a.equals(result[i])) x = 10*i;
			if(b.equals(result[i])) y = i;
			if(c.equals(result[i])) z = (int)Math.pow(10, i);
		}
		
		scanner.close();
		System.out.println((x+y)*z);
	}

}

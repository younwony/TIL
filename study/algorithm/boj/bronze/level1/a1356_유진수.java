/**
 * @작성자 wony
 * @작성일 2020. 2. 29.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 29.
 * @사용처
 * @Todo
 */

public class a1356_유진수 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 29.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		String testNum = scanner.nextLine();
		
		scanner.close();
		
		String n,m;
		
		boolean isResult = false;
		
		for(int i = 0; i < testNum.length(); i++) {
			n = testNum.substring(0,i);
			m = testNum.substring(i, testNum.length());
			if(isEqual(n, m)) {
				isResult = true;
				break;
			}
		}
		
		System.out.println(isResult ? "YES" : "NO");
	}
	
	public static boolean isEqual(String n, String m) {
		int nCount = 1;
		int mCount = 1;
		
		for(int i = 0; i < n.length(); i++) {
			nCount *= n.charAt(i) - '0';
		}
		for(int i = 0; i < m.length(); i++) {
			mCount *= m.charAt(i) - '0';
		}
		
		return nCount == mCount;
	}

}

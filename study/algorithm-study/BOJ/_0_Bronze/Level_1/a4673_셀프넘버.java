/**
 * @작성자 wony
 * @작성일 2019. 10. 19.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_1;

import java.util.ArrayList;
import java.util.List;

/**
 * @작성자 wony
 * @작성일 2019. 10. 19.
 * @사용처
 * @Todo
 */

public class a4673_셀프넘버 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 19.
	 * @사용처 selfNumber
	 * @param args
	 * @Todo
	 */

	
	static boolean[] selftNum = new boolean[10001];
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int count = 1; count <= 10000; count++) {
			selfNumber(count);
		}
		
		for(int i = 1; i < selftNum.length; i++) {
			if(!selftNum[i])
				System.out.println(i);
		}
	}
	
	public static void selfNumber(int num) {
		int sum = num;
		while(num != 0) {
			sum += num%10;
			num /= 10;
		}
		if(sum <= 10000) selftNum[sum] = true;
	}

}

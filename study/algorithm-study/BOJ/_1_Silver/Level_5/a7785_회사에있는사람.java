/**
 * @작성자 wony
 * @작성일 2020. 2. 8.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 8.
 * @사용처
 * @Todo
 */

public class a7785_회사에있는사람 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 8.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int testCase = Integer.parseInt(scanner.next());
		
		HashMap<String, String> hashMap = new HashMap<String, String>();
		
		
		for(int i = 0; i < testCase; i++) {
			hashMap.put(scanner.next(), scanner.next());
		}
		
		scanner.close();
		
		ArrayList<String> keySet = new ArrayList<String>(hashMap.keySet());
		
		keySet.sort(null);
		Collections.reverse(keySet);
		
		
		for(String key : keySet) {
			if("enter".equals(hashMap.get(key))){
				System.out.println(key);
			}
		}
	}

}

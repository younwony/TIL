/**
 * @작성자 wony
 * @작성일 2019. 12. 19.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.util.Scanner;
import java.util.Stack;

/**
 * @작성자 wony
 * @작성일 2019. 12. 19.
 * @사용처
 * 
 */
public class a1316_그룹단어체커 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 19.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCount = scanner.nextInt();
		scanner.nextLine();
		String[] testStrArray;
		
		Stack<String> stack = new Stack<String>();
		boolean groupLang;
		int count = 0;
		for(int i = 0; i < testCount; i++){
			testStrArray = scanner.nextLine().split("");
			groupLang = true;
			for(String data : testStrArray){
				if(stack.isEmpty())stack.push(data);
				else if(stack.peek().equals(data))stack.push(data);
				else if(stack.contains(data)){groupLang = false; break;}
				else stack.push(data);
			}
			stack.clear();
			if(groupLang)count++;
		}
		
		scanner.close();
		System.out.println(count);
	}

}

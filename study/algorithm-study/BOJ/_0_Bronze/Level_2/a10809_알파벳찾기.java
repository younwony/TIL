/**
 * @작성자 wony
 * @작성일 2019. 10. 29.
 * @사용처
 *
 */
package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 10. 29.
 * @사용처 알파벳 찾기
 * 
 */
public class a10809_알파벳찾기 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 29.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		String inputData = scanner.nextLine();
		scanner.close();
		int[] outputData = new int[26];
		
		for(int i = 0; i < outputData.length; i++)
			outputData[i] = -1;
		
		for(int i = 0; i < inputData.length(); i++){
			if(outputData[(int)inputData.charAt(i)%97] == -1)
				outputData[(int)inputData.charAt(i)%97] = i;
		}
		
		for(int i = 0; i < outputData.length; i++)
			System.out.print(outputData[i] + " ");
		
			
	}

}

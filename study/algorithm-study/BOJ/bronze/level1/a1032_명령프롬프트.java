/**
 * @작성자 wony
 * @작성일 2020. 1. 17.
 * @사용처
 *
 */
package _0_Bronze.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 17.
 * @사용처
 * 
 */
public class a1032_명령프롬프트 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 17.
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
		String[][] inputData = new String[testCount][50];
		String equalStr;
		boolean eqaulResult;
		StringBuilder resultStr = new StringBuilder();

		for(int i = 0; i < testCount; i++){
			inputData[i] = scanner.nextLine().split("");
		}

		scanner.close();
		
		for(int i = 0; i < inputData[0].length; i++){
			equalStr = inputData[0][i];
			eqaulResult = true;
			for(int j = 1; j < testCount; j++){
				if(!equalStr.equals(inputData[j][i])){
					eqaulResult = false;
					break;
				}
			}
			if(eqaulResult){
				resultStr.append(equalStr);
			}else{
				resultStr.append("?");
			}
		}
		
		System.out.println(resultStr.toString());
	}

}

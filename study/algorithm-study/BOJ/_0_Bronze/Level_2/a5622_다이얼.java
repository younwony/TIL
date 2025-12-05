/**
 * @작성자 wony
 * @작성일 2019. 12. 18.
 * @사용처
 *
 */
package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 18.
 * @사용처
 * 
 */
public class a5622_다이얼 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 18.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String[] inputData = scanner.nextLine().split("");
		scanner.close();
		int sum = 0;
		for(int i = 0; i < inputData.length; i++)
			sum += DialNumber(inputData[i]);
		System.out.println(sum);
	}
	
	public static int DialNumber(String str){
		if("A".equals(str) || "B".equals(str) || "C".equals(str)) return 3;
		if("D".equals(str) || "E".equals(str) || "F".equals(str)) return 4;
		if("G".equals(str) || "H".equals(str) || "I".equals(str)) return 5;
		if("J".equals(str) || "K".equals(str) || "L".equals(str)) return 6;
		if("M".equals(str) || "N".equals(str) || "O".equals(str)) return 7;
		if("P".equals(str) || "Q".equals(str) || "R".equals(str) || "S".equals(str)) return 8;
		if("T".equals(str) || "U".equals(str) || "V".equals(str)) return 9;
		if("W".equals(str) || "X".equals(str) || "Y".equals(str) || "Z".equals(str)) return 10;
		return 0;
	}

}

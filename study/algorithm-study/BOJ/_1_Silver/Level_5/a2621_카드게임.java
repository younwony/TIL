/**
 * @작성자 wony
 * @작성일 2020. 3. 11.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 3. 11.
 * @사용처
 * 
 */
public class a2621_카드게임 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 3. 11.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		HashMap<String, Integer> colorTypeMap = new LinkedHashMap<String, Integer>();
		colorTypeMap.put("R", 0);
		colorTypeMap.put("B", 1);
		colorTypeMap.put("Y", 2);
		colorTypeMap.put("G", 3);
		int[] colorType = new int[4]; // R, B, Y, G
		int[] colorNumber = new int[10];
		
		String[] inputData;
		for(int i = 0; i < 5; i++){
			inputData = scanner.nextLine().split(" ");
			colorType[colorTypeMap.get(inputData[0])]++;
			colorNumber[Integer.valueOf(inputData[1])]++;
		}
		
		int maxType = 0;
		int maxNumber = 0;
		int maxNumberIndex = 0;
		
		for(int i = 0 ; i < 4; i++){
			if(colorType[i] >= maxType ){
				maxType = colorType[i];
			}
		}
		for(int i = 0 ; i < 10; i++){
			if(colorNumber[i] >= maxNumber ){
				maxNumber = colorNumber[i];
				maxNumberIndex = i;
			}
		}
		
		boolean isFlush = false;
		int nextMaxNumberIndex = 0;
		if(maxNumber == 3){
			for(int i = 0; i < 10; i++){
				if(colorNumber[i] == 2){
					nextMaxNumberIndex = i;
					isFlush = true;
					break;
				}
			}
		}
		
		boolean isTwoPair = false;
		int twoPairIndex = 0;
		if(maxNumber == 2){
			for(int i = 0; i < 10; i ++){
				if(colorNumber[i] == 2 && i != maxNumberIndex){
					twoPairIndex = i;
					isTwoPair = true;
					break;
				}
			}
		}

		boolean isStraight = false;
		if(maxNumber == 1 && maxNumberIndex > 4){
			for(int i = 0 ; i< 4; i++){
				isStraight = (colorNumber[maxNumberIndex - i] - colorNumber[maxNumberIndex - i -1] == 0) ? true : false;
				if(!isStraight){break;}
			}
		}
		
		if(maxType == 5 && isStraight){
			System.out.println(900 + maxNumberIndex);
		}else if(maxNumber == 4){
			System.out.println(800 + maxNumberIndex);
		}else if(isFlush){
			System.out.println(700 + maxNumberIndex*10 + nextMaxNumberIndex);
		}else if(maxType == 5){
			System.out.println(600 + maxNumberIndex);
		}else if(isStraight){
			System.out.println(500 + maxNumberIndex);
		}else if(maxNumber == 3){
			System.out.println(400 + maxNumberIndex);
		}else if(isTwoPair){
			System.out.println(300 + maxNumberIndex * 10 + twoPairIndex);
		}else if(maxNumber == 2){
			System.out.println(200 + maxNumberIndex);
		}else{
			System.out.println(100 + maxNumberIndex);
		}
		
		scanner.close();
	}

}

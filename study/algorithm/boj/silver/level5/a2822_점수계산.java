/**
 * @작성자 wony
 * @작성일 2020. 2. 17.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * @작성자 wony
 * @작성일 2020. 2. 17.
 * @사용처
 * 
 */
public class a2822_점수계산 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 17.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
		
		for(int i = 0; i < 8; i++){
			map.put(scanner.nextInt(), i);
		}
		
		scanner.close();
		
		Iterator<Integer> iterator = map.descendingKeySet().iterator();
		
		StringBuilder builder = new StringBuilder();
		
		int sum =0;
		int score;
		int[] scoreArray = new int[5];
		
		for(int i = 0; i< 5; i++){
			score = iterator.next();
			sum += score;
			scoreArray[i] = map.get(score);
		}
		
		Arrays.sort(scoreArray);
		
		for(int i : scoreArray){
			builder.append(i+1 + " ");
		}
		
		System.out.println(sum);
		System.out.println(builder.toString());
	}

}

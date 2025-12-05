/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 *
 */
package _1_Silver.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 * 
 */
public class a4948_베르트랑공준 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 26.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int[] memDecimal = new int[(123456*2)+1];
		
		memDecimal[1] = 1;
		for(int i = 2; i < (int)Math.sqrt(memDecimal.length); i++){
			for(int j = i*i; j < memDecimal.length; j += i){
				memDecimal[j] = 1;
			}
		}
		
		int count;
		int testN;
		int testNDouble;
		while(0 != (testN = scanner.nextInt())){
			count = 0;
			testNDouble = testN*2;
			testN++;
			for(; testN <= testNDouble; testN++){
				if(memDecimal[testN] == 0){count++;}
			}
			System.out.println(count);
		}
		
		scanner.close();
	}

}

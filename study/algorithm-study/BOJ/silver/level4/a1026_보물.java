/**
 * @작성자 wony
 * @작성일 2019. 11. 12.
 * @사용처
 *
 */
package _1_Silver.Level_4;

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 11. 12.
 * @사용처 보물
 * 
 */
public class a1026_보물 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 12.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCount = scanner.nextInt();
		
		Integer[] arrayA = new Integer[testCount];
		Integer[] arrayB = new Integer[testCount];
		
		for(int i = 0; i < testCount; i++)
			arrayA[i] = scanner.nextInt();
		
		for(int i = 0; i < testCount; i++)
			arrayB[i] = scanner.nextInt();
		
		Arrays.sort(arrayB);
		Arrays.sort(arrayA, Collections.reverseOrder());
		
		int minSum = 0;
		
		for(int i = 0; i < testCount; i++)
			minSum += arrayB[i]*arrayA[i];
		
		System.out.println(minSum);
	}

	/**
	 * 2020.07.26 Again Study
	 *
	 *  public static void main(String[] args) {
	 *         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	 *
	 *         try {
	 *             int n = Integer.valueOf(bufferedReader.readLine());
	 *             int[] aArray = new int[n];
	 *             int[] bArray = new int[n];
	 *             String[] inputArray = bufferedReader.readLine().split(" ");
	 *             for(int i = 0 ; i < n; i++){
	 *                 aArray[i] = Integer.valueOf(inputArray[i]);
	 *             }
	 *             inputArray = bufferedReader.readLine().split(" ");
	 *             for(int i = 0; i < n; i++){
	 *                 bArray[i] = Integer.valueOf(inputArray[i]);
	 *             }
	 *
	 *             Arrays.sort(aArray);
	 *             Arrays.sort(bArray);
	 *
	 *             int sum = 0;
	 *             for(int i = 0 ; i < n; i++){
	 *                 sum += aArray[i]*bArray[n-1-i];
	 *             }
	 *             System.out.println(sum);
	 *         } catch (Exception e){
	 *             e.printStackTrace();
	 *         } finally {
	 *             try {
	 *                 if(bufferedReader != null){ bufferedReader.close();}
	 *             }catch (Exception e2){
	 *                 e2.printStackTrace();
	 *             }
	 *
	 *         }
	 *     }
	 */
}

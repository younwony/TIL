/**
 * @작성자 wony
 * @작성일 2020. 1. 15.
 * @사용처
 *
 */
package _1_Silver.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 15.
 * @사용처
 * 
 */
public class a1182_부분수열의합 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 15.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	
	static int[] mem = new int[40000001];
	static int[] inputArray;
	static boolean[] visited;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int testCount = scanner.nextInt();
		inputArray = new int[testCount];
		visited = new boolean[testCount];
		int resultSum = scanner.nextInt();
		for(int i = 0; i < testCount; i++)
			inputArray[i] = scanner.nextInt();
		scanner.close();
		
		dfs(0);
		
		if(mem[0]>0){mem[0]--;}
		
		if(resultSum > 0){System.out.println(mem[resultSum+20000000]);}
		else{System.out.println(mem[Math.abs(resultSum)]);}
		
	}
	
	public static void dfs(int index){
		int sum = 0;
		for(int i = 0; i < inputArray.length; i++){
			if(visited[i]){
				sum += inputArray[i];
			}
		}
		
		if(sum > 0){mem[sum+20000000]++;}
		else{mem[Math.abs(sum)]++;}
		
		for(int i = index; i < inputArray.length; i++){
			visited[i] = true;
			dfs(i+1);
			visited[i] = false;
		}
	}

}

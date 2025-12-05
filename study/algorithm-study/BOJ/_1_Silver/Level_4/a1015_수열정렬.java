package _1_Silver.Level_4;

import java.util.Scanner;

public class a1015_수열정렬 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		
		int[] aArray = new int[n];
		
		for(int i = 0 ; i< n; i++){
			aArray[i] = scanner.nextInt();
		}
		
		int count;
		
		StringBuilder result = new StringBuilder();
		
		for(int i = 0; i < n; i++){
			count = 0;
			for(int j=  0; j < n; j++){
				if(aArray[i] > aArray[j]){
					count++;
				}else if(aArray[i] == aArray[j] && j < i){
					count++;
				}
			}
			result.append(count + " ");
		}
		
		System.out.println(result.toString());
	}

}

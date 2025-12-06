package _1_Silver.Level_5;

import java.util.Arrays;
import java.util.Scanner;

public class a1059_수2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int l = scanner.nextInt();
		int[] lSet = new int[l];
		
		for(int i = 0; i < l; i++){
			lSet[i] = scanner.nextInt();
		}
		
		int n = scanner.nextInt();
		
		Arrays.sort(lSet);
		
		int leftN = 0;
		int rightN = 0;
		
		for(int i = 0 ; i< l; i++){
			if(n <= lSet[i]){
				leftN = i == 0 ? 0 : lSet[i-1];
				rightN = lSet[i];
				break;
			}
		} 
		
		int result = (n - leftN - 1) * (rightN - n) + (rightN - n -1);
		if(n == rightN){
			result = 0;
		}
		System.out.println(result);
	}

}

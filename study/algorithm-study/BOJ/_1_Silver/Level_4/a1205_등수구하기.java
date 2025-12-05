package _1_Silver.Level_4;

import java.util.Scanner;

public class a1205_등수구하기 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		int score = scanner.nextInt();
		int p = scanner.nextInt();
		
		if(n > 0){
			int[] nArray = new int[n];
			
			int count = 1;
			
			for(int i = 0 ; i < n; i++){
				nArray[i] = scanner.nextInt();
				if(nArray[i] > score){
					count++;
				}
			}
			
			
			
			if(nArray[n-1] >= score && n == p){
				System.out.println(-1);
			}else{
				System.out.println(count);
			}
		}else{
			System.out.println(1);
		}
		
		scanner.close();
	}

}

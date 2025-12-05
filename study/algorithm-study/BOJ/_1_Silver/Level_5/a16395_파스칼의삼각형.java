package _1_Silver.Level_5;

import java.util.Scanner;

public class a16395_파스칼의삼각형 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		
		int k = scanner.nextInt();
		
		int[][] pascalTriangle = new int[30][30];
		for (int i = 0 ; i < 30; i++){
			pascalTriangle[i][0] = 1;
			pascalTriangle[i][i] = 1;
		}
		
		for (int i = 2 ; i < 30; i++){
			for(int j = 1; j < i; j++){
				pascalTriangle[i][j] = pascalTriangle[i-1][j-1] + pascalTriangle[i-1][j]; 
			}
		}
		
		scanner.close();
		
		System.out.println(pascalTriangle[n-1][k-1]);
		
	}

}

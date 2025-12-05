package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a1932_정수삼각형 {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		int n = Integer.valueOf(bufferedReader.readLine());
		int[][] triangle = new int[n][n];

		StringTokenizer stringTokenizer;
		for(int i = 0; i < n; i++){
			stringTokenizer = new StringTokenizer(bufferedReader.readLine());

			for(int j = 0; j <= i; j++){
				triangle[i][j] = Integer.parseInt(stringTokenizer.nextToken());
			}
		}


		for(int i = 1; i < n; i++){
			for(int j = 0; j <= i; j++){
				if(j == 0){
					triangle[i][j] += triangle[i-1][j];
				}else if(j == i){
					triangle[i][j] += triangle[i-1][j-1];
				}else{
					triangle[i][j] += Math.max(triangle[i-1][j-1], triangle[i-1][j]);
				}
			}
		}

		int max = 0;

		for(int i = 0; i < n; i++){
			max = Math.max(max, triangle[n-1][i]);
		}

		System.out.println(max);

	}

}

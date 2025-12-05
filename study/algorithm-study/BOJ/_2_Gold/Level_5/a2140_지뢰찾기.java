package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class a2140_지뢰찾기 {

	static char[][] inputArray;
	static int[] iArray = {-1,-1,-1,0,0,1,1,1};
	static int[] jArray = {-1,0,1,-1,1,-1,0,1};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			int n = Integer.valueOf(bufferedReader.readLine());
			inputArray = new char[n][n];
			
			for(int i = 0; i < n; i++){
				inputArray[i] = bufferedReader.readLine().toCharArray();
			}
			
			int resultCount = 0;
			
			if(n < 2){
				System.out.println(0);
			}else{
				for(int i = 1; i < n - 1; i++){
					for (int j = 1; j < n - 1; j++){
						if(isZero(i, j)){
							continue;
						}else{
							fieldMinus(i, j);
							resultCount++;
						}
					}
				}
				System.out.println(resultCount);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if(bufferedReader != null){bufferedReader.close();}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
	
	public static boolean isZero(int i, int j){
		
		for(int k = 0 ; k < 8; k++){
			if('0' == inputArray[i + iArray[k]][j + jArray[k]]){
				return true;
			}
		}
		
		return false;
	}
	
	public static void fieldMinus(int i, int j){
		for(int k = 0 ; k < 8; k++){
			if(inputArray[i + iArray[k]][j + jArray[k]] > 0){
				inputArray[i + iArray[k]][j + jArray[k]]--;
			}
		}
	}
}

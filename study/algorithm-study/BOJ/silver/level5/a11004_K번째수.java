package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * @작성자 wony
 * @작성일 2020. 2. 10.
 * @사용처
 *
 */
public class a11004_K번째수 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		try {
			st = new StringTokenizer(bufferedReader.readLine());
			int n = Integer.parseInt(st.nextToken());
	        int k = Integer.parseInt(st.nextToken());
	        
	        int[] num = new int[n];
	        int[] num2 = new int[n];
	        
	        st = new StringTokenizer(bufferedReader.readLine());
	        for (int i = 0; i < n; i++){
	        	num[i] = Integer.parseInt(st.nextToken());
	        	num2[i] = num[i];
	        }
	        
	        mergeSort(num, num2, 0, num2.length-1);
	        
	        System.out.print(num[k - 1]);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				if(bufferedReader != null) {bufferedReader.close();}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
	
	public static void mergeSort(int[] saveArray, int[] array, int left ,int right){
		if(right <= left)
			return ;
		
		int middle = (right+left)/2;
		
		mergeSort(saveArray, array, left, middle);
		mergeSort(saveArray, array, middle+1, right);
		mergeSortSum(saveArray, array, left, middle, right);
	}
	public static void mergeSortSum(int[] saveArray, int[] array, int left ,int middle, int right){
		
		int i = left;
		int j = middle +1;
		int k = left;
		
		while(i <= middle && j <=right){
			if(saveArray[i] >= saveArray[j]){
				array[k] = saveArray[j];
				j++;
			}else{
				array[k] = saveArray[i];
				i++;
			}
			k++;
		}
		
		while(i <= middle){
			array[k] = saveArray[i];
			k++;
			i++;
		}
		
		while(j <= right){
			array[k] = saveArray[j];
			k++;
			j++;
		}
		
		for(; left<=right; left++){
			saveArray[left] = array[left];
		}
	}

}

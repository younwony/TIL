package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a14888_연산자끼워넣기 {

	static int[] nArray;
	static int[] signArray; // 0 : +, 1 : -, 2 : x, 3 : /
	static int max = Integer.MIN_VALUE;
	static int min = Integer.MAX_VALUE;
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		int n = Integer.valueOf(bufferedReader.readLine());
		
		StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine(), " ");
		nArray = new int[n];
		int i = 0;
		while(stringTokenizer.hasMoreElements()){
			nArray[i++] = Integer.valueOf(stringTokenizer.nextToken());
		}
		
		stringTokenizer = new StringTokenizer(bufferedReader.readLine(), " ");
		signArray = new int[4];
		
		i = 0;
		while(stringTokenizer.hasMoreElements()){
			signArray[i++] = Integer.valueOf(stringTokenizer.nextToken());
		}
		
		dfs(1, nArray[0]);
		
		System.out.println(max);
		System.out.println(min);
		
	}
	
	public static void dfs(int index, int sum){
		
		
		if(index == nArray.length){
			if(sum > max){
				max = sum;
			}
			if(sum < min){
				min = sum;
			}
			return ;
		}
		
		for(int i = 0 ; i < 4; i++){
			if(signArray[i] != 0){
				signArray[i]--;
				dfs(index+1, calculation(i, index, sum));
				signArray[i]++;
			}
		}
	}
	                 
	public static int calculation(int sign, int index, int sum){
		switch (sign) {
		case 0: sum += nArray[index];
				break;
		case 1: sum -= nArray[index];
				break;
		case 2: sum *= nArray[index];
				break;
		case 3: 
			if(sum < 0){
				sum *= -1;
				sum /= nArray[index];
				sum *= -1;
			}else{
				sum /= nArray[index];
			}
				break;
		default:
				break;
		}
		
		return sum;
	}
}

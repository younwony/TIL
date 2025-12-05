package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Stack;

public class a2493_탑 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder result = new StringBuilder();
		
		try {
			int n = Integer.valueOf(bufferedReader.readLine());
			String[] nArray = bufferedReader.readLine().split(" ");
			
			Stack<int[]> initStack = new Stack<>();
			Stack<int[]> tempStack = new Stack<>();
			
			for(int i = 0; i < n; i++){
				initStack.push(new int[]{Integer.valueOf(nArray[i]), i});
			}
			
			int[] resultData = new int[n];
			
			int index = n;
			
			while(!initStack.empty()){
				index--;
				tempStack.push(initStack.pop());
				if(!initStack.empty() && initStack.peek()[0] > tempStack.peek()[0]){
					while(!tempStack.empty() && initStack.peek()[0] > tempStack.peek()[0]){
						resultData[tempStack.pop()[1]] = index; 
					}
				}
			}
			
			for(int i = 0; i < n; i++){
				result.append(resultData[i] + " ");
			}
			
			System.out.println(result.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				if(bufferedReader != null){bufferedReader.close();}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

}

/**
 * @작성자 wony
 * @작성일 2019. 12. 30.
 * @사용처
 *
 */
package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Stack;

/**
 * @작성자 wony
 * @작성일 2019. 12. 30.
 * @사용처
 * 
 */
public class a15652_N과M_4 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 30.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	static Stack<Integer> stack = new Stack<Integer>();
	static int n;
	static BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			String[] inputData = bufferedReader.readLine().split(" ");
			n = Integer.parseInt(inputData[0]);
			int m = Integer.parseInt(inputData[1]);
			BackTraking(1, m);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if(bufferedReader != null){bufferedReader.close();}
				if(bufferedWriter != null){bufferedWriter.flush(); bufferedWriter.close();}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
	
	public static void BackTraking(int i, int m){
		if(m == 0){
			try {
				for(int j = 0; j < stack.size(); j++){
					bufferedWriter.write(stack.get(j) + " ");
				}
				bufferedWriter.flush();
				bufferedWriter.newLine();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}else{
			for(; i <= n; i++){
				if(stack.isEmpty() || i >= stack.peek()){
				stack.push(i);
				BackTraking(i, m - 1);
				stack.pop();
				}
			}
		}
	}

}

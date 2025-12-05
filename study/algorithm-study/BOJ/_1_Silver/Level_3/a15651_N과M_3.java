/**
 * @작성자 wony
 * @작성일 2019. 12. 28.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.Stack;

/**
 * @작성자 wony
 * @작성일 2019. 12. 28.
 * @사용처
 * @Todo
 */

public class a15651_N과M_3 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 28.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	static Stack<Integer> stack = new Stack<Integer>();
	static BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			String[] inputData =bufferedReader.readLine().split(" ");
			backTracking(Integer.parseInt(inputData[0]), Integer.parseInt(inputData[1]));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				if(bufferedReader != null) bufferedReader.close();
				if(bufferedWriter != null) {bufferedWriter.flush(); bufferedWriter.close();}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		
	}
	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 28.
	 * @사용처 Multiset Permutation(중복순열)
	 * @param n
	 * @param m
	 * @Todo
	 */
	public static void backTracking(int n, int m){
		if(m == 0){
			try {
				for(int i = 0; i < stack.size(); i++) {
					bufferedWriter.write(stack.get(i) + " ");
				}
				bufferedWriter.flush();
				bufferedWriter.newLine();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}else{
			for(int i = 1; i <= n; i++){
				stack.push(i);
				backTracking(n, m-1);
				stack.pop();
			}
		}
	}

}

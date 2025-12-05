/**
 * @작성자 wony
 * @작성일 2020. 3. 3.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 3. 3.
 * @사용처
 * @Todo
 */

public class a11723_집합 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 3. 3.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static int[] set = new int[21];
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
		try {
			
			int testCase = Integer.valueOf(bufferedReader.readLine());
			
			String[] inputData;
			
			for(int i = 0; i < testCase; i++) {
				inputData = bufferedReader.readLine().split(" ");
				if(inputData.length > 1) {
					if("check".equals(inputData[0])) {bufferedWriter.write(String.valueOf(set[Integer.valueOf(inputData[1])]) + "\n");}
					else{Conduct(inputData[0], Integer.valueOf(inputData[1]));}
				}else {
					Conduct(inputData[0], 0);
				}
			}
			bufferedWriter.flush();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if(bufferedReader != null) {bufferedReader.close();}
				if(bufferedWriter != null) { bufferedWriter.flush(); bufferedWriter.close();}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
	
	public static void Conduct(String data, int x) {
		switch (data) {
		case "add":	set[x] = 1; break;
		case "remove":	set[x] = 0;break;
		case "toggle":	if(set[x] == 1) {set[x] = 0;}else{set[x] = 1;}break;
		case "all":	for(int i = 0; i < set.length; i++) {set[i] = 1;}break;
		case "empty":	for(int i = 0; i < set.length; i++) {set[i] = 0;}break;
		}
	}

}

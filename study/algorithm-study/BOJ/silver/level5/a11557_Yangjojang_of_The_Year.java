/**
 * @작성자 wony
 * @작성일 2020. 3. 23.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @작성자 wony
 * @작성일 2020. 3. 23.
 * @사용처
 * 
 */
public class a11557_Yangjojang_of_The_Year {

	/**
	 * @작성자 wony
	 * @작성일 2020. 3. 23.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
		
		try {
			
			int testCase = Integer.valueOf(bufferedReader.readLine());
			
			String[] inputData = new String[2];
			int max,testCount;
			String maxIndex = "";
			
			StringBuilder result = new StringBuilder();
			for(int i = 0 ; i < testCase; i++){
				max = 0;
				testCount = Integer.valueOf(bufferedReader.readLine());
				for(int j = 0; j < testCount; j++){
					inputData = bufferedReader.readLine().split(" ");
					
					if(Integer.valueOf(inputData[1]) > max){
						max = Integer.valueOf(inputData[1]);
						maxIndex = inputData[0];
					}
				}
				result.append(maxIndex + "\n");
			}
			bufferedWriter.write(result.toString());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally{
			try {
				if(bufferedReader != null){bufferedReader.close();}
				if(bufferedWriter != null){bufferedWriter.flush(); bufferedWriter.close();}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

}

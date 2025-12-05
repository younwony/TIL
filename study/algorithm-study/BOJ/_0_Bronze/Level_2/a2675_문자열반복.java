/**
 * @작성자 wony
 * @작성일 2019. 12. 18.
 * @사용처
 *
 */
package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @작성자 wony
 * @작성일 2019. 12. 18.
 * @사용처
 * 
 */
public class a2675_문자열반복 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 18.
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
			int testCount = Integer.parseInt(bufferedReader.readLine());
			int repeatCount;
			String[] repeatString;
			String[] inputData;
			StringBuilder builder;
			for(int i = 0; i < testCount; i++){
				builder = new StringBuilder();
				inputData = bufferedReader.readLine().split(" ");
				repeatCount = Integer.parseInt(inputData[0]);
				repeatString = inputData[1].split("");
				for(int j =0; j < repeatString.length; j++){
					for(int k = 0; k < repeatCount; k++){
						builder.append(repeatString[j]);
					}
				}
				bufferedWriter.write(builder.toString()+"\n");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if(bufferedReader != null) bufferedReader.close();
				if(bufferedWriter != null){ bufferedWriter.flush(); bufferedWriter.close();}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

}

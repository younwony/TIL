/**
 * @작성자 wony
 * @작성일 2020. 5. 4.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @작성자 wony
 * @작성일 2020. 5. 4.
 * @사용처
 * @Todo
 */

public class a11365_밀비급일 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 5. 4.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
		
		try {
			StringBuilder str = new StringBuilder();
			
			String inputStr;
			while(!"END".equals(inputStr = bufferedReader.readLine())) {
				
				for(int i = 0; i < inputStr.length(); i++) {
					str.append(inputStr.charAt(inputStr.length() - i - 1));
				}
				str.append("\n");
			}
			
			bufferedWriter.write(str.toString());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				if(bufferedReader != null) {bufferedReader.close();}
				if(bufferedWriter != null) {bufferedWriter.flush(); bufferedReader.close();}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

}

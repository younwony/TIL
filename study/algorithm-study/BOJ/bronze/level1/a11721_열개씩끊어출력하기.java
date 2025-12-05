/**
 * @작성자 wony
 * @작성일 2019. 12. 13.
 * @사용처
 *
 */
package _0_Bronze.Level_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @작성자 wony
 * @작성일 2019. 12. 13.
 * @사용처
 * 
 */
public class a11721_열개씩끊어출력하기 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 13.
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
			String text = bufferedReader.readLine();
			int textLength = text.length();
			int textI = textLength/10;
			
			for(int i = 1; i <= textI; i++){
				bufferedWriter.write(text.substring(10*(i-1), 10*i) + "\n");
				bufferedWriter.flush();
			}
			bufferedWriter.write(text.substring(10*textI));
			bufferedWriter.flush();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if(bufferedReader != null) bufferedReader.close();
				if(bufferedWriter != null)bufferedWriter.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

}

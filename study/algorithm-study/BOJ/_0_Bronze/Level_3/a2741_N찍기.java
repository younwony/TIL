/**
 * @작성자 wony
 * @작성일 2019. 10. 2.
 * @사용처
 * TODO
 */
package _0_Bronze.Level_3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @작성자 wony
 * @작성일 2019. 10. 2.
 * @사용처
 * TODO
 */
public class a2741_N찍기 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 2.
	 * @사용처 1~N 출력
	 * @Todo
	 *  buffer read, write 사용시 -> 메모리 27596KB, 시간 224MS  소요
	 *  scanner, sysout 사용시 -> 메모리 34784, 시간 700ms 소요
	 *  buffer의 사용을 중요시하자!
	 * @param args
	 * TODO
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
		
		try {
			int inputData = Integer.parseInt(bufferedReader.readLine());
			for(int i = 1; i <= inputData; i++)
				bufferedWriter.write(i+"\n");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				bufferedReader.close();
				bufferedWriter.flush();
				bufferedWriter.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

}

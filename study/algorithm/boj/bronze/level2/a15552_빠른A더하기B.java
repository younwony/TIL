/**
 * @작성자 wony
 * @작성일 2019. 10. 2.
 * @사용처
 * TODO
 */
package _0_Bronze.Level_2;

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
public class a15552_빠른A더하기B {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 2.
	 * @사용처 BufferedReader, BufferedWriter (다량의 데이터를 사용할경우 버퍼를 사용하는것이 속도적 으로 더빠르다.)
	 * @Todo
	 * @param args
	 * TODO
	 */
	public static void main(String[] args) {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
		
		try {
			int testCaseCount = Integer.parseInt(bufferedReader.readLine());
			
			int[] result = new int[testCaseCount];
			String[] aAndb = new String[2];
			for(int i = 0; i < testCaseCount; i++){
				aAndb = bufferedReader.readLine().split(" ");
				result[i] = Integer.parseInt(aAndb[0])+Integer.parseInt(aAndb[1]);
			}
			
			for(int j : result)
				bufferedWriter.write(j+"\n");
			
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

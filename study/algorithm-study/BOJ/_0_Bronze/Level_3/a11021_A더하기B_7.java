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
public class a11021_A더하기B_7 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 2.
	 * @사용처 a+b v7
	 * @Todo
	 * 문자 구성중 숫자의 연산이 중간에 들어가는 경우 Integer.valueOf를 사용! Integer.parseInt는 사용이 안된다.
	 * @param args
	 * TODO
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
		
		try {
			int testCaseCount;
			testCaseCount = Integer.parseInt(bufferedReader.readLine());
			
			String[] result = new String[testCaseCount];
			String[] aAndb = new String[2];
			for(int i = 0; i < result.length; i++){
				aAndb = bufferedReader.readLine().split(" ");
				result[i] = "Case #" + Integer.valueOf(i+1) + ": " + String.valueOf(Integer.parseInt(aAndb[0])+Integer.parseInt(aAndb[1]));
			}
			
			for(String resultSet : result)
				bufferedWriter.write(resultSet + "\n");
			
			
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

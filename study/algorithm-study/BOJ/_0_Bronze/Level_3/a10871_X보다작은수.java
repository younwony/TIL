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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @작성자 wony
 * @작성일 2019. 10. 2.
 * @사용처
 * TODO
 */
public class a10871_X보다작은수 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 2.
	 * @사용처 N개의 정수중 X보다 작은 수 출력
	 * @Todo
	 *  BufferWriter.write() 에서 int형 출력시 ascii코드로 출력된다. 따라서 int형을 출력하고 싶을때는 String으로 형변환후 출력을 해줘야 한다.
	 *  scanner / sysout 메모리 , 시간 비교 결과 buffer 로직이 훨신빠르다.
	 * @param args
	 * TODO
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
		StringTokenizer stringTokenizer = null;
		
		try {
			int n,x;
			stringTokenizer = new StringTokenizer(bufferedReader.readLine(), " ");
			n = Integer.parseInt(stringTokenizer.nextToken());
			x = Integer.parseInt(stringTokenizer.nextToken());
			
			int result = 0;
			
			stringTokenizer = new StringTokenizer(bufferedReader.readLine(), " ");
			while(stringTokenizer.hasMoreTokens()){
				result = Integer.parseInt(stringTokenizer.nextToken());
				if(result < x)
					bufferedWriter.write(String.valueOf(result) + " ");
			}
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

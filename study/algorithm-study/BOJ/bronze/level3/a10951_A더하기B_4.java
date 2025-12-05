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
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * @작성자 wony
 * @작성일 2019. 10. 2.
 * @사용처
 * TODO
 */
public class a10951_A더하기B_4 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 2.
	 * @사용처 A+B v4
	 * @Todo
	 * buffer 를 이용해 출력해보고자 했지만 buffer에 쌓이는 개수를 지정 또는 확인 할 수 있는 방법을 찾아내지 못했다.
	 * @param args
	 * TODO
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			Scanner scanner = new Scanner(System.in);
			
			int a,b;
			
			while(scanner.hasNext()){
				a = scanner.nextInt();
				b = scanner.nextInt();
				
				System.out.println(a+b);
			}
	}
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
		StringTokenizer stringTokenizer = null;
		
		int a,b;
		String input;
		try {
			while(true){
				input = bufferedReader.readLine();
				if("".equals(input))
					break;
				stringTokenizer = new StringTokenizer(input, " ");
				a = Integer.parseInt(stringTokenizer.nextToken());
				b = Integer.parseInt(stringTokenizer.nextToken());
				bufferedWriter.write(String.valueOf(a+b) + "\n");
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
	}*/
}

/**
 * @작성자 wony
 * @작성일 2020. 3. 5.
 * @사용처
 *
 */
package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * @작성자 wony
 * @작성일 2020. 3. 5.
 * @사용처
 * 
 */
public class a1764_듣보잡 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 3. 5.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			String[] inputNM = bufferedReader.readLine().split(" ");
			int n = Integer.valueOf(inputNM[0]);
			int m = Integer.valueOf(inputNM[1]);
			
			ArrayList<String> nDataArray = new ArrayList<String>();
			ArrayList<String> resultData = new ArrayList<String>();
			
			for(int i = 0; i < n; i++){
				nDataArray.add(bufferedReader.readLine());
			}
			
			String isData;
			StringBuilder stringBuilder = new StringBuilder();
			int count = 0;
			for(int i = 0; i < m; i++){
				isData = bufferedReader.readLine();
				if(nDataArray.indexOf(isData) != -1){
					count++;
					resultData.add(isData);
				}
			}
			
			resultData.sort(null);
			
			for(String data : resultData){
				stringBuilder.append(data + "\n");
			}
			System.out.println(count);
			System.out.println(stringBuilder.toString());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if(bufferedReader != null){bufferedReader.close();}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

}

/**
 * @작성자 wony
 * @작성일 2019. 11. 15.
 * @사용처
 *
 */
package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

/**
 * @작성자 wony
 * @작성일 2019. 11. 15.
 * @사용처 숫자카드 2
 * 
 */
public class a10816_숫자카드2 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 15.
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
			int[] memPlusResult = new int[10000001];
			int[] memMinusResult = new int[10000001];
			
			Arrays.fill(memPlusResult, 0);
			Arrays.fill(memMinusResult, 0);
			
			int inputCount = Integer.parseInt(bufferedReader.readLine());
			
			String[] inputArray = bufferedReader.readLine().split(" ");
			
			int outputCount = Integer.parseInt(bufferedReader.readLine());
			
			String[] outputArray = bufferedReader.readLine().split(" ");
			
			
			for(int i = 0; i < inputCount; i++){
				if(Integer.parseInt(inputArray[i]) >= 0) 
					memPlusResult[Integer.parseInt(inputArray[i])]++; 
				else
					memMinusResult[Integer.parseInt(inputArray[i]) * -1]++;
			}
			
			for(String data : outputArray){
				if(Integer.parseInt(data) >= 0)
					bufferedWriter.write(String.valueOf(memPlusResult[Integer.parseInt(data)]));
				else
					bufferedWriter.write(String.valueOf(memMinusResult[Integer.parseInt(data) * -1]));
				bufferedWriter.write(" ");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally{
			try {
				if(bufferedReader != null) bufferedReader.close();
				if(bufferedWriter != null){ bufferedWriter.flush(); bufferedWriter.flush();}
				
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		
	}

	/**
	 * 2020.07.26 Again Study
	 *
	 * public static void main(String[] args) {
	 *         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	 *         try{
	 *             bufferedReader.readLine();
	 *
	 *             int[] nArray = new int[20000001];
	 *             StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
	 *
	 *             while(stringTokenizer.hasMoreTokens()){
	 *                 nArray[Integer.valueOf(stringTokenizer.nextToken()) + 10000000]++;
	 *             }
	 *
	 *             bufferedReader.readLine();
	 *
	 *             StringBuilder result = new StringBuilder();
	 *             stringTokenizer = new StringTokenizer(bufferedReader.readLine());
	 *             while(stringTokenizer.hasMoreTokens()){
	 *                 result.append(nArray[Integer.valueOf(stringTokenizer.nextToken()) + 10000000] + " ");
	 *             }
	 *
	 *             System.out.println(result.toString());
	 *         }catch (Exception e){
	 *             e.printStackTrace();
	 *         }finally {
	 *             try {
	 *                 if(bufferedReader != null){bufferedReader.close();}
	 *             }catch (Exception e2){
	 *                 e2.printStackTrace();
	 *             }
	 *         }
	 *     }
	 */

}

/**
 * @작성자 wony
 * @작성일 2019. 11. 11.
 * @사용처
 *
 */
package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @작성자 wony
 * @작성일 2019. 11. 11.
 * @사용처 에디터
 * 
 */
public class a1406_에디터 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 11.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
		StringBuilder builder = new StringBuilder();
		
		try {
			
			String[] startStr = bufferedReader.readLine().split("");
			
			int testCount = Integer.parseInt(bufferedReader.readLine());
			
			String[] str = new String[startStr.length + testCount];
			
			for(int i = 0; i< str.length; i++)
				str[i] = "";
			
			for(int i = 0; i < startStr.length; i++)
				str[i] = startStr[i];
			
			String[] order;
			
			int point = startStr.length;
			
			for(int i = 0; i< testCount; i++){
				order = bufferedReader.readLine().split(" ");
				if("L".equals(order[0])) point = editorLeftMove(str, point);
				if("D".equals(order[0])) point = editorRightMove(str, point);
				if("B".equals(order[0])) point = editorBackSpace(str, point);
				if("P".equals(order[0])) point = editorPlus(str, order[1], point);
			}
			
			for(String result : str)
				builder.append(result);
			
			bufferedWriter.write(builder.toString());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if(bufferedReader != null) bufferedReader.close();
				if(bufferedWriter != null) bufferedWriter.flush(); bufferedWriter.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
	
	static int editorLeftMove(String[] str, int point){
		return point == 0 ? 0 : point-1;
	}
	
	static int editorRightMove(String[] str, int point){
		int rightMaxPoint = str.length;
		
		for(int i = 0; i < str.length; i++){
			if("".equals(str[i])){
				rightMaxPoint = i;
				break;
			}
		}
		
		return point == rightMaxPoint ? point : point+1;
		
	}
	static int editorBackSpace(String[] str, int point){
		if(point == 0)
			return point;
		
		str[point-1] = "";
		
		for(int i = point-1; i < str.length-1; i++){
			str[i] = str[i+1];
		}
		
		return point-1;
		
	}
	static int editorPlus(String[] str, String data, int point){
		for(int i = str.length-1; i > point; i--){
			str[i] = str[i-1];
		}
		
		str[point] = data;
		
		return point + 1;
	}

	/**
	 * Deque 2개 로 처리
	 *
	 * static Deque<String> deque = new ArrayDeque<>();
	 *     static Deque<String> dequeClone = new ArrayDeque<>();
	 *     static StringBuilder result = new StringBuilder();
	 *     static String[] inputData;
	 *     public static void main(String[] args) {
	 *         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	 *
	 *         try {
	 *             String inputDataString = bufferedReader.readLine();
	 *             for(int i = 0 ; i < inputDataString.length(); i++){
	 *                 deque.offer(String.valueOf(inputDataString.charAt(i)));
	 *             }
	 *
	 *             int n = Integer.valueOf(bufferedReader.readLine());
	 *             for(int i = 0 ; i < n; i++){
	 *                 inputData = bufferedReader.readLine().split(" ");
	 *                 Order(inputData[0]);
	 *             }
	 *
	 *             while(!deque.isEmpty()){
	 *                 result.append(deque.pollFirst());
	 *             }
	 *
	 *             while(!dequeClone.isEmpty()){
	 *                 result.append(dequeClone.pollFirst());
	 *             }
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
	 *
	 *     public static void Order(String order){
	 *         if("L".equals(inputData[0])){
	 *             if(!deque.isEmpty()){
	 *                 dequeClone.offerFirst(deque.pollLast());
	 *             }
	 *         }else if("D".equals(inputData[0])){
	 *             if(!dequeClone.isEmpty()){
	 *                 deque.offer(dequeClone.pollFirst());
	 *             }
	 *         }else if("B".equals(inputData[0])){
	 *             if(!deque.isEmpty()){
	 *                 deque.pollLast();
	 *             }
	 *         }else if("P".equals(inputData[0])){
	 *             deque.offer(inputData[1]);
	 *         }
	 *
	 *     }
	 */
}

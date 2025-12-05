/**
 * @작성자 wony
 * @작성일 2019. 11. 11.
 * @사용처
 *
 */
package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @작성자 wony
 * @작성일 2019. 11. 11.
 * @사용처 덱(Deque)
 * 
 */
public class a10866_덱 {

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
		
		try {
			int testCount = Integer.parseInt(bufferedReader.readLine());
			
			int[] deque = new int[testCount];
			
			for(int i = 0; i < testCount; i++)
				deque[i] = 0;
			
			String[] order;
			
			for(int i = 0; i < testCount; i++){
				order = bufferedReader.readLine().split(" ");
				if("push_front".equals(order[0]))
					dequePush_Front(deque, Integer.parseInt(order[1]));
				if("push_back".equals(order[0]))
					dequePush_Back(deque, Integer.parseInt(order[1]));
				if("pop_front".equals(order[0]))
					bufferedWriter.write(dequePop_Front(deque) + "\n");
				if("pop_back".equals(order[0]))
					bufferedWriter.write(dequePop_Back(deque) + "\n");
				if("size".equals(order[0]))
					bufferedWriter.write(dequeSize(deque) + "\n");
				if("empty".equals(order[0]))
					bufferedWriter.write(dequeEmpty(deque) + "\n");
				if("front".equals(order[0]))
					bufferedWriter.write(dequeFront(deque) + "\n");
				if("back".equals(order[0]))
					bufferedWriter.write(dequeBack(deque) + "\n");
			}
			
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
	
	public static void dequePush_Front(int[] deque, int data){
		for(int i = deque.length -1; i > 0; i--){
			deque[i] = deque[i-1];
		}
		deque[0] = data;
	}
	
	public static void dequePush_Back(int[] deque, int data){
		for(int i = 0; i < deque.length; i++){
			if(deque[i] == 0){
				deque[i] = data;
				break;
			}
		}
	}
	
	public static int dequePop_Front(int[] deque){
		int data = deque[0] == 0 ? -1 : deque[0];
		
		for(int i = 0; i < deque.length - 1; i++)
			deque[i] = deque[i+1];
			
		return data;
	}
	
	public static int dequePop_Back(int[] deque){
		
		int data = -1;
		
		for(int i = deque.length-1; i>= 0; i--){
			if(deque[i] != 0){
				data = deque[i];
				deque[i] = 0;
				break;
			}
		}
		return data;
	}
	
	public static int dequeSize(int[] deque){
		int dequeSizeCount = 0;
		for(int i = 0; i < deque.length; i++){
			if(deque[i] != 0)
				dequeSizeCount++;
		}
		return dequeSizeCount;
	}	
	
	public static int dequeEmpty(int[] deque){
		return deque[0] == 0 ? (deque[deque.length-1] == 0 ? 1 : 0) : 0;
	}
	
	public static int dequeFront(int[] deque){
		return deque[0] == 0 ? -1 : deque[0];
	}
	
	public static int dequeBack(int[] deque){
		int data = -1;
		
		for(int i = deque.length-1; i >=0; i--){
			if(deque[i] != 0){
				data = deque[i];
				break;
			}
		}
		
		return data;
	}

	/**
	 *
	 * 좀더빠른 라이브러리 덱
	 *
	 *  static Deque<Integer> deque = new ArrayDeque<>();
	 *     static StringBuilder result = new StringBuilder();
	 *     static String[] inputData;
	 *     public static void main(String[] args) {
	 *         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	 *
	 *         try {
	 *             int n = Integer.valueOf(bufferedReader.readLine());
	 *             for(int i = 0 ; i < n; i++){
	 *                 inputData = bufferedReader.readLine().split(" ");
	 *                 stackOrder(inputData[0]);
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
	 *     public static void stackOrder(String order){
	 *         if("push_front".equals(order)){
	 *             deque.offerFirst(Integer.valueOf(inputData[1]));
	 *         }else if("push_back".equals(order)){
	 *             deque.offerLast(Integer.valueOf(inputData[1]));
	 *         }else if("pop_front".equals(order)){
	 *             result.append((deque.isEmpty() ? "-1" : deque.pollFirst()) + "\n");
	 *         }else if("pop_back".equals(order)){
	 *             result.append((deque.isEmpty() ? "-1" : deque.pollLast()) + "\n");
	 *         }else if("size".equals(order)){
	 *             result.append(Integer.toString(deque.size()) + "\n");
	 *         }else if("empty".equals(order)){
	 *             result.append((deque.isEmpty() ? "1" : "0") + "\n");
	 *         }else if("front".equals(order)){
	 *             result.append((deque.isEmpty() ? "-1" : deque.peekFirst()) + "\n");
	 *         }else if("back".equals(order)){
	 *             result.append((deque.isEmpty() ? "-1" : deque.peekLast()) + "\n");
	 *         }
	 *     }
	 */
}

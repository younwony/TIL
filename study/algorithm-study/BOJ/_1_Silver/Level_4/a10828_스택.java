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
 * @사용처 스택
 * 
 */
public class a10828_스택 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 11.
	 * @사용처 스택(Stack - 배열구현)
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
			
			int[] stack = new int[testCount];
			
			for(int i= 0; i< testCount; i++)
				stack[i] = 0;
			
			
			String[] order;
			
			for(int i =0; i < testCount; i++){
				order = bufferedReader.readLine().split(" ");
				if("push".equals(order[0]))
					stackPush(stack, Integer.parseInt(order[1]));
				if("pop".equals(order[0]))
					bufferedWriter.write(stackPop(stack) + "\n");
				if("size".equals(order[0]))
					bufferedWriter.write(stackSize(stack) + "\n");
				if("empty".equals(order[0]))
					bufferedWriter.write(stackEmpty(stack) + "\n");
				if("top".equals(order[0]))
					bufferedWriter.write(stackTop(stack) + "\n");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally{
			try {
				if(bufferedReader != null) bufferedReader.close();
				if(bufferedWriter != null) bufferedWriter.flush(); bufferedWriter.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
	
	public static void stackPush(int[] stack, int data){
		for(int i = stack.length -1 ; i > 0; i--){
			stack[i] = stack[i-1];
		}
		stack[0] = data;
	}
	
	public static int stackPop(int[] stack){
		
		int data;
		
		if(stack[0] == 0)
			data = -1;
		else
			data = stack[0];
		
		for(int i = 0; i < stack.length-1; i++)
			stack[i] = stack[i+1];
		
		return data;
	}
	
	public static int stackSize(int[] stack){
		
		int sizeCount = 0;
		
		for(int data : stack)
			if(data != 0)
				sizeCount++;
		
		return sizeCount; 
	}	
	
	public static int stackEmpty(int[] stack){
		if(stack[0] == 0)
			return 1;
		else
			return 0;
	}
	
	public static int stackTop(int[] stack){
		if(stack[0] == 0)
			return -1;
		else
			return stack[0];
	}

	/**
	 * Stack Lib 좀더 빠르게
	 *
	 *
	 * static Stack<Integer> stack = new Stack<>();
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
	 *         if("push".equals(order)){
	 *             stack.push(Integer.valueOf(inputData[1]));
	 *         }else if("pop".equals(order)){
	 *             result.append((stack.empty() ? "-1" : stack.pop()) + "\n");
	 *         }else if("size".equals(order)){
	 *             result.append(Integer.toString(stack.size()) + "\n");
	 *         }else if("empty".equals(order)){
	 *             result.append((stack.empty() ? "1" : "0") + "\n");
	 *         }else if("top".equals(order)){
	 *             result.append((stack.empty() ? "-1" : stack.peek()) + "\n");
	 *         }
	 *     }
	 */
}

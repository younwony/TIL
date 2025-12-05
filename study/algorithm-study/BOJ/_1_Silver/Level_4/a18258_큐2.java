package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class a18258_큐2 {

	static Queue<Integer> queue = new LinkedList<>();
	static StringBuilder result = new StringBuilder();
	static String[] inputData;
	static String back;
	public static void main(String[] args) {
          BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
 
          try {
              int n = Integer.valueOf(bufferedReader.readLine());
              for(int i = 0 ; i < n; i++){
                  inputData = bufferedReader.readLine().split(" ");
                  stackOrder(inputData[0]);
              }
              System.out.println(result.toString());
          }catch (Exception e){
              e.printStackTrace();
          }finally {
              try {
                  if(bufferedReader != null){bufferedReader.close();}
              }catch (Exception e2){
                  e2.printStackTrace();
              }
          }
      }
	 
      public static void stackOrder(String order){
          if("push".equals(order)){
              queue.offer(Integer.valueOf(inputData[1]));
              back = inputData[1];
          }else if("pop".equals(order)){
              result.append((queue.isEmpty() ? "-1" : queue.poll()) + "\n");
          }else if("size".equals(order)){
              result.append(Integer.toString(queue.size()) + "\n");
          }else if("empty".equals(order)){
              result.append((queue.isEmpty() ? "1" : "0") + "\n");
          }else if("front".equals(order)){
              result.append((queue.isEmpty() ? "-1" : queue.peek()) + "\n");
          }else if("back".equals(order)){
              if(queue.isEmpty()){
                  result.append("-1" + "\n");
              }else{
                  result.append(back + "\n");
              }
          }
      }
}

/**
 * @작성자 wony
 * @작성일 2019. 11. 22.
 * @사용처
 *
 */
package _1_Silver.Level_3;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 11. 22.
 * @사용처 프린터 큐
 * 
 */
public class a1966_프린터큐 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 22.
	 * @사용처 우선순위 큐(Priority Queue)
	 * @Todo
	 * 
	 * 현재 Queue로만 사용하여 설계
	 * 
	 * 우선순위 큐 사용해보기!
	 * 
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		 
		int testCount = scanner.nextInt();
		
		int documentSize, count, resultLocation;
		
		for(int i = 0; i< testCount; i++){
			Queue<Integer> queue = new LinkedList<Integer>();
			documentSize = scanner.nextInt();
			resultLocation = scanner.nextInt();
			for(int j = 0; j < documentSize; j++)
				queue.offer(scanner.nextInt());
			
			count = 0;
			
			while(resultLocation != -1){
				if(queueGreateThenData(queue)){ //현재 출력값보다 높은 우선순위가 있을경우
					if(resultLocation == 0) resultLocation += queue.size(); //현재 출력값이 출력하고자하는 값일 경우 사이즈만큼 뒤로보내기
					else{
						queue.offer(queue.poll());
						resultLocation--;
					}
				}else{ // 현재 출력값보다 높은 우선순위가 없을 경우
					count++;
					if(resultLocation == 0){
						System.out.println(count);
					}else{
						queue.poll();
					}
					resultLocation--;
				}
			}
			
		}
		scanner.close();
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 22.
	 * @사용처 queue내 현재 출력값보다 큰값 체크
	 * @Todo
	 * @param queue
	 * @return
	 * 
	 */
	public static boolean queueGreateThenData(Queue<Integer> queue){
		
		int data = queue.peek();
		
		while(data != 9){
			data++;
			if(queue.contains(data))
				return true;
		}
		
		return false;
	}

}

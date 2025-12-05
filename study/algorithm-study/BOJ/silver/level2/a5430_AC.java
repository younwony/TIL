/**
 * @작성자 wony
 * @작성일 2019. 11. 23.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 11. 23.
 * @사용처
 * @Todo
 */

public class a5430_AC {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 23.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCount = Integer.parseInt(scanner.nextLine());
		
		for(int i = 0; i < testCount; i++) {
			
			// 선언부 시작
			String[] order = scanner.nextLine().split("");
			int testDataCount = Integer.parseInt(scanner.nextLine());
			String data = scanner.nextLine();
			String[] dataArray = data.substring(1, data.length()-1).split(",");
			Deque<Integer> deque = new ArrayDeque<Integer>();
			boolean reverse = false;
			// 선언부 끝
			
			//Deque에 Data input
			for(int j = 0; j < testDataCount; j++)
				deque.offer(Integer.parseInt(dataArray[j]));
			
			//빈 Deque에 poll명령체크 하기위해 반복문 밖에 변수 선언
			int k = 0;
			int oL = order.length;
			for(k = 0; k < oL; k++) {
				if("R".equals(order[k])) reverse = !reverse;
				if("D".equals(order[k])) {
					if(deque.isEmpty())	break;
					if(reverse) deque.pollLast();
					else deque.poll();
				}
			}
			
			if(k < oL) System.out.println("error"); // 빈 Deque에 poll 명령 시 에러
			else {
				StringBuilder builder = new StringBuilder();
				String result;
				builder.append("[");
				while(!deque.isEmpty()) {
					if(reverse)	builder.append(deque.pollLast() + ",");
					else builder.append(deque.poll() + ",");
				}
				if(builder.length() > 1) result = builder.substring(0,builder.length()-1);
				else result = builder.toString();
				result += "]";
				System.out.println(result);
			}
		}
		
		scanner.close();
	}

}

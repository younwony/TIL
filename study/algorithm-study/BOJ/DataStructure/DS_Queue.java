/**
 * @작성자 wony
 * @작성일 2019. 11. 12.
 * @사용처
 *
 */
package DataStructure;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 11. 12.
 * @사용처 Queue
 * 
 */
public class DS_Queue {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 12.
	 * @사용처 Queue lib 사용
	 * @Todo
	 * 
	 * 직접 구현 - 10845
	 * 
	 * 1. Queue 에 데이터 삽입할때  add() 와  offer()가 있다.
	 * offer() 는 값의 입력에 대해 boolean 을 반환하지만 add()는 반환값이 없다.
	 * 해당기능은 Queue의 사이즈에 제한이 있을경우  필요하다.
	 * 
	 * 2. Queue 에서  헤더 값을 찾을 때 peek() 와 element()가 있다.
	 * peek() 큐가 비어있을 경우 false를 반환 하지만 element()는 NoSuchElementException 을 던진다.
	 * 기본적으로 Queue의 구현은 Null 요소 삽입을 허용하지 않는다.
	 * 
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCount = Integer.parseInt(scanner.nextLine());
		
		Queue<Integer> queue = new LinkedList<Integer>();
		String[] order;
		
		int lastData = -1;
		
		for(int i = 0; i< testCount; i++){
			
			order = scanner.nextLine().split(" ");
			
			if("push".equals(order[0])) {queue.offer(Integer.parseInt(order[1]));lastData = Integer.parseInt(order[1]);}
//			if("push".equals(order[0])) queue.add(Integer.parseInt(order[1])); 
			if("pop".equals(order[0])) if(queue.isEmpty()) System.out.println(-1); else System.out.println(queue.poll());
			if("size".equals(order[0])) System.out.println(queue.size());
			if("empty".equals(order[0])) if(queue.isEmpty()) System.out.println(1); else System.out.println(0);
			if("front".equals(order[0])) if(queue.isEmpty()) System.out.println(-1); else System.out.println(queue.peek());
			if("back".equals(order[0])) if(queue.isEmpty()) System.out.println(-1); else System.out.println(lastData);
		}
		
	}

}

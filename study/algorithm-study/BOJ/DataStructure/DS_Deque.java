/**
 * @작성자 wony
 * @작성일 2019. 11. 12.
 * @사용처
 *
 */
package DataStructure;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 11. 12.
 * @사용처 Deque
 * 
 */
public class DS_Deque {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 12.
	 * @사용처 Deque
	 * @Todo
	 * 
	 * 직접 구현 - 10866
	 * 
	 * 1. 추가
	 * Queue 형식 추가 : add = put = offer
	 * Stack 형식 추가 : push
	 * 
	 * 2. 추출
	 * Front 추출 : poll(), pollFirst(), pop()
	 * Back 추출 : pollLast()
	 * @param args
	 * 
	 * 3. 헤더
	 * Front 헤더  : peek(), peekFirst(), get(), getFirst()
	 * Back 헤더 : peekLast(), getLast()
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int testCount = scanner.nextInt();
		scanner.nextLine();
		
		Deque<Integer> deque = new ArrayDeque<Integer>();
		String[] order;
		
		for(int i = 0; i < testCount; i++){
			order = scanner.nextLine().split(" ");
			if("push_front".equals(order[0])) deque.offerFirst(Integer.parseInt(order[1]));
			if("push_back".equals(order[0])) deque.offerLast(Integer.parseInt(order[1]));
			if("pop_front".equals(order[0])) if(deque.isEmpty()) System.out.println(-1); else System.out.println(deque.poll());
			if("pop_back".equals(order[0])) if(deque.isEmpty()) System.out.println(-1); else System.out.println(deque.pollLast());
			if("size".equals(order[0])) System.out.println(deque.size());
			if("empty".equals(order[0])) if(deque.isEmpty()) System.out.println(1); else System.out.println(0);
			if("front".equals(order[0])) if(deque.isEmpty()) System.out.println(-1); else System.out.println(deque.peek());
			if("back".equals(order[0])) if(deque.isEmpty()) System.out.println(-1); else System.out.println(deque.peekLast());
		}
		
		scanner.close();
	}

}

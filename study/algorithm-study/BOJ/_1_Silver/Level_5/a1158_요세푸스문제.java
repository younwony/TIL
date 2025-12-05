/**
 * @작성자 wony
 * @작성일 2019. 11. 17.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

/**
 * @작성자 wony
 * @작성일 2019. 11. 17.
 * @사용처 조세퍼스 문제
 * @Todo
 */

public class a1158_요세푸스문제 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 17.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int N = scanner.nextInt(); // 인원
		
		int K = scanner.nextInt(); // K순서
		
		scanner.close();
		
		Deque<Integer> deque = new ArrayDeque<Integer>(); 
		
		for(int i = 1; i <= N; i++)
			deque.offer(i);
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("<");
		
		while(deque.size() != 0) {
			for(int i = 1; i < K; i++) 
				deque.offer(deque.poll());
			builder.append(deque.poll() + ", ");
		}
		
		builder.replace(builder.length()-2, builder.length(),"");
		
		builder.append(">");
		
		System.out.println(builder.toString());
		
	}

}

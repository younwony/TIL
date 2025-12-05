/**
 * @작성자 wony
 * @작성일 2020. 1. 19.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 19.
 * @사용처
 * @Todo
 */

public class a2161_카드1 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 19.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.close();
		Queue<Integer> queue = new LinkedList<Integer>();
		boolean nextCheck = true;
		
		for(int i = 1; i <= n; i++) {
			queue.offer(i);
		}
		
		while(!queue.isEmpty()) {
			if(nextCheck) {
				System.out.print(queue.poll() + " ");
			}else {
				queue.offer(queue.poll());
			}
			nextCheck = !nextCheck;
		}
		
	}

}

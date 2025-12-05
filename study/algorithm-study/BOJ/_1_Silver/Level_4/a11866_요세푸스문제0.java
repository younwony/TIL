/**
 * @작성자 wony
 * @작성일 2020. 3. 4.
 * @사용처
 *
 */
package _1_Silver.Level_4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 3. 4.
 * @사용처
 * 
 */
public class a11866_요세푸스문제0 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 3. 4.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		int k = scanner.nextInt();
		
		scanner.close();
		
		Queue<Integer> queue = new LinkedList<Integer>();
		
		for(int i = 1 ; i <= n; i++){
			queue.offer(i);
		}
		
		int i = 1;
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("<");
		while(!queue.isEmpty()){
			if(i%k == 0){
				builder.append(queue.poll()+", ");
			}else{
				queue.offer(queue.poll());
			}
			i++;
		}
		builder.delete(builder.lastIndexOf(","), builder.length());
		builder.append(">");
		
		System.out.println(builder.toString());
	}

}

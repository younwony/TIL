/**
 * @작성자 wony
 * @작성일 2019. 11. 16.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_3;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Stack;

/**
 * @작성자 wony
 * @작성일 2019. 11. 16.
 * @사용처 스택 수열
 * @Todo
 */

public class a1874_스택수열 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 16.
	 * @사용처 스택수열 순서대로
	 * @param args
	 * @Todo
	 * 
	 * 1. 1 ~ n 까지 수열이 가능한지 Stack으로 나열하기
	 * 
	 * 처음 로직은 문제의 이해를 완벽히 하지못해 시간초과가 나게 여러 중첩문을 사용했다.
	 * 
	 * 해당 문제를 풀기위해 중요한건 stack에서 버리는 pop이 있으면 안되는점을 캐치하니 쉽게 풀렸다.
	 * 
	 * 2. 처음에는 stack.contains 로 stack내부에 해당 data가 있는지를 체크해 데이터를 pop 할때 까지 while문을 돌렸다.
	 * 
	 * 해당로직으로 시간초과 발생을 알고 다시 문제를 확인하며 다른 풀이를 확인해 보니 peek로  stack에 output부분만을 확인하여 일치 시 꺼내는 좋은 로직을 익혀 해당방법으로 풀이를 진행헀다.
	 * 
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner= new Scanner(System.in);
		
		int testCount = scanner.nextInt();
		
		int[] dataArray = new int[testCount];
		
		for(int i = 0; i < testCount; i++)
			dataArray[i] = scanner.nextInt();
		
		scanner.close();
		
		Stack<Integer> stack = new Stack<Integer>();
		
		StringBuilder builder = new StringBuilder();
		
		int j = 0;
		
		for(int i = 1; i <= testCount; i++) {
			stack.push(i);
			builder.append("+\n");
			while(!stack.isEmpty() && stack.peek() == dataArray[j]) {
				stack.pop();
				builder.append("-\n");
				j++;
			}
		}
		
		if(!stack.isEmpty())
			System.out.println("NO");
		else
			System.out.println(builder.toString());
		
		
	}

}

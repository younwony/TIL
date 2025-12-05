/**
 * @작성자 wony
 * @작성일 2019. 11. 12.
 * @사용처
 *
 */
package DataStructure;

import java.util.Scanner;
import java.util.Stack;

/**
 * @작성자 wony
 * @작성일 2019. 11. 12.
 * @사용처 Stack
 * 
 */
public class DS_Stack {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 12.
	 * @사용처 Stack lib 사용
	 * @Todo
	 * 
	 * 직접구현 - 10828
	 * 
	 * 1. 스택의 가장 최근에 들어온 아이템을 확인하기 위해 lastElement() 함수를 사용했다.
	 * stack 클래스에서 제공하는 peek() 역시 마지막 아이템을 가르키기에  peek() 와 lastElement()는 같은 역할을 하는 것을 확인했다.
	 * 차이는 peek()는 Stack.class 내에서 size()함수를 한번 호출하기에 시간과메모리적으로 좀더 손해를 보았다.
	 * 반면  lastElement()는 vector.class 내에서 지정된 StackCount 전역 변수에서 받아오기에 메모리와 시간적으로 효율성을 가지게 되었다. 
	 * 
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCount = Integer.parseInt(scanner.nextLine());
		
		String[] order;
		
		Stack<Integer> stack = new Stack<Integer>();
		
		for(int i = 0; i < testCount; i++){
			order = scanner.nextLine().split(" ");
			switch (order[0]) {
			case "push" : stack.push(Integer.parseInt(order[1])); break;
//			case "top"  : if(stack.empty()) System.out.println(-1); else System.out.println(stack.peek()); break;
			case "top"  : if(stack.empty()) System.out.println(-1); else System.out.println(stack.lastElement()); break;
			case "pop"  : if(stack.empty()) System.out.println(-1); else System.out.println(stack.pop()); break;
			case "size" : System.out.println(stack.size()); break;
			case "empty": if(stack.empty()) System.out.println(1); else System.out.println(0);
			}
		}
		scanner.close();
	}

}

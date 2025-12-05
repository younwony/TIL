package Kakao.Problem_2019_Intern;

import java.util.Stack;

public class 키패드_누르기 {
public static void main(String[] args) {
		
//		int[] numbers = new int[]{1, 3, 4, 5, 8, 2, 1, 4, 5, 9, 5};
		int[] numbers = new int[]{7, 0, 8, 2, 8, 3, 1, 5, 7, 6, 2};
//		String hand = "right";
		String hand = "left";
		
        System.out.println(solution(numbers, hand));

    }

	 public static String solution(int[] numbers, String hand) {
	        String answer = "";
	        
	        int[][] phone = new int[][]{{3,1},{0,0},{0,1},{0,2},{1,0},{1,1},{1,2},{2,0},{2,1},{2,2},{3,0},{3,2}};
	        
	        int[] leftPoint = phone[10];
	        int[] rightPoint = phone[11];
	        int leftSize,rightSize;
	        
	        for(int number : numbers){
	        	if(number == 1 || number == 4 || number == 7){
	        		answer += "L";
	        		leftPoint = phone[number];
	        	}else if(number == 3 || number == 6 || number == 9){
	        		answer += "R";
	        		rightPoint = phone[number];
	        	}else{
	        		leftSize = pointLength(phone[number], leftPoint);
	        		rightSize = pointLength(phone[number], rightPoint);
	        		if("right".equals(hand)){
	        			if(leftSize < rightSize){
	        				answer += "L";
	        				leftPoint = phone[number];
	        			}else{
	        				answer += "R";
	    	        		rightPoint = phone[number];
	        			}
	        		}else{
	        			if(rightSize < leftSize){
	        				answer += "R";
	        				rightPoint = phone[number];
	        			}else{
	        				answer += "L";
	        				leftPoint = phone[number];
	        			}
	        		}
	        	}
	        }
	        return answer;
    }
	 
	public static int pointLength(int[] phone, int[] hand){
		return Math.abs(phone[0] - hand[0]) + Math.abs(phone[1] - hand[1]);
	}
}

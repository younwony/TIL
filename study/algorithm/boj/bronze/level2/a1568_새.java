package _0_Bronze.Level_2;

import java.util.Scanner;

public class a1568_새 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		int count = 0;
		int modn;
		
		int sum = 0;
		while(true){
			count++;
			modn = sigma(n, count);
			
			if(modn == 0){
				sum+=count;
				break;
			}else if(modn < 0){
				sum+=count-1;
				n = sigma(n, count-1);
				count = 0;
			}
		}
		
		System.out.println(sum);
	}
	
	public static int sigma(int n , int count){
		
		int sum = (count*(count+1))/2;
		return n - sum; 
	}

}

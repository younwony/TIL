/**
 * @작성자 wony
 * @작성일 2019. 10. 8.
 * @사용처
 * TODO
 */
package _0_Bronze.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 10. 8.
 * @사용처
 * TODO
 */
public class a4344_평균은넘겠지 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 8.
	 * @사용처 평균 이상 비율 구하기.
	 * @Todo 주석은 nextline처리, 현재 로직은 nextInt처리 : nextLine 로직이 더빠르며 메모리가 int보다 50%가량밖에 들지않는다.
	 * @param args
	 * TODO
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCaseCount = Integer.parseInt(scanner.nextLine()); 
		int sum,count,inputCaseCount;
		double avg;
		int[] inputCaseArrays = null;
		for(int i=0; i < testCaseCount; i++){
			inputCaseCount = scanner.nextInt();
			inputCaseArrays = new int[inputCaseCount];
			sum = 0;
			avg = 0;
			count =0;
			for(int j=0; j< inputCaseCount; j++){
				inputCaseArrays[j] = scanner.nextInt(); 
				sum += inputCaseArrays[j];
			}
			avg = (double)sum/inputCaseCount;
			for(int k=0; k< inputCaseCount; k++){
				if(Double.valueOf(inputCaseArrays[k]) > avg)
					count++;
			}
			System.out.println(String.format("%.3f%%", (double)count/inputCaseCount*100));
		}
		scanner.close();
		
/*Scanner scanner = new Scanner(System.in);
		
		int testCaseCount = Integer.parseInt(scanner.nextLine());
		int sum,count;
		double avg;
		String[] inputCaseArrays = null;
		for(int i=0; i < testCaseCount; i++){
			inputCaseArrays = scanner.nextLine().split(" ");
			sum = 0;
			avg = 0;
			count =0;
			for(int j=1; j<inputCaseArrays.length ; j++){
				sum += Integer.parseInt(inputCaseArrays[j]);
			}
			avg = (double)sum/(inputCaseArrays.length-1);
			for(int k=1; k< inputCaseArrays.length; k++){
				if(Double.valueOf(inputCaseArrays[k]) > avg)
					count++;
			}
			System.out.println(String.format("%.3f%%", (double)count/(inputCaseArrays.length-1)*100));
		}
		scanner.close();*/
	}

}

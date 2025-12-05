/**
 * @작성자 wony
 * @작성일 2019. 12. 18.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 18.
 * @사용처
 * 
 */
public class a2941_크로아티아알파벳 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 18.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		String[] croatiaLang = {"c=","c-","dz=","d-","lj","nj","s=","z="};
		String inputData = scanner.nextLine();
		
		scanner.close();
		int count = 0;
		while(!"".equals(inputData)){
			for(int i = 0; i < croatiaLang.length; i++){
				while(inputData.indexOf(croatiaLang[i]) >= 0){
					inputData = inputData.replaceFirst(croatiaLang[i], ",");
					count++;
				}
			}
			inputData = inputData.replace(",","");
			count += inputData.length();
			break;
		}
		System.out.println(count);
	}

}

/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 *
 */
package _0_Bronze.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 * 
 */
public class a4153_직각삼각형 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 26.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int x,y,z,max,temp;
		while(true){
			x = scanner.nextInt();
			y = scanner.nextInt();
			z = scanner.nextInt();
			if(x == 0 && y == 0 && z == 0){break;}
			max = Math.max(Math.max(x, y),z);
			if(max == x){
				temp = x;
				x = z;
				z = temp;
			}else if(max == y){
				temp = y;
				y = z;
				z = temp;
			}
			if(Math.pow(x, 2) + Math.pow(y, 2) == Math.pow(z, 2)){System.out.println("right");}
			else{System.out.println("wrong");}
		}
		scanner.close();
	}

}

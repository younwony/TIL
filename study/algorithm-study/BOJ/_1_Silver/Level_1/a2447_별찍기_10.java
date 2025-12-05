/**
 * @작성자 wony
 * @작성일 2019. 10. 20.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 10. 20.
 * @사용처
 * @Todo
 */

public class a2447_별찍기_10 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 20.
	 * @사용처 별찍기(분할정복 알고리즘) 재귀형식, 공부더필요
	 * @param args
	 * @Todo
	 */

	 static char a[][];
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
        
        int n = sc.nextInt();
        
        a = new char[n][n];
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                a[i][j] = '*';
            }
        }
        
        recursive(0, 0, n);
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                sb.append(a[i][j]);
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }
    
    public static void recursive(int s, int e, int len) {
        if(len < 3) return;
        int m = len/3;
        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                if(i == 1 && j == 1) fillBlank(s + m, e + m, m);
                else recursive(s + m*i, e + m*j, m);
            }
        }
    }
    
    public static void fillBlank(int s, int e, int len) {
        for(int i=s; i<s+len; i++) {
            for(int j=e; j<e+len; j++) {
                a[i][j] = ' ';
            }
        }
    }

}

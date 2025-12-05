/**
 * @작성자 wony
 * @작성일 2020. 1. 8.
 * @사용처
 *
 */
package _2_Gold.Level_5;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 8.
 * @사용처
 * 
 */
public class a15686_치킨배달 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 8.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	
	static ArrayList<int[]> arrayList  = new ArrayList<int[]>();
	static ArrayList<int[]> filedList = new ArrayList<int[]>();
	static int[][] filed;
	static boolean[] visitied;
	static int m = 0;
	static int chickenlength = 2500;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		m = scanner.nextInt();
		filed = new int[n][n];
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				filed[i][j] = scanner.nextInt();
				if(filed[i][j] == 2){arrayList.add(new int[]{i,j});}
			}
		}
		scanner.close();
		visitied = new boolean[arrayList.size()];
		
		dfs(0, 0);
		
		System.out.println(chickenlength);
		
	}
	
	public static void dfs(int depth, int index){
		if(depth == m){
			filedList.clear();
			for(int i = 0; i < visitied.length; i++){
				if(visitied[i]){filedList.add(arrayList.get(i));}
			}
			solvedChickenLength();
		}else{
			for(int i = index; i < arrayList.size(); i++){
				if(visitied[i]){continue;}
				else{
					visitied[i] = true;
					dfs(depth+1, i+1);
					visitied[i] = false;
				}
			}
		}
	}
	
	public static void solvedChickenLength(){
		int totalLength = 0;
		int length;
		for (int i = 0; i < filed.length; i++) {
			for (int j = 0; j < filed.length; j++) {
				if(filed[i][j] == 1){
					length = (int)Math.pow(filed.length, 2);
					for (int j2 = 0; j2 < filedList.size(); j2++) {
						length = Math.min(length, (Math.abs(i-filedList.get(j2)[0]) + Math.abs(j-filedList.get(j2)[1])));
					}
					totalLength += length;
				}
			}
		}
		
		chickenlength = Math.min(chickenlength, totalLength);
	}

}

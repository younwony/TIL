package _1_Silver.Level_1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class a7569_토마토 {

	static int[][][] field;
	static int[] hArray = {-1, 1, 0, 0, 0, 0};
	static int[] nArray = {0, 0, -1, 1, 0, 0};
	static int[] mArray = {0, 0, 0, 0, -1, 1};
	
	public static void main(String[] args){
		// TODO Auto-generated method stub
		
		Scanner scanner = new Scanner(System.in);
		int m = scanner.nextInt();
		int n = scanner.nextInt();
		int h = scanner.nextInt();
		
		field = new int[h][n][m];
		
		Queue<tomato> queue = new LinkedList<>();
		
		for(int i = 0 ; i < h; i++){
			for(int j = 0; j < n; j++){
				for(int k = 0; k < m; k++){
					field[i][j][k] = scanner.nextInt(); 
					if(field[i][j][k] == 1){
						queue.offer(new tomato(i, j, k, 1));
					}
				}
			}
		}
		
		int result = bfs(queue);
		
		start:
		for(int i = 0 ; i < h; i++){
			for(int j = 0; j < n; j++){
				for(int k = 0; k < m; k++){
					if(field[i][j][k] == 0){
						result = -1;
						break start;
					}
				}
			}
		}
		
		System.out.println(result);
		
	}
	
	public static int bfs(Queue<tomato> queue){
		
		int result = 0;
		
		while(!queue.isEmpty()){
			tomato tomato = queue.poll();
			int h = tomato.h;
			int n = tomato.n;
			int m = tomato.m;
			int cnt = tomato.cnt;
			
			for(int i = 0; i < 6; i++){
				
				if(isFiled(h + hArray[i], n + nArray[i], m + mArray[i])){
					if(field[h+hArray[i]][n+nArray[i]][m+mArray[i]] == 0){
						field[h+hArray[i]][n+nArray[i]][m+mArray[i]] = 1;
						result = cnt + 1;
						queue.offer(new tomato(h + hArray[i], n + nArray[i], m + mArray[i], result));
					}
				}
			
			}
		}
		
		return result == 0 ? 0 : result-1;
	}
	
	public static boolean isFiled(int h, int n, int m){
		return h >= 0 && h < field.length && n >= 0 && n < field[0].length && m >= 0 && m <field[0][0].length; 
	}

	public static class tomato{
		int h;
		int n;
		int m;
		int cnt;
		
		public tomato(int h, int n, int m, int cnt) {
			super();
			this.h = h;
			this.n = n;
			this.m = m;
			this.cnt = cnt;
		}
		
		
	}
}

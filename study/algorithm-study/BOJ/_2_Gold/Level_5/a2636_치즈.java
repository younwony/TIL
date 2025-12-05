package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class a2636_치즈 {

	static int[][] field;
	static boolean[][] visited;
	static int[] xArray = {-1,1,0,0};
	static int[] yArray = {0,0,-1,1};
	static int resultCnt = 0;
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		String[] xy = bufferedReader.readLine().split(" ");
		int x = Integer.valueOf(xy[0]);
		int y = Integer.valueOf(xy[1]);
		
		field = new int[x][y];
		String[] xInput;
		for(int i = 0; i < x; i++){
			xInput = bufferedReader.readLine().split(" ");
			for(int j = 0; j < y; j++){
				field[i][j] = Integer.valueOf(xInput[j]);
			}
		}
		
		int bfsCnt = 0;
		int temp = -1;
		
		int result = 0;
		while(temp != 0){
			visited = new boolean[x][y];

			go:
			for(int i =0 ; i < x; i++){
				for(int j = 0; j < y; j++){
					if(field[i][j] == 0 && !visited[i][j]){
						temp = bfs(i, j);
						if(temp != 0) bfsCnt = temp;
						break go;
					}
				}
			}
			result++;
		}
		System.out.println(result-1);
		System.out.println(bfsCnt);
	}
	
	public static int bfs(int x, int y){
		
		int bfsCnt = 0;
		
		Queue<point> queue = new LinkedList<point>();
		
		queue.offer(new point(x, y));
		field[x][y]--;
		visited[x][y] = true;

		while(!queue.isEmpty()){
			point point = queue.poll();
			
			for(int i = 0; i < 4; i++){
				int xn = point.x + xArray[i];
				int yn = point.y + yArray[i];
				if(xn < 0 || xn >= field.length || yn < 0 || yn >= field[0].length) continue;
				if(visited[xn][yn]) continue;
				if(field[xn][yn] <= 0){
					queue.offer(new point(xn, yn));
					visited[xn][yn] = true;
					field[xn][yn]--;
				}else if(field[xn][yn] == 1) {
					bfsCnt++;
					field[xn][yn]--;
					visited[xn][yn] = true;
				}
			}
		}
		
		return bfsCnt;
		
	}
	
	public static class point{
		int x;
		int y;
		public point(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		
	}

}

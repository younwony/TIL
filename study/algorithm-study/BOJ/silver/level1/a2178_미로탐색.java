package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class a2178_미로탐색 {

	static char[][] field;
	static int cntMin = Integer.MAX_VALUE;
	static boolean[][] visited;
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String[] inputXY = bufferedReader.readLine().split(" ");
		int x = Integer.valueOf(inputXY[0]);
		int y = Integer.valueOf(inputXY[1]);
		
		field = new char[x][y];
		visited = new boolean[x][y];
		for(int i = 0; i < x; i++){
			field[i] = bufferedReader.readLine().toCharArray();
		}
		
		dfs(0,0,1);
		
		System.out.println(cntMin);
	}
	
	public static void dfs(int x, int y, int cnt){
		
		if(visited[visited.length - 1][visited[0].length - 1]){
			if(cnt < cntMin){
				cntMin = cnt;
			}
			return ;
		}
		
		if(isField(x+1, y)){
			if(!visited[x+1][y] && field[x+1][y] == '1'){
				visited[x+1][y] = true;
				dfs(x+1, y, cnt+1);
				visited[x+1][y] = false;
			}
		}
		if(isField(x, y+1)){
			if(!visited[x][y+1] && field[x][y+1] == '1'){
				visited[x][y+1] = true;
				dfs(x, y+1, cnt+1);
				visited[x][y+1] = false;
			}
		}
		if(isField(x-1, y)){
			if(!visited[x-1][y] && field[x-1][y] == '1'){
				visited[x-1][y] = true;
				dfs(x-1, y, cnt+1);
				visited[x-1][y] = false;
			}
		}
		if(isField(x, y-1)){
			if(!visited[x][y-1] && field[x][y-1] == '1'){
				visited[x][y-1] = true;
				dfs(x, y-1, cnt+1);
				visited[x][y-1] = false;
			}
		}
	}
	
	public static boolean isField(int x, int y){
		return (x >= 0 && x  < field.length) && (y >= 0 && y  < field[0].length); 
	}
	
	
	/**
	 * 
	 *  4ms 빨라진 로직..
	 * 
	 public static void main(String[] args) throws Exception{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		String[] xy = bufferedReader.readLine().split(" ");
		int x = Integer.valueOf(xy[0]);
		int y = Integer.valueOf(xy[1]);
		
		char[][] field = new char[x][y];
		
		for(int i = 0; i < x; i++){
			field[i] = bufferedReader.readLine().toCharArray();
		}
		
		Queue<point> queue = new LinkedList<point>();
		
		queue.offer(new point(0, 0, 1));
		field[0][0] = '0'; 
		
		int result = 1;
		
		int pointX, pointY, pointCount;
		
		while(!queue.isEmpty()){
			point point = queue.poll();
			pointX = point.x;
			pointY = point.y;
			pointCount = point.count;
			
			if(pointX == x-1 && pointY == y-1){
				result = pointCount;
				break;
			}
			
			if(pointX - 1 >= 0)
				if(field[pointX - 1][pointY] == '1'){
					queue.offer(new point(pointX - 1, pointY, pointCount + 1));
					field[pointX - 1][pointY] = '0';
				}
			if(pointY - 1 >= 0)
				if(field[pointX][pointY - 1] == '1'){
					queue.offer(new point(pointX, pointY - 1, pointCount + 1));
					field[pointX][pointY - 1] = '0';
				}
			if(pointX + 1 < x)
				if(field[pointX + 1][pointY] == '1'){
					queue.offer(new point(pointX + 1, pointY, pointCount + 1));
					field[pointX + 1][pointY] = '0';
				}
			if(pointY + 1 < y)
				if(field[pointX][pointY + 1] == '1'){
					queue.offer(new point(pointX, pointY + 1, pointCount + 1));
					field[pointX][pointY + 1] = '0';
				}
			
		}
		
		System.out.println(result);
	}
	
	static class point{
		int x;
		int y;
		int count;
		
		public point(int x, int y, int count) {
			super();
			this.x = x;
			this.y = y;
			this.count = count;
		}
	}
	
	 */

}

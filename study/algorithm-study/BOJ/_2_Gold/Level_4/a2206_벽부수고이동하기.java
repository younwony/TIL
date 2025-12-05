import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class a2206_벽부수고이동하기 {

	static char[][] field;
	static int[] xArray = {1,0,-1,0};
	static int[] yArray = {0,1,0,-1};
	static int result = Integer.MAX_VALUE;
	static boolean[][][] visited; //0 = isBraak(true) 1 = isBreak(false);
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String[] nm = bufferedReader.readLine().split(" ");
		int n = Integer.valueOf(nm[0]);
		int m = Integer.valueOf(nm[1]);
		
		field = new char[n][m];
		
		for(int i = 0 ; i< n; i++){
			field[i] = bufferedReader.readLine().toCharArray();
		}
		
		visited = new boolean[n][m][2];
		
		bfs();
		
		System.out.println(result == Integer.MAX_VALUE ? -1 : result);
		
	}
	
	public static void bfs(){
		Queue<node> queue = new LinkedList<>();
		
		queue.offer(new node(0, 0, 1, false));
		visited[0][0][0] = true;
		visited[0][0][1] = true;
		int x,y,cnt;
		boolean isBreak;
		while(!queue.isEmpty()){
			node node = queue.poll();
			x = node.x;
			y = node.y;
			cnt = node.cnt;
			isBreak = node.isBreak;
			
			if(x == field.length-1 && y == field[0].length - 1){
				if(cnt < result){
					result = cnt;
				}
			}
			
			for(int i = 0 ; i < 4; i++){
				int xn = x + xArray[i];
				int yn = y + yArray[i];
				if(isFiled(xn, yn, isBreak)){
					if(field[xn][yn] == '1'){
						if(!isBreak && !visited[xn][yn][1]){
							queue.offer(new node(xn, yn, cnt + 1, true));
							visited[xn][yn][1] = true;
						}
					}else{
						if(isBreak){
							if(!visited[xn][yn][1]){
								queue.offer(new node(xn, yn, cnt + 1, isBreak));
								visited[xn][yn][1] = true;
							}
						}else{
							if(!visited[xn][yn][0]){
								queue.offer(new node(xn, yn, cnt + 1, isBreak));
								visited[xn][yn][0] = true;
							}
						}
					}
				}
			}
		}
	}
	
	public static boolean isFiled(int x, int y, boolean isBreak){
		boolean result = true;
		
		if(x < 0 || x >= field.length || y < 0 || y >= field[0].length){
			result = false;
		}else{
			if(isBreak){
				if(field[x][y] == '1'){
					result = false;
				}
			}
		}
		
		return result;
		
	}
	
	public static class node{
		int x;
		int y;
		int cnt;
		boolean isBreak;
		
		public node(int x, int y, int cnt, boolean isBreak) {
			super();
			this.x = x;
			this.y = y;
			this.cnt = cnt;
			this.isBreak = isBreak;
		}
	}
}

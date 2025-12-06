package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;

public class a4963_섬의개수 {

	static int[] xArray = {-1,-1,-1,0,0,1,1,1};
	static int[] yArray = {-1,0,1,-1,1,-1,0,1};
	static int[][] field;
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder builder = new StringBuilder();
		
		while(true){
			String[] inputXY = bufferedReader.readLine().split(" ");
			int x = Integer.valueOf(inputXY[1]);
			int y = Integer.valueOf(inputXY[0]);
			int cnt = 0;
			
			if(x == 0 && y == 0){break;}
			
			field = new int[x][y];
			
			for(int i = 0; i < x; i++){
				String[] yField = bufferedReader.readLine().split(" ");
				for(int j = 0; j < y; j++){
					field[i][j] = Integer.valueOf(yField[j]);
				}
			}
			
			for(int i = 0; i < x; i++){
				for(int j = 0; j <y; j++){
					if(field[i][j] == 1){
						cnt++;
						bfs(i, j);
					}
				}
			}
			
			builder.append(cnt + "\n");
			
		}
		
		
		System.out.println(builder.toString());
	}
	
	public static void bfs(int x, int y){
		field[x][y] = 0;
		for(int i =0 ; i < 8; i++){
			int xTemp = x + xArray[i];
			int yTemp = y + yArray[i];
			if(isField(xTemp, yTemp)){
				if(field[xTemp][yTemp] == 1){
					bfs(xTemp, yTemp);
				}
			}
		}
	}
	
	public static boolean isField(int x, int y){
		return (x >= 0 && x < field.length) && (y >= 0 && y < field[0].length);
	}

}

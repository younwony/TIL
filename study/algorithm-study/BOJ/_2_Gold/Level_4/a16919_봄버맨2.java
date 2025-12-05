package _2_Gold.Level_4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class a16919_봄버맨2 {

	static char[][] field;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			
			String[] inputNumberData = bufferedReader.readLine().split(" ");
			StringBuilder resultOne = new StringBuilder();
			StringBuilder resultTwo = new StringBuilder();
			StringBuilder resultThree = new StringBuilder();
			StringBuilder resultFour = new StringBuilder();
			
			int r = Integer.valueOf(inputNumberData[0]);
			int c = Integer.valueOf(inputNumberData[1]);
			long n = Long.valueOf(inputNumberData[2]);
			
			field = new char[r][c];
			
			for(int i = 0; i < r; i++){
				field[i] = bufferedReader.readLine().toCharArray();
			}
			
			ArrayList<int[]> boomPoint = new ArrayList<>();
			
			for(int i = 0; i < r; i++){
				for(int j = 0; j < c; j++){
					resultTwo.append('O');
					resultOne.append(field[i][j]);
					if(field[i][j] == 'O'){
						boomPoint.add(new int[]{i,j});
						boomPoint.add(new int[]{i-1,j});
						boomPoint.add(new int[]{i+1,j});
						boomPoint.add(new int[]{i,j-1});
						boomPoint.add(new int[]{i,j+1});
					}
					field[i][j] = 'O';
				}
				resultTwo.append("\n");
				resultOne.append("\n");
			}
			
			fieldBoom(boomPoint);
			
			for(int i = 0; i < r; i++){
				for(int j = 0; j < c; j++){
					resultThree.append(field[i][j]);
				}
				resultThree.append("\n");
			}
			
			boomPoint.clear();
			
			for(int i = 0; i < r; i++){
				for(int j = 0; j < c; j++){
					if(field[i][j] == 'O'){
						boomPoint.add(new int[]{i,j});
						boomPoint.add(new int[]{i-1,j});
						boomPoint.add(new int[]{i+1,j});
						boomPoint.add(new int[]{i,j-1});
						boomPoint.add(new int[]{i,j+1});
					}
					field[i][j] = 'O';
				}
			}
			
			fieldBoom(boomPoint);
			
			for(int i = 0; i < r; i++){
				for(int j = 0; j < c; j++){
					resultFour.append(field[i][j]);
				}
				resultFour.append("\n");
			}
			
			if(n < 2){
				System.out.println(resultOne.toString());
			}else if(n % 2 == 0){
				System.out.println(resultTwo.toString());
			}else if(n % 4 == 1){
				System.out.println(resultFour.toString());
			}else{
				System.out.println(resultThree.toString());
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if(bufferedReader != null){ bufferedReader.close();}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
	
	public static void fieldBoom(ArrayList<int[]> boomPoint){
		int x;
		int y;
		for(int i = 0; i< boomPoint.size(); i++){
			x = boomPoint.get(i)[0];
			y = boomPoint.get(i)[1];
			if(isField(x, y)){field[x][y] = '.';}
		}
	}
	
	public static boolean isField(int x, int y){
		
		return x >= 0 && y >= 00 && x < field.length && y < field[0].length;
	}
}

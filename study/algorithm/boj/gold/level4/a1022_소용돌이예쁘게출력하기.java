package _2_Gold.Level_4;

import java.util.Scanner;

public class a1022_소용돌이예쁘게출력하기 {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner scanner = new Scanner(System.in);
		
		int x1 = scanner.nextInt();
		int y1 = scanner.nextInt();
		int x2 = scanner.nextInt();
		int y2 = scanner.nextInt();
		
		scanner.close();
		
		int arrayXSize = x2 - x1 + 1;
		int arrayYSize = y2 - y1 + 1;
		
		int[][] resultArray = new int[arrayXSize][arrayYSize];
		
		StringBuilder result = new StringBuilder();
		
		int indexX = 0;
		int maxNum = 0;
		for(int i = x1; i <= x2; i++){
			int indexY = 0;
			for(int j = y1; j <= y2; j++){
				resultArray[indexX][indexY] = setFiledNumber(i, j);
				if(resultArray[indexX][indexY] > maxNum){
					maxNum = resultArray[indexX][indexY];
				}
				indexY++;
			}
			indexX++;
		}
		
		
		int maxNumberLength = String.valueOf(maxNum).length() + 1;
		
		
		for(int i = 0; i < arrayXSize; i++){
			for(int j = 0; j < arrayYSize; j++){
				result.append(String.format("%"+maxNumberLength + "s", resultArray[i][j]));
			}
			result.append("\n");
		}
		
		System.out.println(result.toString());
	}
	
	public static int setFiledNumber(int x, int y){
		
		int result = 0;
		
		int tempX = Math.abs(x);
		int tempY = Math.abs(y);
		
		if(x >= 0 && y >= 0){
			if(x >= y){
				result = (int)Math.pow((2*x + 1), 2) - (x - y);
			}else{
				result = (int)Math.pow((2*(y-1) + 1), 2) + (y - x);
			}
		}else if(x >= 0 && y < 0){
			if(x >= tempY){
				result = (int)Math.pow((2*x + 1), 2) - x - tempY;
			}else{
				result = (int)Math.pow((2*tempY + 1), 2) - 3*tempY + x;
			}
		}else if(x < 0 && y >= 0){
			if(tempX >= y){
				result = (int)Math.pow((2*(tempX - 1) + 1), 2) + 3*tempX - y; 
			}else{
				result = (int)Math.pow((2*(y - 1) + 1), 2) + y + tempX;
			}
		}else if(x < 0 && y < 0){
			if(tempX >= tempY){
				result = (int)Math.pow((2*tempX + 1), 2) - 5*tempX + tempY;
			}else{
				result = (int)Math.pow((2*tempY + 1), 2) - 3*tempY - tempX;
			}
		}
		
		return result;
	}

}

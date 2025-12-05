package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a1913_달팽이 {

	private static final int[] X_ARRAY = {1, 0, -1, 0};
	private static final int[] Y_ARRAY = {0, 1, 0, -1};
	private static int n;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

		n = Integer.parseInt(bufferedReader.readLine());

		int findNumber = Integer.parseInt(bufferedReader.readLine());

		int[][] snail = new int[n][n];

		int cnt = n*n;
		int turnCnt = 0;
		int x = 0;
		int y = 0;

		int resultX = 0;
		int resultY = 0;
		while(x != n/2 || y != n/2){
			/* 찾는 숫자일 경우 x,y 좌표 저장*/
			if(cnt == findNumber){
				resultX = x;
				resultY = y;
			}

			snail[x][y] = cnt;

			/* 필드를 벗어 나거나 값이 0 이 아닌 경우 회전 값 변경 후 넘기기*/
			int nextX = x + X_ARRAY[turnCnt];
			int nextY = y + Y_ARRAY[turnCnt];
			if(isNonField(nextX,nextY) || snail[nextX][nextY] != 0){
				turnCnt = (turnCnt + 1) % 4;
			}else{
				cnt--;
				x = nextX;
				y = nextY;
			}
		}

		snail[x][y] = 1;
		if(findNumber == 1){
			resultX = x;
			resultY = y;
		}

		StringBuilder result = new StringBuilder();

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				result
						.append(snail[i][j])
						.append(" ");
			}
			result.append("\n");
		}

		result
				.append(resultX + 1)
				.append(" ")
				.append(resultY + 1);

		System.out.println(result.toString());

	}

	private static boolean isNonField(int x, int y) {
		return x < 0 || y < 0 || x == n || y == n;
	}

}

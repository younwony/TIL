package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a9465_스티커 {

	public static void main(String[] args) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

		int T = Integer.parseInt(bufferedReader.readLine());

		StringTokenizer stringTokenizer;
		StringBuilder result = new StringBuilder();
		while(T-- > 0){
			int n = Integer.parseInt(bufferedReader.readLine());
			int[][] sticker = new int[2][n + 2];
			for(int i = 0; i < 2; i++){
				stringTokenizer = new StringTokenizer(bufferedReader.readLine());
				for(int j = 2; j < n + 2; j++){
					sticker[i][j] = Integer.parseInt(stringTokenizer.nextToken());
				}
			}

			for(int j = 2; j < n + 2; j++){
				int preTwo = Math.max(sticker[0][j - 2], sticker[1][j - 2]);
				sticker[0][j] += Math.max(sticker[1][j-1], preTwo);
				sticker[1][j] += Math.max(sticker[0][j-1], preTwo);
			}

			result
					.append(
							Math.max(sticker[0][n],
									Math.max(sticker[0][n+1],
											Math.max(sticker[1][n], sticker[1][n+1]))))
					.append("\n");
		}

		System.out.println(result.toString());
	}

}

package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a16439_치킨치킨치킨 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int n = Integer.parseInt(stringTokenizer.nextToken());
        int m = Integer.parseInt(stringTokenizer.nextToken());

        int[][] personAndChicken = new int[n][m];

        for (int i = 0; i < n; i++) {
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            for (int j = 0; j < m; j++) {
                personAndChicken[i][j] = Integer.parseInt(stringTokenizer.nextToken());
            }
        }

        int max = 0;

        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                for (int k = j + 1; k < m; k++) {

                    int sum = 0;

                    for(int person = 0; person < n; person++){
                        sum += Math.max(personAndChicken[person][i], Math.max(personAndChicken[person][j], personAndChicken[person][k]));
                    }

                    max = Math.max(max, sum);

                }
            }
        }

        System.out.println(max);

    }
}

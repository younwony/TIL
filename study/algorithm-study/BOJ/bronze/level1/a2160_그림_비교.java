package _0_Bronze.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a2160_그림_비교 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bufferedReader.readLine());
        String[][][] field = new String[N][5][7];

        for(int i = 0; i < N; i++){
            for (int j = 0; j < 5; j++) {
                field[i][j] = bufferedReader.readLine().split("");
            }
        }

        int a = 0;
        int b = 0;
        int min = Integer.MAX_VALUE;

        for(int i = 0; i < N; i++){
            for (int j = i+1; j < N; j++) {

                int cnt = 0;

                for (int k = 0; k < 5; k++) {
                    for (int l = 0; l < 7; l++) {
                        if(!field[i][k][l].equals(field[j][k][l])){
                            cnt++;
                        }
                    }
                }

                if(cnt < min){
                    a = i+1;
                    b = j+1;
                    min = cnt;
                }

            }
        }

        System.out.println(a + " " + b);
    }

}

package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a14594_동방_프로젝트_Small {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bufferedReader.readLine());
        int M = Integer.parseInt(bufferedReader.readLine());

        int[] walls = new int[N + 1];

        StringTokenizer stringTokenizer;
        for(int i = 0; i < M; i++){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            int x = Integer.parseInt(stringTokenizer.nextToken());
            int y = Integer.parseInt(stringTokenizer.nextToken());

            for(int j = x; j < y; j++){
                walls[j] = 1;
            }
        }

        int cnt = 0;

        for (int i = 0; i < walls.length; i++) {
            cnt++;
            for(int j = i + 1; j < walls.length; j++, i++){
                if(walls[j] == 0){
                    break;
                }
            }
        }

        System.out.println(cnt);

    }
}

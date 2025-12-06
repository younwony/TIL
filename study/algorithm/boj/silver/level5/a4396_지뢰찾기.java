package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a4396_지뢰찾기 {

    private static int[] xArray = new int[]{-1,-1,-1,0,0,0,1,1,1};
    private static int[] yArray = new int[]{-1,0,1,-1,0,1,-1,0,1};
    private static char[][] boom;
    private static char[][] input;
    private static int n;

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(bufferedReader.readLine());
        boom = new char[n][n];
        input = new char[n][n];

        for(int i = 0; i < n; i ++){
            boom[i] = bufferedReader.readLine().toCharArray();
        }

        /* 열려 있는 칸이 지뢰일 경우를 체크할 변수 */
        boolean isBoom = false;
        for(int i = 0; i < n; i ++){
            input[i] = bufferedReader.readLine().toCharArray();
            for (int j = 0; j < n; j++) {
                /* 지뢰 체크 로직을 할 때 숫자로로 쉽게 계산을 하기 위해 x를 0으로 치환 */
                if(input[i][j] == 'x'){
                    input[i][j] = '0';
                    /* 열려있는 칸이 지뢰일 경우 */
                    if(boom[i][j] == '*'){
                        input[i][j] = '*';
                        isBoom = true;
                    }
                }
            }
        }

        /* 열려있는 칸중에 지뢰가 있을경우 지뢰있는칸 모두 오픈 */
        if(isBoom){
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if(boom[i][j] == '*'){
                        input[i][j] = '*';
                    }
                }
            }
        }

        /* 지뢰 체크 로직 */
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(boom[i][j] == '*'){
                    changeBoom(i, j);
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (char[] inputLine : input) {
            result
                    .append(new String(inputLine))
                    .append("\n");
        }

        System.out.println(result.toString());
    }

    private static void changeBoom(int i, int j) {
        for (int k = 0; k < xArray.length; k++) {
            int x = i + xArray[k];
            int y = j + yArray[k];
            /* 필드내에 있으며, 지뢰가 아니고, 열려있을 경우 */
            if(isField(x,y) && input[x][y] != '*' && input[x][y] != '.'){
                input[x][y]++;
            }
        }
    }

    private static boolean isField(int i, int j){
        return i >= 0 && j >= 0 && i < n && j < n;
    }
}

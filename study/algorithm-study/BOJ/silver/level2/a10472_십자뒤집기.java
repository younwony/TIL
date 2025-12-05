package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class a10472_십자뒤집기 {
    static int[][][] clickArray = {
            {{0,0},{0,1},{1,0}}, {{0,0},{0,1},{0,2},{1,1}}, {{0,1},{0,2},{1,2}},
            {{0,0},{1,0},{2,0},{1,1}}, {{0,1}, {1,0},{1,1},{1,2},{2,1}}, {{0,2},{1,1},{1,2},{2,2}},
            {{1,0},{2,0},{2,1}}, {{1,1},{2,0},{2,1},{2,2}}, {{1,2},{2,1},{2,2}}};
    static boolean[] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int P = Integer.parseInt(bufferedReader.readLine());


        for(int i = 0; i < P; i++){
            // . = false , * true
            int[][] field = new int[3][3];
            visited = new boolean[(int) Math.pow(2,9)];
            for (int j = 0; j < 3; j++) {
                String[] input = bufferedReader.readLine().split("");
                for (int k = 0; k < input.length; k++) {
                    field[j][k] = "*".equals(input[k]) ? 1 : 0;
                }
            }

            //then
            Queue<Field> queue = new LinkedList<>();
            queue.add(new Field(0,field));
            int index = getIndex(field);
            if(index == 0){
                System.out.println(0);
                continue;
            }
            loop:
            while(!queue.isEmpty()){
                Field nextField = queue.poll();
                for(int j = 0; j < clickArray.length; j++){
                    click(nextField.field, j);
                    index = getIndex(nextField.field);
                    if(!visited[index]){
                        visited[index] = true;
                        if(index == 0){
                            System.out.println(nextField.cnt + 1);
                            break;
                        }
                        queue.add(new Field(nextField.cnt + 1, nextField.field));
                    }
                    click(nextField.field, j);
                }
            }

        }
    }

    private static int getIndex(int[][] field) {
        String str = "";
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                str += field[i][j];
            }
        }

        return Integer.parseInt(str,2);
    }

    private static void click(int[][] field, int index) {
        int[][] points = clickArray[index];
        for (int[] point : points) {
            int x = point[0];
            int y = point[1];
            field[x][y] = field[x][y] == 0 ? 1 : 0;
        }
    }

    static class Field {
        int cnt;
        int[][] field = new int[3][3];

        public Field(int cnt, int[][] field) {
            this.cnt = cnt;
            setField(this.field,field);
        }

        private void setField(int[][] setField, int[][] field) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    setField[i][j] = field[i][j];
                }
            }
        }
    }
}

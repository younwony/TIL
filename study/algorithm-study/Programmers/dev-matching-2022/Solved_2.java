package dev_matching_2022_2;

public class Solved_2 {
    public static void main(String[] args) {
        int[][] solution = solution(4, true);
//        int[][] solution = solution(5, false);

        for (int[] ints : solution) {
            for (int anInt : ints) {
                System.out.print(anInt);
            }
            System.out.println();
        }
    }

    public static int[][] solution(int n, boolean horizontal) {
        int[][] answer = new int[n][n];

        int moveIndex = horizontal ? 0 : 1;

        moveRoobot(n, answer, moveIndex);

        return answer;
    }

    private static void moveRoobot(int n, int[][] answer, int moveIndex) {
        int number = 1;
        answer[0][0] = number++;
        int x = 0;
        int y = 0;
        for(int i = 0; i < n - 1; i++) {

            if(i % 2 == moveIndex){
                y += 1;
            }else{
                x += 1;
            }

            answer[x][y] = number++;


            if (i % 2 == moveIndex) {
                for (int j = 0; j <= i; j++) {
                    x += 1;
                    y += 0;
                    answer[x][y] = number++;
                }

                for (int j = 0; j <= i; j++) {
                    x += 0;
                    y += -1;
                    answer[x][y] = number++;
                }
            } else {
                for (int j = 0; j <= i; j++) {
                    x += 0;
                    y += 1;
                    answer[x][y] = number++;
                }

                for (int j = 0; j <= i; j++) {
                    x += -1;
                    y += -0;
                    answer[x][y] = number++;
                }
            }
        }
    }
}

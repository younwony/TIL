package level_2;

public class _가장_큰_정사각형_찾기 {

    public static void main(String[] args) {
//        System.out.println(solution(new int[][]{{0,1,1,1},{1,1,1,1},{1,1,1,1},{0,0,1,0}}));
//        System.out.println(solution(new int[][]{{0,0,1,1},{1,1,1,1}}));
        System.out.println(solution(new int[][]{{0}}));
    }


    private static int solution(int [][]board)
    {
        int answer = 0;

        int[][] dp = new int[board.length][board[0].length];

        /* 1행에 1이 있을경우 answer = 1*/
        for (int dpVal : dp[0]) {
            if(dpVal == 1){
                answer = 1;
                break;
            }
        }
        /* dp */
        for (int i = 1; i < dp.length; i++) {
            /*각 행 첫번째 열 입력*/
            dp[i][0] = board[i][0];
            for (int j = 1; j < dp[0].length; j++) {
                /* 현재 필드 값이 0 이면 패스 */
                if(board[i][j] == 0) continue;

                int _x_y = dp[i - 1][j - 1];
                int _xy = dp[i - 1][j];
                int x_y = dp[i][j - 1];
                /* 결과값이 모두 같으면 + 1*/
                if(_x_y == _xy && _xy == x_y) {
                    dp[i][j] = _x_y + 1;
                }else{
                    /* 결과값이 모두 같지 않을 경우 최소값에 + 1*/
                    dp[i][j] = Math.min(x_y, Math.min(_xy, _x_y)) + 1;
                }
                /* 최대값 구하기 */
                answer = Math.max(answer, dp[i][j]);
            }
        }

        return answer*answer;
    }
    /* 효율성 실패 */
    private static int solution2(int [][]board)
    {
        int answer = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if(board[i][j] == 0) continue;
                int sqrWidth = squareWidth(i,j, board);
                answer = Math.max(answer,sqrWidth);
            }
        }

        return answer;
    }

    private static int squareWidth(int i, int j, int[][] board) {
        int result = 0;
        boolean isSquare = true;
        whileLoop:
        while (isSquare){
            result++;
            for(int x = i; x < i + result; x++){
                for(int y = j; y < j + result; y++){
                    isSquare = isSquare(x,y,board);
                    if(!isSquare) break whileLoop;
                }
            }
        }
        return result*result;
    }

    private static boolean isSquare(int i, int j, int[][] board) {
        int nextI = i + 1;
        int nextJ = j + 1;
        if(nextI >= board.length) return false;
        if(nextJ >= board[0].length) return false;
        if(board[i][nextJ] == 0) return false;
        if(board[nextI][j] == 0) return false;
        if(board[nextI][nextJ] == 0) return false;
        return true;
    }
}

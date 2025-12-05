package level_2;

public class _삼각_달팽이 {

    public static void main(String[] args) {
        System.out.println(solution(4));
    }

    private static int[] solution(int n) {

        int[] answer = new int[(n+1)*n/2];

        int[][] field = new int[n][n];

        int[] yMoves = new int[]{0,1,-1};
        int[] xMoves = new int[]{1,0,-1};

        int number = 1;
        int index = 0;
        int x = -1;
        int y = 0;
        while(n > 0){
            for(int i =0 ; i< n; i++){
                x += xMoves[index];
                y += yMoves[index];
                field[x][y] = number++;
            }
            index = (index+1)%3;
            n--;
        }

        int answerIndex = 0;
        for (int[] ints : field) {
            for (int val : ints) {
                if(val == 0) break;
                answer[answerIndex++] = val;
            }
        }
        return answer;
    }
}

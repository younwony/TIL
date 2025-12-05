package level_2;

public class _땅따먹기 {

    public static void main(String[] args) {
        System.out.println(solution(new int[][]{{1,2,3,5},{5,6,7,8},{4,3,2,1},{4,6,3,4}}));
        System.out.println(solution(new int[][]{{1,2,3,5}}));
    }

    private static int solution(int[][] land) {
        int answer = 0;

        for (int i = 1; i < land.length; i++) {
            for (int j = 0; j < 4; j++) {
                int maxPreLandScore = 0;
                for(int k = 0; k < 4; k++){
                    if(j == k) continue;
                    maxPreLandScore = Math.max(maxPreLandScore, land[i-1][k]);
                }
                land[i][j] += maxPreLandScore;
            }
        }

        for(int i =0; i < 4; i++){
            answer = Math.max(land[land.length - 1][i], answer);
        }

        return answer;
    }
}

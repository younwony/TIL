package level_3;

public class _n으로_표현 {

    public static void main(String[] args) {
        System.out.println(solution(5, 12));
        System.out.println(solution(5, 555));
        System.out.println(solution(2, 11));
    }

    static int numberCheck = Integer.MAX_VALUE;
    static int n;
    static int num;
    private static int solution(int N, int number) {
        int answer = 0;
        n = N;
        num = number;
        int nn = N;
        for(int i = 0 ; i < 8; i ++){
            dfs(nn, i+1);
            nn = nn*10 + N;
        }

        return numberCheck > 8 ? -1 : numberCheck;
    }

    private static void dfs(int nowNumber, int numberCnt) {
        if(nowNumber == num){
            numberCheck = Math.min(numberCheck, numberCnt);
            return ;
        }
        if(numberCnt > 8) return;

        dfs(nowNumber+n, numberCnt+1);
        dfs(nowNumber/n, numberCnt+1);
        dfs(nowNumber-n, numberCnt+1);
        dfs(nowNumber*n, numberCnt+1);
    }
}

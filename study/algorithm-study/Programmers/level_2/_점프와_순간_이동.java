package level_2;

public class _점프와_순간_이동 {
    /**
     * 1. K칸 앞으로 점프 -> 건전지 사용량 K 만큼 듬
     * 2. 현재 온 거리 * 2 순간이동 -> 건전지 사용량 x
     */
    public static void main(String[] args) {
        System.out.println(solution(5));
        System.out.println(solution(6));
        System.out.println(solution(5000));
    }
    private static int solution(int n) {
        int anser = 0;
        while(n > 0){
            if(n%2 != 0) anser++;
            n /= 2;
        }
        return anser;
    }
    /* 효율성 실패 */
    private static int solution2(int n) {
        int[] dp = new int[n+1];
        for(int i = 1 ; i <= n; i++){
            if(i%2 == 0) dp[i] = dp[i/2];
            else dp[i] = dp[(i-1)/2] + 1;
        }
        return dp[n];
    }
}

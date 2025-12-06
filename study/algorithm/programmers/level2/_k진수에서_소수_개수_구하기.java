package level_2;

import java.util.StringTokenizer;

public class _k진수에서_소수_개수_구하기 {
    public static void main(String[] args) {
        System.out.println(solution(437674, 3));
        System.out.println(solution(110011, 10));
    }

    public static int solution(int n, int k) {
        int ans = 0;
        String temp[] = Integer.toString(n, k).split("0");

        Loop :
        for (String t : temp) {
            if (t.length() == 0) continue;
            long a = Long.parseLong(t);
            if (a == 1) continue;
            for (int i = 2; i <= Math.sqrt(a); i++) {

                if (a % i == 0) {
                    continue Loop;
                }
            }

            ans++;
        }
        return ans;
    }

}



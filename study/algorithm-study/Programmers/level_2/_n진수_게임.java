package level_2;

public class _n진수_게임 {

    public static void main(String[] args) {
        System.out.println(solution(2,4,2,1));
        System.out.println(solution(16,16,2,1));
        System.out.println(solution(16,16,2,2));
    }

    private static String solution(int n, int t, int m, int p) {
        String answer = "";
        String baseN = "";
        int index = 0;
        while(baseN.length() <= t*m){
            baseN += Integer.toString(index++, n);
        }

        int nowIndex = p-1;
        while(t > 0){
            answer += baseN.charAt(nowIndex);
            nowIndex += m;
            t--;
        }

        return answer.toUpperCase();
    }
}

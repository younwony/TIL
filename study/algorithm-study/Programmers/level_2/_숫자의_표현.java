package level_2;

public class _숫자의_표현 {

    public static void main(String[] args) {
        System.out.println(solution(15));
    }

    private static int solution(int n) {
        int answer = 1;

        for(int i = 1; i <= n/2 + 1; i++){
            int sum = i;
            for(int j = i + 1; j <= n/2 + 1; j++){
                sum += j;
                if(sum == n){
                    answer++;
                    break;
                }

                if(sum > n) break;
            }
        }
        return answer;
    }
}

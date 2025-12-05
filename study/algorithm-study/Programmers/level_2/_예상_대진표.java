package level_2;

public class _예상_대진표 {

    /**
     * 1. n명 참가
     */
    public static void main(String[] args) {
        System.out.println(solution(8, 4, 7));
        for(int i = 5; i <= 16; i++)
            System.out.println(solution(8, 4, i));
    }

    private static int solution(int n, int a, int b){
        int answer = 1;

        if(a > b){
            int temp = a;
            a = b;
            b = temp;
        }

        while(n > 0){
            if(a + 1 == b && a % 2 == 1) break;
            if(a % 2 == 1) a++;
            a /= 2;
            if(b % 2 == 1) b++;
            b /= 2;
            n /= 2;
            answer++;
        }


        return answer;
    }
}

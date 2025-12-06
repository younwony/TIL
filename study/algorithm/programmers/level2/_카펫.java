package level_2;

public class _카펫 {
    public static void main(String[] args) {
        System.out.println(solution(10, 2));
        System.out.println(solution(8, 1));
        System.out.println(solution(24, 24));
        System.out.println(solution(3, 6));
        /* Yello 필드 체크하지않았을때 실패 케이스*/
        System.out.println(solution(50, 22));
    }

    /*
    * x >= y
    * */
    private static int[] solution(int brown, int yellow) {
        int[] answer = new int[2];
        
        int max = brown + yellow;
        /* 제곱근에서부터 찾기위해 제곱근 구하기*/
        int sqrt = (int) Math.sqrt(max);
        while(true){
            /* x/y값이 딱 나누어 떨어지고 yellow 필드 또한 딱 나누어 떨어지는지 체크
            * 기존에는 yellow필드가 정확히 나누어떨어지는지 체크하지 않아서 몇몇의 TestCase에서 실패하였다.
            * */
            if(max%sqrt == 0 && yellow%(max/sqrt-2) == 0) break;
            sqrt--;
        }

        answer[0] = max/sqrt;
        answer[1] = sqrt;

        return answer;
    }
}

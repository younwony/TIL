package level_1;

public class _내적 {

    public static void main(String[] args) {
        System.out.print(solution(new int[]{1,2,3,4}, new int[]{-3,-1,0,2}));
    }

    private static int solution(int[] a, int[] b) {
        int answer = 0;
        for (int i =0; i < a.length; i++){
            answer += a[i]*b[i];
        }
        return answer;
    }
}

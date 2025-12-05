package level_2;

public class _주식가격 {

    public static void main(String[] args) {
        System.out.println(solution(new int[]{1,2,3,2,3}));
    }

    private static int[] solution(int[] prices) {
        int[] answer = new int[prices.length];
        for (int i = 0; i < prices.length; i++) {
            int now = prices[i];
            for(int j = i + 1; j < prices.length; j++){
                answer[i]++;
                int next = prices[j];
                if(now > next) break;
            }
        }
        return answer;
    }
}

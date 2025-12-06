package level_2;

public class _타겟_넘버 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{1,1,1,1,1}, 3));
    }

    static int answer = 0;
    private static int solution(int[] numbers, int target) {

        dfs(+numbers[0], numbers, 1, target);
        dfs(-numbers[0], numbers, 1, target);
        return answer;
    }

    private static void dfs(int value, int[] numbers, int depth, int target) {
        if(depth == numbers.length){
            if(value == target) answer++;
            return;
        }
        dfs(value+numbers[depth], numbers, depth+1, target);
        dfs(value-numbers[depth], numbers, depth+1, target);
    }
}

package level_2;

import java.util.HashSet;
import java.util.Set;

public class _소수_찾기 {

    public static void main(String[] args) {
    }

    private static int[] decimal = new int[10000000];
    private static boolean[] visited;
    private static Set<Integer> decimalCnt = new HashSet<>();

    private static int solution(String numbers) {

        setDecimal();

        String[] numberArray = numbers.split("");
        visited = new boolean[numberArray.length];
        for (int i = 0; i < numberArray.length; i++) {
            visited[i] = true;
            dfs(numberArray, numberArray[i]);
            visited[i] = false;
        }
        return decimalCnt.size();
    }

    private static void dfs(String[] numberArray, String s) {
        for (int i = 0; i < numberArray.length; i++) {
            if(visited[i]) continue;
            visited[i] = true;
            dfs(numberArray, s + numberArray[i]);
            visited[i] = false;
        }

        if(decimal[Integer.valueOf(s)] == 0) decimalCnt.add(Integer.valueOf(s));
    }

    private static void setDecimal() {
        /* 제곱근 까지 구하는이유
        *  임의의 숫자(N)을 임의의 숫자 (M)으로 나누면 몫이 생기게 되는데, 몫과 나누는 수(M) 둘중 하나는 N의 제곱근 이하이기 때문이다.
        * */
        decimal[0] = 1;
        decimal[1] = 1;
        for(int i = 2; i < Math.sqrt(decimal.length); i++){
            if(decimal[i] == 1) continue;
            for(int j = i*2; j < decimal.length; j += i){
                decimal[j] = 1;
            }
        }
    }
}

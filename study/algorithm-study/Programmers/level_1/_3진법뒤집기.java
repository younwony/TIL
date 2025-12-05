package level_1;

import java.util.ArrayList;
import java.util.Stack;

public class _3진법뒤집기 {

    public static void main(String[] args) {
        System.out.println(bestSolution(125));
        System.out.println(solution(125));
    }

    private static int solution(int n) {

        int answer = 0;
        int nMod;
        int nDiv = n;
        int base = 3;
        /* reverse로 인해 스택에 저장 */
        Stack<Integer> base3Stack = new Stack<>();
        /* 3진법 만들기 */
        while(nDiv >= 3){
            nMod = nDiv%base;
            nDiv /= base;
            base3Stack.push(nMod);
        }
        base3Stack.push(nDiv);


        int indices = 1;
        while (!base3Stack.isEmpty()){
            answer += indices * base3Stack.pop();
            indices *= 3;
        }
        return answer;
    }

    /**
     * desc : 내코드에 비해 엄청 간결하게 짯다.
     * 그래도 평균 풀이 시간이 내코드는 0.2ms Best는 1.5ms로 시간적인 우위를 가져왓다.
     */
    private static int bestSolution(int n){
        /* 3진법으로 저장할 String*/
        String base3 = "";

        /* n이 0보다 클 때 까지 계속*/
        while(n > 0){
            /* 나머지 값을 계속 저장*/
            base3 += (n % 3);
            n /= 3;
        }

        return Integer.valueOf(base3, 3);
    }
}

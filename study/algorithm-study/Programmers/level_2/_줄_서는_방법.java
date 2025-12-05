package level_2;

import java.util.ArrayList;
import java.util.List;

public class _줄_서는_방법 {

    public static void main(String[] args) {

        int[] solution = solution(3, 4);
        for (int i : solution) {
            System.out.println("i = " + i);
        }
    }


    public static int[] solution(int n, long k) {
        int[] answer = new int[n];

        ArrayList<Integer> numbers = new ArrayList<>();

        long fac = 1;
        for (int i = 1; i <= n; i++) {
            numbers.add(i);
            fac *= i;
        }

        k--;

        int index = 0;
        while(n > 0){
            fac /= n--;
            int numberIndex = (int) (k / fac);
            answer[index++] = numbers.get(numberIndex);
            numbers.remove(numberIndex);
            k %= fac;
        }

        return answer;
    }
}

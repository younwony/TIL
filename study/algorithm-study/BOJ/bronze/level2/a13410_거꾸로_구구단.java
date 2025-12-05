package _0_Bronze.Level_2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class a13410_거꾸로_구구단 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int K = scanner.nextInt();
        ArrayList<Integer> gugudan = new ArrayList<>();
        for (int i = 1; i <= K; i++) {
            int number = N * i;
            String reverseStr = new StringBuilder(String.valueOf(number)).reverse().toString();
            int reverse = Integer.parseInt(reverseStr);
            gugudan.add(reverse);
        }
        Collections.sort(gugudan, Collections.reverseOrder());
        System.out.println(gugudan.get(0));

    }
}

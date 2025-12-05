package _1_Silver.Level_3;

import java.util.Arrays;
import java.util.Scanner;

public class a11399_ATM {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] person = new int[n];
        for (int i = 0; i < n; i++) {
            person[i] = scanner.nextInt();
        }
        Arrays.sort(person);
        int sum = 0;
        for (int i = 0; i < person.length; i++) {
            sum += person[i];
            person[i] = sum;
        }

        int answer = 0;
        for (int time : person) {
            answer += time;
        }

        System.out.println(answer);

    }
}

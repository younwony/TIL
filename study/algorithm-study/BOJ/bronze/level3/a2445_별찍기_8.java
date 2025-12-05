package _0_Bronze.Level_3;

import java.util.Scanner;

public class a2445_별찍기_8 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        StringBuilder output = new StringBuilder();
        int index = 1;
        for (int i = 0; i < 2*n - 1; i++) {
            for (int j = 0; j < 2*n; j++) {
                output.append(j >= index && j < 2*n - index ? ' ' : '*');
            }
            output.append("\n");
            index += i < n - 1 ? 1 : -1;
        }
        output.delete(output.length()-1, output.length());

        System.out.println(output.toString());
    }
}

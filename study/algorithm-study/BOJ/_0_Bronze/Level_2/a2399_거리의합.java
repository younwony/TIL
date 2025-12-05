package _0_Bronze.Level_2;

import java.util.Scanner;

public class a2399_거리의합 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        int[] x = new int[n];

        long result = 0;

        for(int i = 0 ; i< n; i++){
            x[i] = scanner.nextInt();
        }

        for(int i = 0; i < n; i++){
            for(int j = 0; j<n; j++){
                result += Math.abs(x[i] - x[j]);
            }
        }

        System.out.println(result);
    }
}

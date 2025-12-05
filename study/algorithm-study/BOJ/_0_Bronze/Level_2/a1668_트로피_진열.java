package _0_Bronze.Level_2;

import java.util.Scanner;

public class a1668_트로피_진열 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int[] NArray = new int[N];
        for(int i = 0; i < N; i++){
            NArray[i] = scanner.nextInt();
        }


        int left = 0;
        int temp = 0;
        for (int trophy : NArray) {
            if(temp < trophy){
                left++;
                temp = trophy;
            }
        }
        temp = 0;
        int right = 0;
        for (int i = NArray.length-1; i >= 0; i--) {
            if(temp < NArray[i]){
                right++;
                temp = NArray[i];
            }
        }

        System.out.println(left);
        System.out.println(right);
    }
}

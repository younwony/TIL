package _0_Bronze.Level_1;

import java.util.Scanner;

public class a15829_Hashing {
    static int modValue = 1234567891;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int l = scanner.nextInt();
        scanner.nextLine();
        String input = scanner.nextLine();
        long sum = 0;
        for(int i = 0 ; i < input.length(); i++){
            int nowChar = input.charAt(i) - 96;
            sum += nowChar * pow(i);
            sum %= modValue;
        }

        System.out.println(sum);
    }

    private static double pow(int index) {
        long result = 1;
        for(int i = 0; i < index; i++){
            result *= 31;
            result %= modValue;
        }
        return result;
    }
}

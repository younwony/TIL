package _0_Bronze.Level_1;

import java.util.Scanner;

public class a1145_적어도_대부분의_배수 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] numbers = new int[5];
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = scanner.nextInt();
        }

        for (int i = 0; i < numbers.length; i++) {
            for (int j = i+1; j < numbers.length; j++) {
                for (int k = j+1; k < numbers.length; k++) {
                    int gcd = getGcd(numbers[i], numbers[j]);
                    int number = getGcd(gcd, numbers[k]);
                    min = Math.min(min,number);
                }
            }
        }

        System.out.println(min);

    }

    private static int getGcd(int number1, int number2) {
        return (number1 * number2)/gcd(number1, number2);
    }


    public static int gcd(int a, int b){
        if(b == 0){
            return a;
        }else{
            return gcd(b, a%b);
        }
    }
}

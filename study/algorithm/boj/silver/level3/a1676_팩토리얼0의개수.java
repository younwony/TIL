package _1_Silver.Level_3;

import java.math.BigInteger;
import java.util.Scanner;

public class a1676_팩토리얼0의개수 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        BigInteger bigInteger = BigInteger.valueOf(1);

        for(int i = n; i > 1; i--){
            bigInteger = bigInteger.multiply(BigInteger.valueOf(i));
        }

        String str = bigInteger.toString();


        int result = 0;
        for(int i = str.length() - 1; i < str.length(); i--){
            if(str.charAt(i) == '0'){
                result++;
            }else{
                break;
            }
        }
        System.out.println(result);

    }
}

package _0_Bronze.Level_1;

import java.util.Scanner;

public class a1258_팰린드롬수 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while(true){
            int n = scanner.nextInt();
            if(n == 0) break;
            System.out.println(isPalindrome(n) ? "yes" : "no");
        }
    }

    private static boolean isPalindrome(int n) {

        String palindromeStr = String.valueOf(n);
        for(int i = 0 ; i< palindromeStr.length()/2; i++){
            if(palindromeStr.charAt(i) != palindromeStr.charAt(palindromeStr.length()-1-i)) return false;
        }
        return true;
    }
}

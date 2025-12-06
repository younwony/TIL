package _0_Bronze.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class a9933_민균이의_비밀번호 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bufferedReader.readLine());
        HashSet<String> set = new HashSet<>();
        int len = 0;
        char mid = ' ';
        for(int i = 0 ; i < N; i++){
            StringBuilder input = new StringBuilder(bufferedReader.readLine());
            String str = input.toString();
            String reverse = input.reverse().toString();
            if(set.contains(reverse) || str.equals(reverse)){
                len = input.length();
                mid = input.charAt(len/2);
                break;
            }
            set.add(str);
        }

        System.out.println(len + " " + mid);
    }
}

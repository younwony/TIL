package _0_Bronze.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class a6996_애너그램 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int testCnt = Integer.parseInt(bufferedReader.readLine());

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0 ;i < testCnt; i++){
            String[] input = bufferedReader.readLine().split(" ");
            char[] a = input[0].toCharArray();
            char[] b = input[1].toCharArray();
            stringBuilder
                    .append(input[0])
                    .append(" & ")
                    .append(input[1])
                    .append(" are ");
            if(isNonAnagram(a, b)){
                stringBuilder
                        .append("NOT ");
            }
            stringBuilder
                    .append("anagrams.\n");
        }

        System.out.println(stringBuilder.toString());
    }

    private static boolean isNonAnagram(char[] a, char[] b) {
        if(a.length != b.length){
            return true;
        }
        Arrays.sort(a);
        Arrays.sort(b);
        for(int i = 0 ; i < a.length; i++){
            if(a[i] != b[i]){
                return true;
            }
        }
        return false;
    }
}

package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a4446_ROT13 {


    static String rot13L = "aiyeou";
    static String rot13U = rot13L.toUpperCase();
    static String rot13bL = "bkxznhdcwgpvjqtsrlmf";
    static String rot13bU = rot13bL.toUpperCase();
    static int A_LENGTH = 3;
    static int B_LENGTH = 10;

    public static void main(String[] args) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while((line = bufferedReader.readLine()) != null){
            stringBuilder
                    .append(getSolve(line))
                    .append("\n");
        }
        System.out.println(stringBuilder.toString());
    }

    private static String getSolve(String line) {

        StringBuilder result = new StringBuilder();

        for(int i = 0; i < line.length(); i++){
            String str = String.valueOf(line.charAt(i));
            if(rot13L.indexOf(str) > -1){
                result
                        .append(rot13L.charAt((rot13L.indexOf(str) + A_LENGTH)%rot13L.length()));
                continue;
            }

            if(rot13U.indexOf(str) > -1){
                result
                        .append(rot13U.charAt((rot13U.indexOf(str) + A_LENGTH)%rot13U.length()));
                continue;
            }

            if(rot13bL.indexOf(str) > -1){
                result
                        .append(rot13bL.charAt((rot13bL.indexOf(str) + B_LENGTH)%rot13bL.length()));
                continue;
            }

            if(rot13bU.indexOf(str) > -1){
                result
                        .append(rot13bU.charAt((rot13bU.indexOf(str) + B_LENGTH)%rot13bU.length()));
                continue;
            }

            result
                    .append(str);
        }

        return result.toString();
    }
}

package _0_Bronze.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class a10798_세로읽기 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> list = new ArrayList<>();

        int maxLine = 0;
        String line = "";
        while((line = bufferedReader.readLine()) != null && !line.isEmpty()){
            list.add(line);
            maxLine = Math.max(maxLine, line.length());
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < maxLine; i++) {
            for (String s : list) {
                try{
                    stringBuilder.append(s.charAt(i));
                }catch (Exception e){
                }
            }
        }
        System.out.println(stringBuilder.toString());

    }
}

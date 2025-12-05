package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a1543_문서_검색 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String target = bufferedReader.readLine();
        String str = bufferedReader.readLine();
        int cnt = 0;
        int len = str.length();
        int index = 0 - len;
        while(true){
            index = target.indexOf(str, index + len);
            if(index == -1 || index > target.length()) break;
            cnt++;
        }
        System.out.println(cnt);
    }
}

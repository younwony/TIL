package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a14405_피카츄 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String S = bufferedReader.readLine();
        String R = new String(S);

        String[] arrays = new String[]{"pi","ka","chu"};

        for (int i = 0; i < arrays.length; i++) {
            String a = arrays[i];
            String r = arrays[arrays.length - 1 - i];
            S = S.replace(a, "");
            R = R.replace(r, "");
        }

        String result = "YES";
        if(!S.equals(R)){
            result = "NO";
        }else if (S.length() > 0 || R.length() > 0){
            result = "NO";
        }

        System.out.println(result);

    }
}

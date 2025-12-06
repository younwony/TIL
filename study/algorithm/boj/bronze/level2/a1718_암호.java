package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a1718_암호 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String plain = bufferedReader.readLine();
        String encryptionKey = bufferedReader.readLine();

        StringBuilder encryptionTest = new StringBuilder();

        int keyLength = encryptionKey.length();
        int a = 'a';
        int z = 'z';
        for(int i = 0 ; i < plain.length(); i++){
            char plainC = plain.charAt(i);
            if(plainC == ' '){
                encryptionTest.append(plainC);
                continue;
            }
            int key = encryptionKey.charAt(i % keyLength) - a + 1;
            int encryptionT = plainC - key;
            if(encryptionT < a){
                encryptionT = z - (a - encryptionT) + 1;
            }
            encryptionTest.append((char)encryptionT);
        }
        System.out.println(encryptionTest.toString());
    }
}

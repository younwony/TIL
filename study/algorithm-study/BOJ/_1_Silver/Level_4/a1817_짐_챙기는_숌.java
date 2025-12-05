package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a1817_짐_챙기는_숌 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int N = Integer.parseInt(stringTokenizer.nextToken());
        int M = Integer.parseInt(stringTokenizer.nextToken());

        if(N == 0){
            System.out.println(0);
            return;
        }

        int[] books = new int[N];

        stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        for (int i = 0; i < books.length; i++) {
            books[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        int cnt = 1;
        int box = M;
        for(int i = 0; i < N; i++){
            int book = books[i];
            if(book <= box){
                box -= book;
            }else{
                cnt++;
                box = M - book;
            }
        }

        System.out.println(cnt);
    }
}

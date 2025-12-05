package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.StringTokenizer;

public class a1269_대칭_차집합 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int aLen = Integer.parseInt(stringTokenizer.nextToken());
        int bLen = Integer.parseInt(stringTokenizer.nextToken());
        HashSet<Integer> set = new HashSet<>();
        stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        for(int i = 0 ; i < aLen; i++){
            int a = Integer.parseInt(stringTokenizer.nextToken());
            set.add(a);
        }
        stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        for(int i = 0 ; i < bLen; i++){
            int b = Integer.parseInt(stringTokenizer.nextToken());
            if(set.contains(b)){
                set.remove(b);
            }else{
                set.add(b);
            }
        }

        System.out.println(set.size());


    }
}

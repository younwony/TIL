package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class a18115_카드_놓기 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bufferedReader.readLine());
        int[] result = new int[N];
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        Deque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < N; i++) {
             deque.offer(i);
        }

        int cnt = N;
        while(stringTokenizer.hasMoreTokens()){
            int condition = Integer.parseInt(stringTokenizer.nextToken());
            if(condition == 1){
                result[deque.pollFirst()] = cnt--;
            }
            if(condition == 2){
                int temp = deque.pollFirst();
                result[deque.pollFirst()] = cnt--;
                deque.push(temp);
            }
            if(condition == 3){
                result[deque.pollLast()] = cnt--;
            }
        }

        StringBuilder str = new StringBuilder();
        for (int i : result) {
            str
                    .append(i)
                    .append(" ");
        }

        System.out.println(str.toString());
    }
}

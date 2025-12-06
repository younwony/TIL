package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.PriorityQueue;

public class a11286_절댓값_힙 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine());
        PriorityQueue<Integer> queue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int abs = Math.abs(o1) - Math.abs(o2);
                if(abs == 0) return o1 - o2;
                return abs;
            }
        });

        for(int i = 0; i < n; i++){
            int input = Integer.parseInt(bufferedReader.readLine());
            if(input == 0){
                int answer = 0;
                if(!queue.isEmpty()) answer = queue.poll();
                System.out.println(answer);
            }else{
                queue.add(input);
            }
        }
    }
}

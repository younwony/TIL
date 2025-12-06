package _2_Gold.Level_5;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class a14226_이모티콘 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int S = scanner.nextInt();
        boolean[][] visited = new boolean[1001][1001];
        Queue<Emo> queue = new LinkedList<>();
        queue.add(new Emo(0,1,0));
        visited[1][0] = true;
        while(!queue.isEmpty()){
            Emo emo = queue.poll();

            if(emo.view == S){
                System.out.println(emo.time);
                break;
            }

            if(!visited[emo.view][emo.view]){
                visited[emo.view][emo.view] = true;
                queue.add(new Emo(emo.time + 1, emo.view,emo.view));
            }

            int sum = emo.view + emo.clip;
            if(sum <= 1000 && !visited[sum][emo.clip]){
                visited[sum][emo.clip] = true;
                queue.add(new Emo(emo.time + 1, sum, emo.clip));
            }

            int minus = emo.view - 1;
            if(minus >= 2 && !visited[minus][emo.clip]){
                visited[minus][emo.clip] = true;
                queue.add(new Emo(emo.time + 1, minus, emo.clip));
            }
        }
    }

    static class Emo{
        int time;
        int view;
        int clip;

        public Emo(int time, int view, int clip) {
            this.time = time;
            this.view = view;
            this.clip = clip;
        }
    }


}

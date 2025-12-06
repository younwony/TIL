package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class a2304_창고_다각형 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bufferedReader.readLine());

        Stick[] sticks = new Stick[N];
        int maxIndex = 0;
        int max = 0;
        for (int i = 0; i < sticks.length; i++) {
            StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            sticks[i] = new Stick(Integer.parseInt(stringTokenizer.nextToken()),Integer.parseInt(stringTokenizer.nextToken()));
        }

        Arrays.sort(sticks, new Comparator<Stick>() {
            @Override
            public int compare(Stick o1, Stick o2) {
                return o1.x - o2.x;
            }
        });

        for (int i = 0; i < sticks.length; i++) {
            if(sticks[i].y >= max){
                max = sticks[i].y;
                maxIndex = i;
            }
        }

        Stick[] ascSticks = Arrays.copyOfRange(sticks, 0, maxIndex+1);
        Stick[] descSticks = Arrays.copyOfRange(sticks, maxIndex, N);

        int sum = max;

        for (int i = 0; i < ascSticks.length-1; i++) {
            int x = 0;
            int y = ascSticks[i].y;
            for (int j = i+1; j < ascSticks.length; j++) {
                if(ascSticks[i].y <= ascSticks[j].y){
                    x = ascSticks[j].x - ascSticks[i].x;
                    i = j-1;
                    break;
                }
            }
            sum += x * y;
        }


        for (int i = descSticks.length -1 ; i >= 0; i--) {
            int x = 0;
            int y = descSticks[i].y;
            for (int j = i-1; j >= 0; j--) {
                if(descSticks[j].y > descSticks[i].y){
                    x = descSticks[i].x - descSticks[j].x;
                    i = j+1;
                    break;
                }
            }
            sum += x * y;
        }

        System.out.println(sum);
    }

    static class Stick{
        int x;
        int y;

        public Stick(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}

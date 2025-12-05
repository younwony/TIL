package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class a9019_DSLR {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        for(int i = 0; i < n; i++){
            String[] nm = bufferedReader.readLine().split(" ");
            int now = Integer.parseInt(nm[0]);
            int target = Integer.parseInt(nm[1]);

            System.out.println(bfs(now, target));
        }
    }

    private static String bfs(int now, int target) {
        String output = "";
        Queue<Registry> queue = new LinkedList<>();
        queue.add(new Registry(now, ""));
        boolean[] visited = new boolean[10000];
        visited[now] = true;
        while(!queue.isEmpty()){
            Registry registry = queue.poll();
            if(isTarget(registry, target)) {
                return outTarget(registry, target);
            }

            if(!visited[registry.D]) {
                visited[registry.D] = true;
                queue.add(new Registry(registry.D, registry.output + "D"));
            }

            if(!visited[registry.S]){
                visited[registry.S] = true;
                queue.add(new Registry(registry.S, registry.output + "S"));
            }

            if(!visited[registry.L]){
                visited[registry.L] = true;
                queue.add(new Registry(registry.L, registry.output + "L"));
            }

            if(!visited[registry.R]){
                visited[registry.R] = true;
                queue.add(new Registry(registry.R, registry.output + "R"));
            }
        }

        return output;
    }

    private static String outTarget(Registry registry, int target) {
        String result = registry.output;
        if(registry.D == target) return result + "D";
        if(registry.S == target) return result + "S";
        if(registry.L == target) return result + "L";
        if(registry.R == target) return result + "R";
        return result;
    }

    private static boolean isTarget(Registry registry, int target) {
        if(registry.D == target) return true;
        if(registry.S == target) return true;
        if(registry.L == target) return true;
        if(registry.R == target) return true;
        if(registry.number == target) return true;
        return false;
    }

    static class Registry{
        int number;
        String output;
        int D;
        int S;
        int L;
        int R;

        public Registry(int number, String output) {
            this.number = number;
            this.output = output;
            D();
            S();
            L();
            R();
        }

        private void D(){
            this.D =  this.number * 2 % 10000;
        }

        private void S(){
            this.S = this.number == 0 ? 9999 : this.number -1;
        }

        private void L(){
            this.L = this.number % 1000 * 10 + this.number/1000;
        }

        private void R(){
            this.R = this.number % 10 * 1000 + this.number/10;
        }
    }
}

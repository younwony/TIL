package exam.interpark;

import java.util.Arrays;

public class Solve_3 {
    public static void main(String[] args) {
//        System.out.println(solution(10, new int[]{2,3,7,8}));
        System.out.println(solution(5, new int[]{2,2,2,2,2}));
        System.out.println(solution(20, new int[]{16,15,9,17,1,3}));
    }

    public static int solution(int M, int[] load) {
        int answer = 0;
        int m = M;

        Arrays.sort(load);
        int i = load.length-1;
        if(load[i] > M) return -1;
        boolean[] visited = new boolean[load.length];
        for (; i >= 0; i--) {
            if(load[i] == 0) continue;
            M -= load[i];
            visited[i] = true;
            while(M > 0){
                int index = Arrays.binarySearch(load, M);
                if(index < 0 || visited[index]) {
                    M--;
                    continue;
                }
                M -= load[index];
                visited[index] = true;
            }
            answer++;
            M = m;
        }
        return answer;
    }
}

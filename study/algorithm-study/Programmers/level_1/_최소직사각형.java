package level_1;

public class _최소직사각형 {

    public static void main(String[] args) {
        int[][] sizes = {{60, 50}, {30, 70}, {60, 30}, {80, 40}};
        System.out.println(solution(sizes));
        int[][] sizes2 = {{10, 7}, {12, 3}, {8, 15}, {14, 7}, {5, 15}};
        System.out.println(solution(sizes2));
        int[][] sizes3 = {{14, 4}, {19, 6}, {6, 16}, {18, 7}, {7, 11}};
        System.out.println(solution(sizes3));
    }

    public static int solution(int[][] sizes) {
        int answer = 0;

        int max = 0;
        int min = 0;

        for (int[] size : sizes) {
            max = Math.max(max, Math.max(size[0], size[1]));
            min = Math.max(min, Math.min(size[0], size[1]));
        }

        return max * min;
    }

}

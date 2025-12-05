package level_1;

import java.util.Arrays;

public class _K번째수 {

    public static void main(String[] args) {
        solution(new int[]{1, 5, 2, 6, 3, 7, 4}, new int[][]{{2, 5, 3}, {4, 4, 1}, {1, 7, 3}});
    }

    private static int[] solution(int[] array, int[][] commands) {
        int[] answer = new int[commands.length];

        for (int i = 0; i < commands.length; i++) {
            int[] command = commands[i];
            /* Arrays Util 사용해서 Array 자르기 */
            int[] commandArrray = Arrays.copyOfRange(array, command[0] -1, command[1]);
            /* 직접 구현 */
            /*int[] commandArrray = arraySplit(array, command[0], command[1]);*/
            Arrays.sort(commandArrray);
            int result = commandArrray[command[2] - 1];
            answer[i] = result;
        }

        return answer;
    }

    /* Array Split 구현 */
    private static int[] arraySplit(int[] array, int i, int j) {
        int[] result = new int[j-i+1];
        for (int k = 0; k < result.length; k++) {
            result[k] = array[k+i-1];
        }
        return result;
    }
}

package level_2;

import java.util.Arrays;

public class _쿼드압축_후_개수_세기 {

    /**
     * 1. 압축하고자 하는 영역 S
     * 2. S 내부의 수 모두 같은 값 -> S를 해당 수 하나로 압축
     * 3. S 내부의 수 다른 값 -> 4개의 균일한 정사각형 영역으로 쪼갠뒤 다시 2번 로직으로 이동
     */
    public static void main(String[] args) {
//        System.out.println(solution(new int[][]{{1,1,0,0},{1,0,0,0},{1,0,0,1},{1,1,1,1}}));
        System.out.println(solution(new int[][]{{1,1,1,1,1,1,1,1},{0,1,1,1,1,1,1,1},{0,0,0,0,1,1,1,1},{0,1,0,0,1,1,1,1},{0,0,0,0,0,0,1,1},{0,0,0,0,0,0,0,1},{0,0,0,0,1,0,0,1},{0,0,0,0,1,1,1,1}}));
    }

    static int[] answer = new int[2];

    private static int[] solution(int[][] arr) {
        dfs(arr);
        return answer;
    }

    private static void dfs(int[][] arr) {
        if(arr.length == 1) {
            answer[arr[0][0]]++;
            return;
        }

        if (isQuad(arr)) return;
        int arrLen = arr.length;
        int[][] oneQuadRent = setQuadRent(arr, 0, arrLen/2, arrLen/2, arrLen);
        int[][] twoQuadRent = setQuadRent(arr, 0, arrLen/2, 0, arrLen/2 );
        int[][] threeQuadRent = setQuadRent(arr, arrLen/2, arrLen, 0, arrLen/2 );
        int[][] fourQuadRent = setQuadRent(arr, arrLen/2, arrLen, arrLen/2, arrLen );
        dfs(oneQuadRent);
        dfs(twoQuadRent);
        dfs(threeQuadRent);
        dfs(fourQuadRent);
    }

    private static int[][] setQuadRent(int[][] arr, int startX, int endX, int startY, int endY) {
        int[][] twoQuadRent = Arrays.copyOfRange(arr, startX, endX);
        for (int i = 0; i < twoQuadRent.length; i++) {
            twoQuadRent[i] = Arrays.copyOfRange(twoQuadRent[i], startY, endY);
        }
        return twoQuadRent;
    }

    private static boolean isQuad(int[][] arr) {
        int firstValue = arr[0][0];
        for (int[] ints : arr) {
            for (int value : ints) {
                if (value != firstValue) return false;
            }
        }
        answer[firstValue]++;
        return true;
    }
}

package level_1;

public class _택배_상자_꺼내기 {

    public static void main(String[] args) {

        System.out.println(solution(22,6,8));
        System.out.println(solution(13,3,6));
        System.out.println(solution(20,1,6));
        System.out.println(solution(3,3,3));
    }


    /**
     * @param n 상자 개수
     * @param w 가로
     * @param num  꺼내는 번호
     * @return
     */
    public static int solution(int n, int w, int num) {
        int answer = 1;
//        가로 W
//        세로 H
        int h = n / w + (n % w == 0 ? 0 : 1);
        int[][] arr = new int[h][w];

        int numX = 0;
        int numY = 0;
        int cnt = 1;
        for(int i = 0; i < h; i++) {
            if(i % 2 == 0) {
                for(int j = 0; j < w; j++) {
                    arr[i][j] = cnt++;
                    arr[i][j] = arr[i][j] > n ? 0 : arr[i][j];
                    if(arr[i][j] == num) {
                        numX = i;
                        numY = j;
                    }
                }
            } else {
                for(int j = w - 1; j >= 0; j--) {
                    arr[i][j] = cnt++;
                    arr[i][j] = arr[i][j] > n ? 0 : arr[i][j];
                    if(arr[i][j] == num) {
                        numX = i;
                        numY = j;
                    }
                }
            }
        }

        while(++numX < h) {
            if(arr[numX][numY] != 0) {
                answer++;
            } else {
                break;
            }
        }

        return answer;
    }
}

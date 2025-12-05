package me.wony.level_1;

/**
 * [44, 1, 0, 0, 31, 25]	[31, 10, 45, 1, 6, 19]	[3, 5]
 * [0, 0, 0, 0, 0, 0]	[38, 19, 20, 40, 15, 25]	[1, 6]
 * [45, 4, 35, 20, 3, 9]	[20, 9, 3, 45, 4, 35]	[1, 1]
 */
public class _로또의_최고순위와_최저순위 {

    public static void main(String[] args) {


        System.out.println(solution(new int[]{44,1,0,0,31,25}, new int[]{31,10,45,1,6,19}));
        System.out.println(solution(new int[]{0, 0, 0, 0, 0, 0}, new int[]{38, 19, 20, 40, 15, 25}));
        System.out.println(solution(new int[]{45, 4, 35, 20, 3, 9}, new int[]{20, 9, 3, 45, 4, 35}));

    }

    private static int[] solution(int[] lottos, int[] win_nums) {
        int[] answer = new int[2];

        boolean[] lottoNumberCheck = new boolean[46];

        for (int win_num : win_nums) {
            lottoNumberCheck[win_num] = true;
        }

        int result = 7;

        int zero = 0;

        for (int number : lottos) {
            if(number == 0){
                zero++;
            }

            if(lottoNumberCheck[number]){
                result--;
            }
        }

        answer[1] = result == 7 ? 6 : result;
        result -= zero;
        answer[0] = result == 7 ? 6 : result;
        return answer;
    }
}

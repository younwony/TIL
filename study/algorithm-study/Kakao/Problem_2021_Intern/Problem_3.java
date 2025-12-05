package Problem_2021_Intern;

import java.util.Stack;

public class Problem_3 {
    public static void main(String[] args) {
        System.out.println(solution(8, 2, new String[]{"D 2","C","U 3","C","D 4","C","U 2","Z","Z","U 1","C"}));
        System.out.println(solution(8, 2, new String[]{"D 2","C","U 3","C","D 4","C","U 2","Z","Z"}));
//        System.out.println(solution(11, 10, new String[]{"D 2","C","U 3","C","D 4","C","U 2","Z","Z","U 1","C"}));
    }

    public static String solution(int n, int k, String[] cmd) {

        char[] table = new char[n];
        for(int i = 0; i < n; i++){
            table[i] = 'O';
        }

        /* 제거된 값을 담을 스택*/
        Stack<Integer> remove = new Stack<>();

        /* cmd */
        for (String s : cmd) {
            /* 값 */
            String[] order = s.split(" ");
            /* 위로 */
            if("U".equals(order[0])) {
                k = up(k, table, Integer.parseInt(order[1]));
            }else if("D".equals(order[0])){
                k = down(k, table, Integer.parseInt(order[1]));
            }else if("C".equals(order[0])){
                remove.push(k);
                table[k] = 'X';
                k = down(k, table, 1);
            }else if("Z".equals(order[0])){
                table[remove.pop()] = 'O';
            }
        }
        return new String(table);
    }

    private static int up(int k, char[] table, int move) {
        /* move 만큼*/
        while(move > 0){
            k--;
            /* k 가 0 보다 작으면 바로 위의 O 값을 찾은 후 종료  */
            if(k <= 0) {
                for (int i = 0; i < table.length; i++) {
                    if(table[i] == 'O') return i;
                }
            }
            /* 현재 k 가 X 이면 이동값 ++ */
            if(table[k] == 'X') move++;
            move--;
        }
        return k;
    }

    private static int down(int k, char[] table, int move) {
        /* move 만큼 이동 */
        while(move > 0){
            k++;
            /* k 가 테이블 범위 벗어날 경우 O 찾은 후 종료 */
            if(k >= table.length) {
                for (int i = table.length-1; i >= 0; i--) {
                    if(table[i] == 'O') return i;
                }
            }
            /* 현재 k 가 X 이면 이동값 ++*/
            if(table[k] == 'X') move++;
            move--;
        }
        return k;
    }
}

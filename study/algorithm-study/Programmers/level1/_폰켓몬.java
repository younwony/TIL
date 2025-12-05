package level_1;

import java.util.HashSet;
import java.util.Set;

public class _폰켓몬 {

    public static void main(String[] args) {
        System.out.println(solution(new int[]{3,1,2,3}));
        System.out.println(solution(new int[]{3,3,3,2,2,4}));
        System.out.println(solution(new int[]{3,3,3,2,2,2}));
    }

    private static int solution(int[] nums) {
        /* 입력된 폰켓몬 개수 */
        int ponketCnt = nums.length;
        /* 선택가능한 개수 */
        int selectableCount = ponketCnt /2;

        /* 같은종류 제거용 Set*/
        Set<Integer> ponketKindSet = new HashSet<>();
        for (int kind : nums) {
            ponketKindSet.add(kind);
        }
        int ponketKindCnt = ponketKindSet.size();
        /* 선택가능한거보다 종류가 더적으면 종류, 아니면 선택개수*/
        return ponketKindCnt < selectableCount ? ponketKindCnt : selectableCount;
    }
}

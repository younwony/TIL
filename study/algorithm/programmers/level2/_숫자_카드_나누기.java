package level_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class _숫자_카드_나누기 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{10,17}, new int[]{5, 20}));
        System.out.println(solution(new int[]{10,20}, new int[]{5, 17}));
        System.out.println(solution(new int[]{14,35,119}, new int[]{18, 30, 102}));
    }

    public static int solution(int[] arrayA, int[] arrayB) {
        int answer = 0;
        Arrays.sort(arrayA);
        Arrays.sort(arrayB);

        List<Integer> aGcds = getGCDs(arrayA);
        List<Integer> bGcds = getGCDs(arrayB);

        int aMax = getMaxA(aGcds, arrayB);
        int bMax = getMaxA(bGcds, arrayA);

        answer = Math.max(aMax, bMax);
        
        return answer;
    }

    private static List<Integer> getGCDs(int[] arrayA) {
        List<Integer> aMaxList = new ArrayList<>();
        int max = arrayA[0];
        for(int i = 2; i <= max; i++){
            boolean isMod = true;
            for(int j = 0; j < arrayA.length; j++){
                if(arrayA[j] % i != 0){
                    isMod = false;
                    break;
                }
            }

            if(isMod){
                aMaxList.add(i);
            }
        }


        return aMaxList;
    }

    private static int getMaxA(List<Integer> aGcds, int[] arrayB){
        for(int i = aGcds.size() - 1; i >= 0; i--) {

            boolean isMod = true;

            for(int j = 0 ; j < arrayB.length; j++){
                if(arrayB[j] % aGcds.get(i) == 0){
                    isMod = false;
                    break;
                }
            }

            if(isMod){
                return aGcds.get(i);
            }
        }

        return 0;
    }
}

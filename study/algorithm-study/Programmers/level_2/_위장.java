package level_2;

import java.util.*;

public class _위장 {

    public static void main(String[] args) {
        System.out.println(solution(new String[][]{{"yellowhat", "headgear"}, {"bluesunglasses", "eyewear"}, {"green_turban", "headgear"}}));
        System.out.println(solution(new String[][]{{"crowmask", "face"}, {"bluesunglasses", "face"}, {"smoky_makeup", "face"}}));
//        System.out.println(solution(new String[][]{{"yellow", "eyes"}, {"yellowhat", "headgear"}, {"bluesunglasses", "eyewear"}, {"green_turban", "headgear"}}));
    }

    /**
     * 1. 각각의 옷의 종류에 옷을 입지않는 경우를 더해 경우의수를 구해준다.
     * 2. 마지막 결과값에 옷을 모두 입지 않은 경우의 수 1가지를 제거한다.
     */
    private static int solution(String[][] clothes) {
        int answer = 1;

        HashMap<String, Integer> clotheMap = new HashMap<>();

        /* 옷 종류별로 개수 */
        for (String[] clothe : clothes) {
            String kind = clothe[1];
            clotheMap.put(kind, clotheMap.getOrDefault(kind, 0) + 1);
        }

        /* 입는 경우의수 구하기*/
        for (String kind : clotheMap.keySet()) {
            /*옷의 종류수에 입지않은 경우의수 1을 더한다. */
            answer *= clotheMap.get(kind)+1;
        }

        return answer-1;
    }

    /**
     * 조합 을 모두 구해서 하나한 계산한 공식으로
     * 1번 TestCase에서 시간초과발생
     */
    private static int solution2(String[][] clothes) {
        int answer = 0;

        HashMap<String, List> clotheMap = new HashMap<>();

        for (String[] clothe : clothes) {
            String kind = clothe[1];
            String name = clothe[0];
            List clotheMapOrDefault = clotheMap.getOrDefault(kind, new ArrayList());
            clotheMapOrDefault.add(name);
            clotheMap.put(kind, clotheMapOrDefault);
        }

        /* 옷 종류 리스트 */
        ArrayList<String > kindList = new ArrayList<>(clotheMap.keySet());

        for(int depth = 1; depth <= kindList.size(); depth++){
            Set<String> combinationSet = new HashSet<>();
            for (int i = 0; i < kindList.size(); i++) {
                combination(combinationSet, kindList, kindList.get(i), depth,1, i+1);
            }
            answer += sumCombinationSet(combinationSet, clotheMap);
        }

        return answer;
    }

    private static int sumCombinationSet(Set<String> combinationSet, HashMap<String, List> clotheMap) {

        int result = 0;

        for (String combination : combinationSet) {
            String[] combinationSplit = combination.split(",");
            int kindCnt = 1;
            for (String kind : combinationSplit) {
                kindCnt *= clotheMap.get(kind).size();
            }
            result += kindCnt;
        }

        return result;
    }

    private static void combination(Set<String> combinationSet, ArrayList<String> kindList, String kind, int depth,int depthIndex, int index) {
        if(depth == depthIndex){
            combinationSet.add(kind);
            return;
        }

        for (int i = index; i < kindList.size(); i++) {
            combination(combinationSet, kindList, kind + "," + kindList.get(i), depth, depthIndex+1,i+1);
        }
    }

}

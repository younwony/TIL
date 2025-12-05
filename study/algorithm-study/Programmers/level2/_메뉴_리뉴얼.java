package me.wony.level_2;

import java.util.*;

public class _메뉴_리뉴얼 {
    /*
    코스요리 구성하기
     이전에 각 손님들이 주문할 때 가장 많이 함께 주문한 단품메뉴들 대상
     코스요리 메뉴는 최소 2가지 이상의 단품메뉴로 구성
     최소 2명 이상의 손님으로부터 주문된 단품메뉴 조합에 대해서만 코스요리 메뉴 후보 대상
     2 <= order <= 20
     2 <= order[i] <= 10
     order => 대문자로 구성
     order => 중복 X

     1 <= course <= 10
     2 <= course[i] <= 10
     course 오름차순 정렬 되있다.
     course => 중복 X

     result
     1. 오름차순으로 정렬
     2. 메뉴 구성이 여러개라면 모드 담아 return
     3. return 배열이 1이상 되도록 주어진다.
    * */
    public static void main(String[] args) {
        /*System.out.println(solution(new String[]{"ABCFG", "AC", "CDE", "ACDE", "BCFG", "ACDEH"}, new int[]{2,3,4}));*/
        /*System.out.println(solution(new String[]{"ABCDE", "AB", "CD", "ADE", "XYZ", "XYZ", "ACD"}, new int[]{2,3,5}));*/
        System.out.println(solution(new String[]{"XYZ", "XWY", "WXA"}, new int[]{2,3,4}));
    }

    private static String[] solution(String[] orders, int[] course) {

        /* 결과 리스트*/
        ArrayList<String> answerList = new ArrayList<>();

        /* 요청 코스만큼 반복*/
       for(int i = 0; i < course.length; i++){
           /* 주문별로 코스 개수를 저장할 맵 설정 예
           course = 2;
           ABC => "AB", 1 / "AC",1 / "BC", 1
           ABD => "AB", 2 / "AD",1 / "BD", 1
           * */
            HashMap<String, Integer> courCheckMap = new HashMap<>();
            for (String order : orders) {
                orderCombination(0,"", order, course[i], courCheckMap);
            }
            /* 주문내용중에 코스에 맞는게 없을 경우 다음 코스로 넘어가기*/
            if(courCheckMap.size() == 0) continue;

            /* 만들어진 코스중 많이 주문된 코스 결과 리스트에 담기(중복포함)*/
            maxCourseStr(courCheckMap, answerList);
        }

       /* 결과값이 오름차순이여야하므로 정렬 */
        Collections.sort(answerList);

        /* Output String 배열에 담기*/
        String[] answer = new String[answerList.size()];
        for (int i = 0; i < answer.length; i++) {
            answer[i] = answerList.get(i);
        }

        return answer;
    }

    /* 최대 코스 스트링 구하는 함수*/
    private static void maxCourseStr(HashMap<String, Integer> courCheckMap, ArrayList<String> answerList) {
        /* Map에 담겨진 Value를 대상으로 정렬하기위해 Comparator사용*/
        ArrayList<String> keySet = new ArrayList<>(courCheckMap.keySet());
        Collections.sort(keySet, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                /* 최대값이므로 o2를 대상으로 비교*/
                return courCheckMap.get(o2).compareTo(courCheckMap.get(o1));
            }
        });

        /* 최대값은 첫번째이므로 값가져오기 */
        int max = courCheckMap.get(keySet.get(0));
        /* 최대값이 중복일 경우에도 출력하는게 조건이므로 같은 Max일경우 계속 삽입*/
        for (String key : keySet) {
            Integer value = courCheckMap.get(key);
            /* 조건중 2인 이상 주문한게 아닐경우에는 패스*/
            if(value < 2) break;
            if(value == max)
                answerList.add(key);
            else break;
        }
    }

    /* 주문중 코스개수에맞는 문자열을 추출하기위한 조합*/
    private static void orderCombination(int index,String courseStr, String order, int cour, HashMap<String, Integer> courCheckMap) {

        /*코스에 맞는 스트링일 경우 삽입*/
        if(courseStr.length() == cour) {
            /* WX == XW 이므로 입력된 문자열을 정렬시켜준다.*/
            courseStr = courseStrOrder(courseStr);
            /* 주어진 Map에 삽입*/
            courCheckMap.put(courseStr, courCheckMap.getOrDefault(courseStr, 0) + 1);
            return;
        }

        /* 조합 함수 */
        for(; index < order.length(); index++){
            orderCombination(index + 1, courseStr + order.charAt(index), order, cour, courCheckMap);
        }
    }

    /* 스트링 정렬 함수*/
    private static String courseStrOrder(String courseStr) {
        char[] chars = courseStr.toCharArray();
        Arrays.sort(chars);
        return String.valueOf(chars);
    }
}

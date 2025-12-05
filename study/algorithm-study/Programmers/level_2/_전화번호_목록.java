package level_2;

import java.util.*;

public class _전화번호_목록 {

    public static void main(String[] args) {
        System.out.println(solution(new String[]{"119", "97674223", "1195524421"}));
        System.out.println(solution(new String[]{"123","456","789","321"}));
        System.out.println(solution(new String[]{"12","123","1235","567","88"}));
    }

    /**
     * 21.03.04 이후 솔루션
     * 기존의 솔루션으로는 효율성 3,4번에서 시간초과
     */
    private static boolean solution(String[] phone_book) {

        Arrays.sort(phone_book, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.length() - o1.length();
            }
        });

        Set<String> preSet = new HashSet<>();

        /* 최소 길이 문자열의 길이 수*/
        int minLen = phone_book[phone_book.length-1].length();
        for (String phone : phone_book) {
            /* 현재 PhoneNumber가 접두사 Set에 있는지 체크 */
            if(preSet.contains(phone)){
                return false;
            }

            /* 현재 Phone의 최소 길이 문자열의 길이 수 부터 Phone의 문자열까지의 String값을 Set에 넣기*/
            for(int i = minLen; i <= phone.length(); i++){
                preSet.add(phone.substring(0,i));
            }
        }
        return true;
    }

    /**
     * 21.03.04 이전 솔루션
     */
    private static boolean preSolution(String[] phone_book) {
        for(int i=0; i<phone_book.length-1; i++) {
            for(int j=i+1; j<phone_book.length; j++) {
                if(phone_book[i].startsWith(phone_book[j])) {return false;}
                if(phone_book[j].startsWith(phone_book[i])) {return false;}
            }
        }
        return true;
    }
}

package level_2;

import java.util.HashMap;

public class _할인_행사 {
    public static void main(String[] args) {
        System.out.println(solution(new String[]{"banana", "apple", "rice", "pork", "pot"}, new int[]{3, 2, 2, 2, 1}, new String[]{"chicken", "apple", "apple", "banana", "rice", "apple", "pork", "banana", "pork", "rice", "pot", "banana", "apple", "banana"}));
    }

    public static int solution(String[] want, int[] number, String[] discount) {
        int answer = 0;

        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < want.length; i++) {
            map.put(want[i], number[i]);
        }

        for (int i = 0; i <= discount.length - 10; i++){
            HashMap<String , Integer> temp = (HashMap<String, Integer>) map.clone();

            for(int j = i; j < i + 10; j++){
                String item = discount[j];

                if(temp.containsKey(item)){
                    int cnt = temp.get(item);
                    if(cnt == 1){
                        temp.remove(item);
                    }else{
                        temp.put(item, cnt - 1);
                    }
                }
            }

            if(temp.isEmpty()){
                answer++;
            }
        }

        return answer;
    }
}

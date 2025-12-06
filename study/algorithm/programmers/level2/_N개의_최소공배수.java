package me.wony.level_2;

import java.util.ArrayList;

public class _N개의_최소공배수 {

    public static void main(String[] args) {
//        System.out.println(solution(new int[]{2,6,8,14}));
        System.out.println(solution(new int[]{2,6,12,16}));
//        System.out.println(solution(new int[]{1,2,3}));
//        System.out.println(solution(new int[]{2,2}));
    }

    private static int solution(int[] arr) {
        int answer = 1;
        ArrayList<Integer> valueList = new ArrayList<>();
        int max;
        int cnt;
        while(true){
            max = getMax(arr);
            int index = 2;
            for(; index <= max; index++){
                cnt = 0;
                for (int value : arr) {
                    if(value % index == 0) cnt++;
                }
                if(cnt > 1) {
                    valueList.add(index);
                    for (int i = 0; i < arr.length; i++) {
                        if(arr[i] % index == 0)
                            arr[i] /= index;
                    }
                    break;
                }
            }
            if(index > max) break;
        }
        for (Integer integer : valueList) {
            answer *= integer;
        }
        for (int i : arr) {
            answer *= i;
        }
        return answer;
    }

    private static int getMax(int[] arr) {
        int max = 0;
        for (int i : arr) {
            max = Math.max(max, i);
        }
        return max;
    }
}

package level_2;

import java.util.ArrayList;

public class _캐시 {
    public static void main(String[] args) {
        System.out.println(solution(3, new String[]{"Jeju", "Pangyo", "Seoul", "NewYork", "LA", "Jeju", "Pangyo", "Seoul", "NewYork", "LA"}));
        System.out.println(solution(3, new String[]{"Jeju", "Pangyo", "Seoul", "Jeju", "Pangyo", "Seoul", "Jeju", "Pangyo", "Seoul"}));
        System.out.println(solution(2, new String[]{"Jeju", "Pangyo", "Seoul", "NewYork", "LA", "SanFrancisco", "Seoul", "Rome", "Paris", "Jeju", "NewYork", "Rome"}));
        System.out.println(solution(5, new String[]{"Jeju", "Pangyo", "Seoul", "NewYork", "LA", "SanFrancisco", "Seoul", "Rome", "Paris", "Jeju", "NewYork", "Rome"}));
        System.out.println(solution(2, new String[]{"Jeju", "Pangyo", "NewYork", "newyork"}));
        System.out.println(solution(5, new String[]{"NewYork","NewYork", "newyork"}));
        System.out.println(solution(0, new String[]{"Jeju", "Pangyo", "Seoul", "NewYork", "LA"}));
    }
    private static int solution(int cacheSize, String[] cities) {
        int answer = 0;
        if(cacheSize == 0) return cities.length*5;
        ArrayList<String> lruList = new ArrayList<>();
        for (String city : cities) {
            city = city.toUpperCase();
            /* cache hit */
            if(lruList.contains(city)){
                lruList.remove(city);
                lruList.add(city);
                answer++;
                continue;
            }

            /* cache size < cacheSize */
            if(lruList.size() < cacheSize){
                lruList.add(city);
                answer += 5;
                continue;
            }

            /* cache miss */
            lruList.remove(0);
            lruList.add(city);
            answer += 5;
        }
        return answer;
    }
}

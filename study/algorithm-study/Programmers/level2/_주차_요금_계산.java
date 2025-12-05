package level_2;

import java.util.HashMap;
import java.util.TreeMap;

public class _주차_요금_계산 {

    public static void main(String[] args) {
        int[] fees = {180, 5000, 10, 600};
        int[] fees2 = {120, 0, 60, 591};
        int[] fees3 = {1, 461, 1, 10};

        String[] records = {"05:34 5961 IN", "06:00 0000 IN", "06:34 0000 OUT", "07:59 5961 OUT", "07:59 0148 IN", "18:59 0000 IN", "19:09 0148 OUT", "22:59 5961 IN", "23:00 5961 OUT"};
        String[] records2 = {"16:00 3961 IN", "16:00 0202 IN", "18:00 3961 OUT", "18:00 0202 OUT", "23:58 3961 IN"};
        String[] records3 = {"23:58 3961 IN"};
//        String[] records3 = {"00:00 1234 IN"};

        int[] answer = solution(fees, records);
        for (int i = 0; i < answer.length; i++) {
            System.out.print(answer[i] + " ");
        }

        System.out.println();

        int[] answer2 = solution(fees2, records2);
        for (int i = 0; i < answer2.length; i++) {
            System.out.print(answer2[i] + " ");
        }

        System.out.println();

        int[] answer3 = solution(fees3, records3);
        for (int i = 0; i < answer3.length; i++) {
            System.out.print(answer3[i] + " ");
        }

    }

    public static int[] solution(int[] fees, String[] records) {
        int lastTime = 24 * 60 - 1;
        Fee fee = new Fee(fees);

        HashMap<String, String> parkingLoot = new HashMap<>();
        HashMap<String, Integer> parkingTimes = new HashMap<>();
        TreeMap<String, Integer> carCoasts = new TreeMap<>();

        for (String record : records) {
            String[] recordSplit = record.split(" ");

            String time = recordSplit[0];
            String carNumber = recordSplit[1];
            String status = recordSplit[2];

            if("IN".equals(status)){
                parkingLoot.put(carNumber, time);
            }

            if("OUT".equals(status)){
                int inTimeMin = convertMinute(parkingLoot.get(carNumber));
                int outTimeMin = convertMinute(time);
                int parkingTime = outTimeMin - inTimeMin;
                parkingTimes.put(carNumber, parkingTimes.getOrDefault(carNumber, 0) + parkingTime);
                parkingLoot.remove(carNumber);
            }
        }


        for (String carNumber : parkingLoot.keySet()) {
            int inTimeMin = convertMinute(parkingLoot.get(carNumber));
            int parkingTime = lastTime - inTimeMin;
            parkingTimes.put(carNumber, parkingTimes.getOrDefault(carNumber, 0) + parkingTime);
        }

        for (String carNumber : parkingTimes.keySet()) {
            Integer parkingTime = parkingTimes.get(carNumber);
            int cost = fee.getCost(parkingTime);
            carCoasts.put(carNumber, cost);
        }

        return carCoasts.values().stream()
                .mapToInt(value -> value)
                .toArray();
    }

    private static int convertMinute(String inTime) {

        String[] timeSplit = inTime.split(":");
        int hour = Integer.parseInt(timeSplit[0]);
        int min = Integer.parseInt(timeSplit[1]);

        return hour * 60 + min;
    }

    public static class Fee {
        private int defaultMin;
        private int defaultCost;
        private int unitTime;
        private int unitCost;

        public Fee(int[] fees) {
            this.defaultMin = fees[0];
            this.defaultCost = fees[1];
            this.unitTime = fees[2];
            this.unitCost = fees[3];
        }

        public int getCost(int parkingTime){
            if (parkingTime < defaultMin) {
                return defaultCost;
            }

            return defaultCost + ((int) Math.ceil((double) (parkingTime - defaultMin) / unitTime)) * unitCost;
        }
    }
}

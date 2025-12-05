package challenge.dev_matching_2021_2;

import java.util.HashMap;
import java.util.Map;

/**
 * enroll의 길이는 1 이상 10,000 이하입니다.
 * enroll에 민호의 이름은 없습니다. 따라서 enroll의 길이는 민호를 제외한 조직 구성원의 총 수입니다.
 * referral의 길이는 enroll의 길이와 같습니다.
 * referral 내에서 i 번째에 있는 이름은 배열 enroll 내에서 i 번째에 있는 판매원을 조직에 참여시킨 사람의 이름입니다.
 * 어느 누구의 추천도 없이 조직에 참여한 사람에 대해서는 referral 배열 내에 추천인의 이름이 기입되지 않고 “-“ 가 기입됩니다. 위 예제에서는 john 과 mary 가 이러한 예에 해당합니다.
 * enroll 에 등장하는 이름은 조직에 참여한 순서에 따릅니다.
 * 즉, 어느 판매원의 이름이 enroll 의 i 번째에 등장한다면, 이 판매원을 조직에 참여시킨 사람의 이름, 즉 referral 의 i 번째 원소는 이미 배열 enroll 의 j 번째 (j < i) 에 등장했음이 보장됩니다.
 * seller의 길이는 1 이상 100,000 이하입니다.
 * seller 내의 i 번째에 있는 이름은 i 번째 판매 집계 데이터가 어느 판매원에 의한 것인지를 나타냅니다.
 * seller 에는 같은 이름이 중복해서 들어있을 수 있습니다.
 * amount의 길이는 seller의 길이와 같습니다.
 * amount 내의 i 번째에 있는 수는 i 번째 판매 집계 데이터의 판매량을 나타냅니다.
 * 판매량의 범위, 즉 amount 의 원소들의 범위는 1 이상 100 이하인 자연수입니다.
 * 칫솔 한 개를 판매하여 얻어지는 이익은 100 원으로 정해져 있습니다.
 * 모든 조직 구성원들의 이름은 10 글자 이내의 영문 알파벳 소문자들로만 이루어져 있습니다.
 */
public class _다단계_칫솔_판매 {
    public static void main(String[] args) {
        int[] solution = solution(new String[]{"john", "mary", "edward", "sam", "emily", "jaimie", "tod", "young"},
                new String[]{"-", "-", "mary", "edward", "mary", "mary", "jaimie", "edward"},
                new String[]{"young", "john", "tod", "emily", "mary"},
                new int[]{12, 4, 2, 5, 10}
        );

        for (int i : solution) {
            System.out.println(i);
        }
    }

    private static Map<String, Datum> map = new HashMap<>();
    public static int[] solution(String[] enroll, String[] referral, String[] seller, int[] amount) {
        int[] answer = new int[enroll.length];


        for (int i = 0; i < enroll.length; i++) {
            String name = enroll[i];
            String referName = referral[i];
            map.put(name, new Datum(referName));
        }

        for (int i = 0; i < seller.length; i++) {
            String name = seller[i];
            Datum nameData = map.get(name);
            nameData.addAmount(amount[i]);
            nameData.addPrice(amount[i] * 90);
            nameData.setCommission(amount[i] * 10);
            map.put(name, nameData);
            calculation(name);
        }

        for (int i = 0; i < enroll.length; i++) {
            Datum datum = map.get(enroll[i]);
            answer[i] = datum.price;
        }

        return answer;
    }

    private static void calculation(String name) {
        if("-".equals(name)) return;
        Datum datum = map.get(name);
        String referName = datum.referName;
        int nextCommission = datum.commission / 10;
        int addCommission = datum.commission - nextCommission;
        if("-".equals(referName) || addCommission == 0) return;

        Datum referData = map.get(referName);
        referData.addPrice(addCommission);
        referData.setCommission(nextCommission);
        map.put(referName, referData);
        calculation(referName);
    }

    static class Datum{
        String referName;
        int amount;
        int price;
        int commission;

        public Datum(String referName) {
            this.referName = referName;
            this.amount = 0;
            this.price = 0;
            this.commission = 0;
        }

        public void setCommission(int commission){
            this.commission = commission;
        }

        public void addAmount(int amount){
            this.amount += amount;
        }

        public void addPrice(int price){
            this.price += price;
        }
    }
}

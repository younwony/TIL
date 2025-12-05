package _1_Silver.Level_4;

import java.util.*;

public class a11652_카드 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        Map<Long, Integer> map = new HashMap<>();

        long number = 0;

        for(int i = 0; i < n; i++){
            number = scanner.nextLong();
            if(map.containsKey(number)){
                map.put(number, map.get(number) + 1);
            }else{
                map.put(number, 1);
            }
        }

        ArrayList<Card> cardList = new ArrayList<>();
        for(long i : map.keySet()){
            cardList.add(new Card(i, map.get(i)));
        }

        Collections.sort(cardList, new Card());

        System.out.println(cardList.get(0).number);

    }
    static class Card implements Comparator<Card> {

        long number;
        int count;

        public Card(long number, int count) {
            this.number = number;
            this.count = count;
        }

        public Card() {
        }

        @Override
        public int compare(Card o1, Card o2) {
            if(o1.count < o2.count){
                return 1;
            }else if(o1.count > o2.count){
                return -1;
            }else{
                if(o1.number < o2.number){
                    return -1;
                }else if(o1.number > o2.number){
                    return 1;
                }else{
                    return 0;
                }
            }
        }
    }
}

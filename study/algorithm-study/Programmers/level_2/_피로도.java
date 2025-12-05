package level_2;

public class _피로도 {

    public static void main(String[] args) {

        int k = 80;
        int[][] dungeons = new int[][]{
                {80, 20},
                {50, 40},
                {30, 10}
        };
        System.out.println(solution(k, dungeons));

    }

    private static int answer = 0;
    public static int solution(int k, int[][] dungeons) {

        int dungeonSize = dungeons.length;

        Character character = new Character(k,  0, dungeonSize);
        dfs(character, dungeons);

        return answer;
    }

    public static void dfs(Character c, int[][] dungeons){
        for (int i = 0; i < dungeons.length; i++) {
            int[] dungeon = dungeons[i];
            int minFatigue = dungeon[0];
            int consumptionFatigue = dungeon[1];

            if(c.isExploreDungeon(i, minFatigue)){
                Character nextC = new Character(c);
                nextC.exploreDungeon(i, consumptionFatigue);
                answer = Math.max(answer, nextC.count);
                dfs(nextC, dungeons);
            }
        }
    }

    public static class Character {
        private int fatigue;
        private int count;
        private boolean[] visited;

        public Character(int k , int count, int dungeonSize) {
            this.fatigue = k;
            this.count = count;
            this.visited = new boolean[dungeonSize];
        }

        public Character(Character character) {
            this.fatigue = character.fatigue;
            this.count = character.count;
            this.visited = character.visited.clone();
        }

        public boolean isExploreDungeon(int dungeonNumber, int minFatigue){
            return !visited[dungeonNumber] && fatigue >= minFatigue ;
        }

        public void exploreDungeon(int dungeonNumber, int consumptionFatigue) {
            visited[dungeonNumber] = true;
            this.fatigue -= consumptionFatigue;
            this.count++;
        }
    }
}

package level_1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class _성격_유형_검사하기 {
    public static void main(String[] args) {
        System.out.println(solution(new String[]{"AN", "CF", "MJ", "RT", "NA"}, new int[]{5, 3, 2, 7, 5}));
        System.out.println(solution(new String[]{"TR", "RT", "TR"}, new int[]{7, 1, 3}));
    }

    public static String solution(String[] survey, int[] choices) {
        AtomicReference<String> answer = new AtomicReference<>("");

        int len = survey.length;

        PersonalityType personalityType = new PersonalityType();
        personalityType.createTypeGroup('R','T');
        personalityType.createTypeGroup('C','F');
        personalityType.createTypeGroup('J','M');
        personalityType.createTypeGroup('A','N');

        for (int i = 0; i < len; i++) {
            char[] charArray = survey[i].toCharArray();
            char left = charArray[0];
            char right = charArray[1];

            int choice = choices[i];
            int score = Math.abs(4 - choice);
            if(score == 0) {
                continue;
            }

            if(choice < 4) {
                personalityType.addScore(left, score);
            }

            if(choice > 4) {
                personalityType.addScore(right, score);
            }
        }

        personalityType.getTypes().stream()
                .map(PersonalityType.TypeGroup::getMaxType)
                .forEach(character -> {
                    answer.updateAndGet(v -> v + character);
                });

        return answer.get();
    }

    public static class PersonalityType{
        private List<TypeGroup> types = new ArrayList<>();

        public List<TypeGroup> getTypes() {
            return types;
        }

        public void createTypeGroup(char r, char t) {
            TypeGroup typeGroup = new TypeGroup();
            typeGroup.types.add(new TypeGroup.Type(r));
            typeGroup.types.add(new TypeGroup.Type(t));
            types.add(typeGroup);
        }

        public void addScore(char left, int score) {
            types.stream()
                    .flatMap(typeGroup -> typeGroup.types.stream())
                    .filter(type -> type.type == left)
                    .forEach(type -> type.count += score);
        }

        public static class TypeGroup {
            private List<Type> types = new ArrayList<>();

            public char getMaxType() {
                int max = 0;
                char maxType = types.get(0).type;
                for (Type type : types) {
                    if(type.count > max) {
                        max = type.count;
                        maxType = type.type;
                    }
                }
                return maxType;
            }

            public static class Type {
                private final char type;
                private int count;

                public Type(char type) {
                    this.type = type;
                    this.count = 0;
                }
            }
        }
    }
}

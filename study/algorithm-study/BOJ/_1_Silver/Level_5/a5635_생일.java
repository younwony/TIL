package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class a5635_생일 {
    public static void main(String[] args) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine());

        Person[] people = new Person[n];

        StringTokenizer stringTokenizer;
        for (int i = 0; i < people.length; i++) {
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            people[i] = new Person(stringTokenizer.nextToken()
                    , Integer.parseInt(stringTokenizer.nextToken())
                    , Integer.parseInt(stringTokenizer.nextToken())
                    , Integer.parseInt(stringTokenizer.nextToken())
            );
        }

        Arrays.sort(people, new Person());

        System.out.println(people[n-1].name);
        System.out.println(people[0].name);

    }

    static class Person implements Comparator<Person> {
        String name;
        int d;
        int m;
        int y;

        public Person() {
        }

        public Person(String name, int d, int m, int y) {
            this.name = name;
            this.d = d;
            this.m = m;
            this.y = y;
        }

        @Override
        public int compare(Person o1, Person o2) {
            if(o1.y == o2.y){
                if(o1.m == o2.m){
                    return  o1.d - o2.d;
                }
                return o1.m - o2.m;
            }
            return o1.y - o2.y;
        }
    }
}

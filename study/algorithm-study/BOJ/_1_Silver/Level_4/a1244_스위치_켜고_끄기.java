package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a1244_스위치_켜고_끄기 {

    public static boolean[] switches;

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int switchCnt = Integer.parseInt(bufferedReader.readLine());
        switches = new boolean[switchCnt + 1];
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        for (int i = 1; i <= switchCnt; i++) {
            switches[i] = "1".equals(stringTokenizer.nextToken());
        }

        int personCnt = Integer.parseInt(bufferedReader.readLine());
        Person[] people = new Person[personCnt];
        for (int i = 0; i < personCnt; i++) {
             stringTokenizer = new StringTokenizer(bufferedReader.readLine());
             people[i] = new Person(
                     Integer.parseInt(stringTokenizer.nextToken()),
                     Integer.parseInt(stringTokenizer.nextToken()));
        }

        for (Person person : people) {
            person.execSwitch();
        }

        StringBuilder result = new StringBuilder();
        for (int i = 1; i < switches.length; i++) {
            result.append(switches[i] ? "1 " : "0 ");
            if(i % 20 == 0){
                result.append("\n");
            }
        }

        System.out.println(result.toString());


    }

    private static class Person{
        int sex;
        int switchIndex;

        public Person(int sex, int switchIndex) {
            this.sex = sex;
            this.switchIndex = switchIndex;
        }

        public void execSwitch(){
            if(sex == 1){
                execMale();
            }else{
                execFemale();
            }
        }

        private void execMale() {
            for(int index = this.switchIndex; index < switches.length; index += this.switchIndex){
                switches[index] = !switches[index];
            }
        }

        private void execFemale() {
            switches[this.switchIndex] = !switches[this.switchIndex];
            for(int index = 1; this.switchIndex - index > 0 && this.switchIndex + index < switches.length; index++){
                if(switches[this.switchIndex - index] == switches[this.switchIndex + index]){
                    switches[this.switchIndex - index] = !switches[this.switchIndex - index];
                    switches[this.switchIndex + index] = !switches[this.switchIndex + index];
                }else{
                    break;
                }
            }
        }
    }
}


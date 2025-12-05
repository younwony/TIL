package _0_Bronze.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a5554_심부름_가는_길 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int houseToSchool = Integer.parseInt(bufferedReader.readLine());
        int schoolToPcRoom = Integer.parseInt(bufferedReader.readLine());
        int pcRoomToAcademy = Integer.parseInt(bufferedReader.readLine());
        int academyToHouse = Integer.parseInt(bufferedReader.readLine());

        int sum = houseToSchool + schoolToPcRoom + pcRoomToAcademy + academyToHouse;

        System.out.println(sum / 60);
        System.out.println(sum % 60);
    }
}

package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class a1620_나는야_포켓몬_마스터_이다솜 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = bufferedReader.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);

        HashMap<Integer, String> pokemonMap = new HashMap<>();
        HashMap<String, Integer> pokemonMap2 = new HashMap<>();
        for(int i = 0; i < n; i++){
            String poketmon = bufferedReader.readLine();
            pokemonMap.put(i+1, poketmon);
            pokemonMap2.put(poketmon, i+1);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < m; i++){
            String input = bufferedReader.readLine();
            if(Character.isDigit(input.charAt(0))){
                stringBuilder.append(pokemonMap.get(Integer.parseInt(input))).append("\n");
            }else{
                stringBuilder.append(pokemonMap2.get(input)).append("\n");
            }
        }

        System.out.println(stringBuilder.toString());
    }
}

package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

public class a14247_나무_자르기 {
    static int n;
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(bufferedReader.readLine());

        StringTokenizer Hi = new StringTokenizer(bufferedReader.readLine());
        StringTokenizer Ai = new StringTokenizer(bufferedReader.readLine());
        ArrayList<Tree> trees = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            trees.add(new Tree(Integer.parseInt(Hi.nextToken()), Integer.parseInt(Ai.nextToken())));
        }

        Collections.sort(trees,new Tree());

        long sum = 0;
        for (int i = 0; i < trees.size(); i++) {
            Tree tree = trees.get(i);
            sum += tree.hi + tree.ai * i;
        }

        System.out.println(sum);
    }

    static class Tree implements Comparator<Tree> {
        int hi;
        int ai;

        public Tree() {
        }

        public Tree(int hi, int ai) {
            this.hi = hi;
            this.ai = ai;
        }

        @Override
        public int compare(Tree o1, Tree o2) {
            return o1.ai - o2.ai;
        }
    }
}

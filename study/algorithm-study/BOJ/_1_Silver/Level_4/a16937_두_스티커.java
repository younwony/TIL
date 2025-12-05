package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a16937_두_스티커 {


    private static int MAX = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int H = Integer.parseInt(stringTokenizer.nextToken());
        int W = Integer.parseInt(stringTokenizer.nextToken());
        int N = Integer.parseInt(bufferedReader.readLine());

        int[][] stickers = new int[N][2];

        for (int i = 0; i < stickers.length; i++) {
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            stickers[i][0] = Integer.parseInt(stringTokenizer.nextToken());
            stickers[i][1] = Integer.parseInt(stringTokenizer.nextToken());
        }

        solved(H,W,stickers);
        solved(W,H,stickers);

        System.out.println(MAX);
    }

    private static void solved(int h, int w, int[][] stickers) {
        for (int i = 0; i < stickers.length; i++) {
            int stickerH = stickers[i][0];
            int stickerW = stickers[i][1];

            if(stickerH <= h && stickerW <= w){
                int sum = stickerH * stickerW;
                int tempH = h - stickerH;
                int tempW = w - stickerW;
                nextSticker(stickers, i, sum, tempH, w);
                nextSticker(stickers, i, sum, h, tempW);
            }
        }
    }

    private static void nextSticker(int[][] stickers, int i, int sum, int tempH, int tempW) {
        for (int j = 0; j < stickers.length; j++) {
            if(i != j){
                int nextStickerH = stickers[j][0];
                int nextStickerW = stickers[j][1];

                if(nextStickerH <= tempH && nextStickerW <= tempW){
                    MAX = Math.max(MAX, sum + nextStickerH * nextStickerW);
                }

                if(nextStickerW <= tempH && nextStickerH <= tempW){
                    MAX = Math.max(MAX, sum + nextStickerH * nextStickerW);
                }
            }
        }
    }
}

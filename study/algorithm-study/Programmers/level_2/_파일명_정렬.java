package level_2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class _파일명_정렬 {

    public static void main(String[] args) {
//        System.out.println(solution(new String[]{"img12.png", "img10.png", "img02.png", "img1.png", "IMG01.GIF", "img2.JPG"}));
//        System.out.println(solution(new String[]{"F-5 Freedom Fighter", "B-50 Superfortress", "A-10 Thunderbolt II", "F-14 Tomcat"}));
//        System.out.println(solution(new String[]{"F-512341324 Freedom Fighter", "B-501234324 Superfortress", "A-1012341234 Thunderbolt II", "F-1123412344 Tomcat"}));
        System.out.println(solution(new String[]{"a -.4325.123png", "a .-4.234png"}));
    }

    private static String[] solution(String[] files) {
        ArrayList<FileName> fileNameArrayList = new ArrayList<FileName>();
        for (String file : files) {
            String head = getHead(file);
            int number = getNumber(file.substring(head.length()));
            fileNameArrayList.add(new FileName(file,head,number));
        }

        Collections.sort(fileNameArrayList, new Comparator<FileName>() {
            @Override
            public int compare(FileName o1, FileName o2) {
                if(o1.head.equalsIgnoreCase(o2.head))
                    return o1.number - o2.number;
                else
                    return o1.head.compareTo(o2.head);
            }
        });

        String[] answer = new String[fileNameArrayList.size()];
        for (int i = 0; i < answer.length; i++) {
            answer[i] = fileNameArrayList.get(i).fileName;
        }

        return answer;
    }

    private static String getHead(String file) {
        String head = "";
        for(int i = 0; i < file.length(); i++){
            if (Character.isDigit(file.charAt(i))){
                head = file.substring(0, i);
                break;
            }
        }
        return head;
    }

    private static int getNumber(String file) {
        int endPoint = Math.min(file.length(), 5);
        int result = endPoint;
        for(int i = 0; i < endPoint; i++){
            if (!Character.isDigit(file.charAt(i))){
                result = i;
                break;
            }
        }
        return Integer.parseInt(file.substring(0, result));
    }

    static class FileName{
        String fileName;
        String head;
        int number;

        public FileName(String fileName, String head, int number) {
            this.fileName = fileName;
            this.head = head;
            this.number = number;
        }
    }

}

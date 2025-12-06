package me.wony.level_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class _오픈채팅방 {

    public static void main(String[] args) {
        String[] solution = solution(new String[]{"Enter uid1234 Muzi", "Enter uid4567 Prodo", "Leave uid1234", "Enter uid1234 Prodo", "Change uid4567 Ryan"});
        for (String s : solution) {
            System.out.println(s);
        }
    }

    private static String[] solution(String[] record) {
        ArrayList<String> messageList = new ArrayList<>();
        HashMap<String,String> idMap = setChangeId(record);
        for (String message : record) {
            if(message.startsWith("Change")) continue;
            String[] recordArray = message.split(" ");
            String action = recordArray[0];
            String uid = recordArray[1];
            String name = idMap.get(uid);
            messageList.add(setMessage(action,name));
        }
        String[] answer = new String[messageList.size()];
        for (int i = 0; i < answer.length; i++) {
            answer[i] = messageList.get(i);
        }
        return answer;
    }

    private static String setMessage(String action, String name) {
        StringBuilder message = new StringBuilder();
        message.append(name)
                .append("님이 ")
                .append("Enter".equals(action) ? "들어왔습니다." : "나갔습니다.");
        return message.toString();
    }

    private static HashMap<String,String> setChangeId(String[] record) {
        HashMap<String,String> idMap = new HashMap<>();
        for (String s : record) {
            String[] recordArray = s.split(" ");
            String action = recordArray[0];
            if("Leave".equals(action)){
                continue;
            }
            idMap.put(recordArray[1],recordArray[2]);
        }
        return idMap;
    }
}

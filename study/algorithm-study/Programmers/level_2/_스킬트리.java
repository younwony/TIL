package level_2;

import java.util.regex.Pattern;

public class _스킬트리 {

    public static void main(String[] args) {
        System.out.println(solution("CBD", new String[]{"BACDE", "CBADF", "AECB", "BDA"}));
    }

    private static int solution(String skill, String[] skill_trees) {
        int answer = 0;

        /* 스킬 트리 입력값 배열 */
        skill_tree_loop:
        for (String skill_tree : skill_trees) {

            /* 뒤에서 부터 스킬 체크*/
            for(int i = skill.length() - 1; i >= 0; i--){
                char nowSkill = skill.charAt(i);
                int skillIndex = skill_tree.indexOf(nowSkill);
                /* 현재 스킬 없으면 패스*/
                if(skillIndex == -1) continue;

                /* 현재스킬이 있을 경우 현재 스킬까지 스킬 자르기*/
                String preSkill_tree = skill_tree.substring(0,skillIndex);

                /* 현재 스킬 까지 자른 스킬트리에서 선행 스킬들이 있는지 체크 */
                for(int j = i-1; j >=0; j-- ){
                    char preeSkill = skill.charAt(j);
                    int preeSkillIndex = preSkill_tree.indexOf(preeSkill);
                    if(preeSkillIndex == -1) continue skill_tree_loop;
                    preSkill_tree = preSkill_tree.substring(0,preeSkillIndex);
                }
            }
            answer++;
        }
        return answer;
    }
}

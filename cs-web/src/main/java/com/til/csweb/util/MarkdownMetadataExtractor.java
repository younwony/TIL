package com.til.csweb.util;

import com.til.csweb.domain.LevelInfo;
import com.til.csweb.domain.PrerequisiteInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import static com.til.csweb.constant.DocumentConstants.*;

/**
 * 마크다운 메타데이터 추출기
 */
@Component
public class MarkdownMetadataExtractor {

    /**
     * 마크다운에서 제목(H1) 추출
     */
    public String extractTitle(String markdown) {
        String[] lines = markdown.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("# ")) {
                return trimmed.substring(2).trim();
            }
        }
        return DEFAULT_TITLE;
    }

    /**
     * 마크다운에서 난이도 레벨 추출
     */
    public LevelInfo extractLevel(String markdown) {
        String[] lines = markdown.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();

            if (trimmed.contains("난이도") || trimmed.contains("레벨") || trimmed.contains("Difficulty")) {
                String cleaned = trimmed.replaceAll("\\*\\*", "").replaceAll("\\*", "");
                int colonIndex = cleaned.indexOf(":");
                if (colonIndex != -1) {
                    String levelPart = cleaned.substring(colonIndex + 1).trim();
                    Matcher matcher = LEVEL_PATTERN.matcher(levelPart);
                    if (matcher.find()) {
                        int level = Integer.parseInt(matcher.group(1));
                        String name = cleanLevelName(matcher.group(2).trim());
                        return new LevelInfo(level, name);
                    }
                }
            }

            if (trimmed.startsWith(">")) {
                String content = trimmed.substring(1).trim().replaceAll("`", "");
                Matcher matcher = LEVEL_PATTERN.matcher(content);
                if (matcher.find()) {
                    int level = Integer.parseInt(matcher.group(1));
                    String name = cleanLevelName(matcher.group(2).trim());
                    return new LevelInfo(level, name);
                }
            }
        }

        return LevelInfo.empty();
    }

    /**
     * 마크다운에서 설명 추출
     */
    public String extractDescription(String markdown) {
        String[] lines = markdown.split("\n");
        StringBuilder description = new StringBuilder();
        boolean foundContent = false;

        for (String line : lines) {
            String trimmed = line.trim();

            if (trimmed.startsWith("#")) {
                continue;
            }

            if (trimmed.contains("난이도") || trimmed.contains("레벨") || trimmed.contains("Difficulty")) {
                continue;
            }

            if (trimmed.isEmpty() && !foundContent) {
                continue;
            }

            if (trimmed.startsWith(">")) {
                String quote = trimmed.substring(1).trim().replaceAll("`", "");
                if (!quote.isEmpty() && !LEVEL_PATTERN.matcher(quote).matches()) {
                    return truncate(quote);
                }
                continue;
            }

            if (!trimmed.isEmpty() && !trimmed.startsWith("-") && !trimmed.startsWith("|")) {
                foundContent = true;
                description.append(trimmed).append(" ");
                if (description.length() > DESCRIPTION_MAX_LENGTH) {
                    break;
                }
            }

            if (foundContent && trimmed.isEmpty()) {
                break;
            }
        }

        return truncate(description.toString().trim());
    }

    /**
     * 마크다운에서 선수 지식 추출
     */
    public List<PrerequisiteInfo> extractPrerequisites(String markdown, String currentCategory) {
        List<PrerequisiteInfo> prerequisites = new ArrayList<>();
        String[] lines = markdown.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();

            if (trimmed.contains("선수 지식") || trimmed.contains("선행 지식")) {
                if (trimmed.contains("없음") || trimmed.contains("없습니다")) {
                    continue;
                }

                Matcher matcher = PREREQUISITE_LINK_PATTERN.matcher(trimmed);
                while (matcher.find()) {
                    String title = matcher.group(1).trim();
                    String link = matcher.group(2).trim();

                    if (!link.startsWith("http")) {
                        Optional<String> path = PathResolver.convertToWebPath(link, currentCategory);
                        path.ifPresent(p -> prerequisites.add(new PrerequisiteInfo(title, p)));
                    }
                }

                if (!prerequisites.isEmpty()) {
                    break;
                }
            }

            if (prerequisites.size() >= PREREQUISITE_LIMIT) {
                break;
            }
        }

        return prerequisites;
    }

    /**
     * 마크다운에서 키워드 추출
     */
    public List<String> extractKeywords(String markdown) {
        List<String> keywords = new ArrayList<>();
        Matcher matcher = KEYWORD_PATTERN.matcher(markdown);

        while (matcher.find()) {
            String keyword = matcher.group(1).trim();
            if (!keyword.isEmpty() && !keywords.contains(keyword)) {
                keywords.add(keyword);
            }
        }

        return keywords;
    }

    private String cleanLevelName(String name) {
        if (name.contains("·")) {
            name = name.substring(0, name.indexOf("·")).trim();
        }
        if (name.contains("선수 지식")) {
            name = name.substring(0, name.indexOf("선수 지식")).trim();
        }
        if (name.contains("선행 지식")) {
            name = name.substring(0, name.indexOf("선행 지식")).trim();
        }
        return name;
    }

    private String truncate(String text) {
        if (text.length() <= DESCRIPTION_MAX_LENGTH) {
            return text;
        }
        return text.substring(0, DESCRIPTION_MAX_LENGTH).trim() + "...";
    }
}

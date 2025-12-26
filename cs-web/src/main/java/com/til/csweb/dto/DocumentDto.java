package com.til.csweb.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

import static com.til.csweb.constant.DocumentConstants.DISPLAY_KEYWORDS_LIMIT;

/**
 * CS 문서 정보를 담는 DTO
 */
@Getter
@Builder
public class DocumentDto {

    private final String category;
    private final String filename;
    private final String title;
    private final String description;
    private final String htmlContent;
    private final Integer level;
    private final String levelName;

    @Builder.Default
    private final List<Prerequisite> prerequisites = Collections.emptyList();

    @Builder.Default
    private final List<String> keywords = Collections.emptyList();

    /**
     * 레벨 정보가 있는지 확인
     */
    public boolean hasLevel() {
        return level != null && levelName != null;
    }

    /**
     * 선수 지식이 있는지 확인
     */
    public boolean hasPrerequisites() {
        return prerequisites != null && !prerequisites.isEmpty();
    }

    /**
     * 키워드가 있는지 확인
     */
    public boolean hasKeywords() {
        return keywords != null && !keywords.isEmpty();
    }

    /**
     * 표시용 키워드 (최대 5개)
     */
    public List<String> getDisplayKeywords() {
        if (keywords == null || keywords.isEmpty()) {
            return Collections.emptyList();
        }
        return keywords.size() > DISPLAY_KEYWORDS_LIMIT
                ? keywords.subList(0, DISPLAY_KEYWORDS_LIMIT)
                : keywords;
    }

    /**
     * 추가 키워드 개수 (5개 초과 시)
     */
    public int getMoreKeywordsCount() {
        if (keywords == null || keywords.size() <= DISPLAY_KEYWORDS_LIMIT) {
            return 0;
        }
        return keywords.size() - DISPLAY_KEYWORDS_LIMIT;
    }

    /**
     * 문서 상세 페이지 URL 경로 반환
     */
    public String getPath() {
        return "/docs/" + category + "/" + filename;
    }

    /**
     * 선수 지식 정보를 담는 내부 클래스
     */
    @Getter
    @RequiredArgsConstructor
    public static class Prerequisite {
        private final String title;
        private final String path;
    }
}

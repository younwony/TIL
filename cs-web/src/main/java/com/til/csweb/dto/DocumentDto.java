package com.til.csweb.dto;

import java.util.Collections;
import java.util.List;

/**
 * CS 문서 정보를 담는 DTO
 */
public class DocumentDto {

    private final String category;
    private final String filename;
    private final String title;
    private final String description;
    private final String htmlContent;
    private final Integer level;
    private final String levelName;
    private final List<Prerequisite> prerequisites;

    private DocumentDto(Builder builder) {
        this.category = builder.category;
        this.filename = builder.filename;
        this.title = builder.title;
        this.description = builder.description;
        this.htmlContent = builder.htmlContent;
        this.level = builder.level;
        this.levelName = builder.levelName;
        this.prerequisites = builder.prerequisites != null ? builder.prerequisites : Collections.emptyList();
    }

    public String getCategory() {
        return category;
    }

    public String getFilename() {
        return filename;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public Integer getLevel() {
        return level;
    }

    public String getLevelName() {
        return levelName;
    }

    public List<Prerequisite> getPrerequisites() {
        return prerequisites;
    }

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
     * 문서 상세 페이지 URL 경로 반환
     */
    public String getPath() {
        return "/docs/" + category + "/" + filename;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String category;
        private String filename;
        private String title;
        private String description;
        private String htmlContent;
        private Integer level;
        private String levelName;
        private List<Prerequisite> prerequisites;

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder htmlContent(String htmlContent) {
            this.htmlContent = htmlContent;
            return this;
        }

        public Builder level(Integer level) {
            this.level = level;
            return this;
        }

        public Builder levelName(String levelName) {
            this.levelName = levelName;
            return this;
        }

        public Builder prerequisites(List<Prerequisite> prerequisites) {
            this.prerequisites = prerequisites;
            return this;
        }

        public DocumentDto build() {
            return new DocumentDto(this);
        }
    }

    /**
     * 선수 지식 정보를 담는 내부 클래스
     */
    public static class Prerequisite {
        private final String title;
        private final String path;

        public Prerequisite(String title, String path) {
            this.title = title;
            this.path = path;
        }

        public String getTitle() {
            return title;
        }

        public String getPath() {
            return path;
        }
    }
}

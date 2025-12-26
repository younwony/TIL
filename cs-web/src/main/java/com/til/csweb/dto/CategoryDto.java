package com.til.csweb.dto;

import java.util.Collections;
import java.util.List;

/**
 * 카테고리 정보를 담는 DTO (하위 카테고리 지원)
 */
public class CategoryDto {

    private final String name;
    private final String path;
    private final String parentCategory;
    private final List<CategoryDto> subcategories;
    private final List<DocumentDto> documents;
    private final int totalDocumentCount;

    private CategoryDto(Builder builder) {
        this.name = builder.name;
        this.path = builder.path;
        this.parentCategory = builder.parentCategory;
        this.subcategories = builder.subcategories != null ? builder.subcategories : Collections.emptyList();
        this.documents = builder.documents != null ? builder.documents : Collections.emptyList();
        this.totalDocumentCount = builder.totalDocumentCount;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public List<CategoryDto> getSubcategories() {
        return subcategories;
    }

    public List<DocumentDto> getDocuments() {
        return documents;
    }

    public int getTotalDocumentCount() {
        return totalDocumentCount;
    }

    /**
     * 하위 카테고리가 있는지 확인
     */
    public boolean hasSubcategories() {
        return subcategories != null && !subcategories.isEmpty();
    }

    /**
     * 직접 포함된 문서가 있는지 확인
     */
    public boolean hasDocuments() {
        return documents != null && !documents.isEmpty();
    }

    /**
     * 카테고리 URL 경로 반환
     */
    public String getCategoryPath() {
        return "/category/" + path;
    }

    /**
     * 전체 경로 (부모 카테고리 포함) 반환
     */
    public String getFullPath() {
        if (parentCategory != null && !parentCategory.isEmpty()) {
            return parentCategory + "/" + name;
        }
        return name;
    }

    /**
     * 표시할 문서 수 (직접 문서 + 하위 카테고리 문서)
     */
    public int getDisplayDocumentCount() {
        if (totalDocumentCount > 0) {
            return totalDocumentCount;
        }
        int count = documents.size();
        for (CategoryDto sub : subcategories) {
            count += sub.getDisplayDocumentCount();
        }
        return count;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String path;
        private String parentCategory;
        private List<CategoryDto> subcategories;
        private List<DocumentDto> documents;
        private int totalDocumentCount;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder parentCategory(String parentCategory) {
            this.parentCategory = parentCategory;
            return this;
        }

        public Builder subcategories(List<CategoryDto> subcategories) {
            this.subcategories = subcategories;
            return this;
        }

        public Builder documents(List<DocumentDto> documents) {
            this.documents = documents;
            return this;
        }

        public Builder totalDocumentCount(int totalDocumentCount) {
            this.totalDocumentCount = totalDocumentCount;
            return this;
        }

        public CategoryDto build() {
            return new CategoryDto(this);
        }
    }
}

package com.til.csweb.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * 카테고리 정보를 담는 DTO (하위 카테고리 지원)
 */
@Getter
@Builder
public class CategoryDto {

    private final String name;
    private final String path;
    private final String parentCategory;

    @Builder.Default
    private final List<CategoryDto> subcategories = Collections.emptyList();

    @Builder.Default
    private final List<DocumentDto> documents = Collections.emptyList();

    private final int totalDocumentCount;

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
}

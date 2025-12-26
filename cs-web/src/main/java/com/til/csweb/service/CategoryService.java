package com.til.csweb.service;

import com.til.csweb.dto.CategoryDto;

import java.util.List;
import java.util.Optional;

/**
 * 카테고리 관리 서비스 인터페이스
 */
public interface CategoryService {

    /**
     * 모든 카테고리 이름 조회
     */
    List<String> getCategories();

    /**
     * 하위 카테고리 이름 조회
     */
    List<String> getSubcategories(String category);

    /**
     * 카테고리 존재 여부 확인
     */
    boolean categoryExists(String category);

    /**
     * 카테고리 상세 정보 조회
     */
    Optional<CategoryDto> getCategoryInfo(String categoryPath);

    /**
     * 모든 카테고리 상세 정보 조회
     */
    List<CategoryDto> getAllCategoryInfos();
}

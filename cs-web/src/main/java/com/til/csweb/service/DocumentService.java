package com.til.csweb.service;

import com.til.csweb.dto.DocumentDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 문서 조회 서비스 인터페이스
 */
public interface DocumentService {

    /**
     * 특정 문서 상세 조회
     */
    Optional<DocumentDto> getDocument(String category, String filename);

    /**
     * 카테고리 내 문서 목록 조회 (직접 포함된 문서만)
     */
    List<DocumentDto> getDocumentsInCategory(String category);

    /**
     * 카테고리 내 문서 목록 조회 (하위 카테고리 포함)
     */
    List<DocumentDto> getDocumentsInCategoryRecursive(String category);

    /**
     * 모든 문서 조회
     */
    List<DocumentDto> getAllDocuments();

    /**
     * 카테고리별 문서 목록 조회 (하위 카테고리 포함)
     */
    Map<String, List<DocumentDto>> getDocumentsByCategory();

    /**
     * 카테고리별 문서 목록 조회 (직접 포함된 문서만)
     */
    Map<String, List<DocumentDto>> getDocumentsByCategoryDirect();
}

package com.til.csweb.repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * 문서 저장소 인터페이스
 */
public interface DocumentRepository {

    /**
     * 모든 카테고리 이름 조회
     */
    List<String> findAllCategoryNames();

    /**
     * 하위 카테고리 이름 조회
     */
    List<String> findSubcategoryNames(String category);

    /**
     * 카테고리 존재 여부 확인
     */
    boolean existsCategory(String category);

    /**
     * 카테고리 내 문서 경로 목록 조회 (직접 포함된 문서만)
     */
    List<Path> findDocumentPaths(String category);

    /**
     * 카테고리 내 문서 경로 목록 조회 (하위 카테고리 포함, 재귀적)
     */
    List<Path> findDocumentPathsRecursive(String category);

    /**
     * 문서 내용 읽기
     */
    Optional<String> readDocument(String category, String filename);

    /**
     * 문서 경로 내용 읽기
     */
    Optional<String> readDocument(Path path);

    /**
     * 문서 경로의 상대 카테고리 추출
     */
    String extractCategory(String baseCategory, Path path);

    /**
     * 문서 기본 경로 조회
     */
    Path getDocsPath();
}

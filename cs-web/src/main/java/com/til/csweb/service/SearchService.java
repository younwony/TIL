package com.til.csweb.service;

import com.til.csweb.domain.KeywordInfo;
import com.til.csweb.dto.DocumentDto;

import java.util.List;

/**
 * 검색 서비스 인터페이스
 */
public interface SearchService {

    /**
     * 전체 텍스트 검색 (제목, 설명, 키워드)
     */
    List<DocumentDto> search(String query);

    /**
     * 키워드로 문서 검색
     */
    List<DocumentDto> searchByKeyword(String keyword);

    /**
     * 모든 키워드 목록 조회 (사용 빈도순 정렬)
     */
    List<KeywordInfo> getAllKeywords();
}

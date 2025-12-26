package com.til.csweb.service;

import com.til.csweb.domain.KeywordInfo;
import com.til.csweb.dto.DocumentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 검색 서비스 구현체
 */
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final DocumentService documentService;

    @Override
    public List<DocumentDto> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String searchQuery = query.trim().toLowerCase();

        return documentService.getAllDocuments().stream()
                .filter(doc -> matchesSearch(doc, searchQuery))
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentDto> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String searchKeyword = keyword.trim().toLowerCase();

        return documentService.getAllDocuments().stream()
                .filter(doc -> doc.getKeywords().stream()
                        .anyMatch(k -> k.toLowerCase().contains(searchKeyword)))
                .collect(Collectors.toList());
    }

    @Override
    public List<KeywordInfo> getAllKeywords() {
        Map<String, Long> keywordCounts = documentService.getAllDocuments().stream()
                .flatMap(doc -> doc.getKeywords().stream())
                .collect(Collectors.groupingBy(k -> k, Collectors.counting()));

        return keywordCounts.entrySet().stream()
                .map(e -> new KeywordInfo(e.getKey(), e.getValue().intValue()))
                .sorted((a, b) -> Integer.compare(b.getCount(), a.getCount()))
                .collect(Collectors.toList());
    }

    private boolean matchesSearch(DocumentDto doc, String query) {
        if (doc.getTitle() != null && doc.getTitle().toLowerCase().contains(query)) {
            return true;
        }
        if (doc.getDescription() != null && doc.getDescription().toLowerCase().contains(query)) {
            return true;
        }
        return doc.getKeywords().stream()
                .anyMatch(k -> k.toLowerCase().contains(query));
    }
}

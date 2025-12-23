package com.til.csweb.controller;

import com.til.csweb.dto.DocumentDto;
import com.til.csweb.service.MarkdownService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * CS 문서 관련 컨트롤러
 */
@Controller
public class DocumentController {

    private final MarkdownService markdownService;

    public DocumentController(MarkdownService markdownService) {
        this.markdownService = markdownService;
    }

    /**
     * 메인 페이지 - 카테고리별 문서 목록
     */
    @GetMapping("/")
    public String index(Model model) {
        Map<String, List<DocumentDto>> documentsByCategory = markdownService.getDocumentsByCategory();
        int totalDocuments = documentsByCategory.values().stream()
                .mapToInt(List::size)
                .sum();

        model.addAttribute("documentsByCategory", documentsByCategory);
        model.addAttribute("totalDocuments", totalDocuments);
        return "index";
    }

    /**
     * 카테고리별 문서 목록 페이지
     */
    @GetMapping("/category/{category}")
    public String category(@PathVariable String category, Model model) {
        List<DocumentDto> documents = markdownService.getDocumentsInCategory(category);
        model.addAttribute("category", category);
        model.addAttribute("documents", documents);
        return "category";
    }

    /**
     * 문서 상세 페이지
     */
    @GetMapping("/docs/{category}/{filename}")
    public String document(
            @PathVariable String category,
            @PathVariable String filename,
            Model model) {

        DocumentDto document = markdownService.getDocument(category, filename);

        if (document == null) {
            return "error/404";
        }

        model.addAttribute("document", document);
        model.addAttribute("categories", markdownService.getCategories());
        return "document";
    }

    /**
     * 검색 페이지
     */
    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String keyword,
            Model model) {

        List<DocumentDto> results;
        String searchType;
        String searchValue;

        if (keyword != null && !keyword.trim().isEmpty()) {
            results = markdownService.searchByKeyword(keyword);
            searchType = "keyword";
            searchValue = keyword;
        } else if (q != null && !q.trim().isEmpty()) {
            results = markdownService.search(q);
            searchType = "query";
            searchValue = q;
        } else {
            results = List.of();
            searchType = null;
            searchValue = null;
        }

        model.addAttribute("results", results);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("resultCount", results.size());
        model.addAttribute("popularKeywords", markdownService.getAllKeywords().stream().limit(20).toList());

        return "search";
    }

    /**
     * 키워드 목록 페이지
     */
    @GetMapping("/keywords")
    public String keywords(Model model) {
        model.addAttribute("keywords", markdownService.getAllKeywords());
        model.addAttribute("totalDocuments", markdownService.getAllDocuments().size());
        return "keywords";
    }
}

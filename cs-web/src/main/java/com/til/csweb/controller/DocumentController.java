package com.til.csweb.controller;

import com.til.csweb.dto.CategoryDto;
import com.til.csweb.dto.DocumentDto;
import com.til.csweb.service.CategoryService;
import com.til.csweb.service.DocumentService;
import com.til.csweb.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.til.csweb.constant.DocumentConstants.SEARCH_RESULT_LIMIT;

/**
 * CS 문서 관련 컨트롤러
 */
@Controller
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final CategoryService categoryService;
    private final SearchService searchService;

    /**
     * 메인 페이지 - 카테고리별 문서 목록
     */
    @GetMapping("/")
    public String index(Model model) {
        List<CategoryDto> categories = categoryService.getAllCategoryInfos();
        Map<String, List<DocumentDto>> documentsByCategory = documentService.getDocumentsByCategory();
        int totalDocuments = documentsByCategory.values().stream()
                .mapToInt(List::size)
                .sum();

        model.addAttribute("categories", categories);
        model.addAttribute("documentsByCategory", documentsByCategory);
        model.addAttribute("totalDocuments", totalDocuments);
        return "index";
    }

    /**
     * 카테고리별 문서 목록 페이지 (최상위 카테고리)
     */
    @GetMapping("/category/{category}")
    public String category(@PathVariable String category, Model model) {
        Optional<CategoryDto> categoryInfo = categoryService.getCategoryInfo(category);
        if (categoryInfo.isEmpty()) {
            return "error/404";
        }

        List<DocumentDto> allDocuments = documentService.getDocumentsInCategoryRecursive(category);
        model.addAttribute("category", category);
        model.addAttribute("categoryInfo", categoryInfo.get());
        model.addAttribute("documents", allDocuments);
        model.addAttribute("subcategories", categoryInfo.get().getSubcategories());
        return "category";
    }

    /**
     * 하위 카테고리별 문서 목록 페이지
     */
    @GetMapping("/category/{category}/{subcategory}")
    public String subcategory(
            @PathVariable String category,
            @PathVariable String subcategory,
            Model model) {
        String fullPath = category + "/" + subcategory;
        Optional<CategoryDto> categoryInfo = categoryService.getCategoryInfo(fullPath);
        if (categoryInfo.isEmpty()) {
            return "error/404";
        }

        List<DocumentDto> documents = documentService.getDocumentsInCategoryRecursive(fullPath);
        model.addAttribute("category", fullPath);
        model.addAttribute("parentCategory", category);
        model.addAttribute("categoryInfo", categoryInfo.get());
        model.addAttribute("documents", documents);
        model.addAttribute("subcategories", categoryInfo.get().getSubcategories());
        return "category";
    }

    /**
     * 문서 상세 페이지 (기본 카테고리)
     */
    @GetMapping("/docs/{category}/{filename}")
    public String document(
            @PathVariable String category,
            @PathVariable String filename,
            Model model) {
        return handleDocumentRequest(category, null, filename, model);
    }

    /**
     * 문서 상세 페이지 (하위 카테고리 지원)
     */
    @GetMapping("/docs/{category}/{subcategory}/{filename}")
    public String documentWithSubcategory(
            @PathVariable String category,
            @PathVariable String subcategory,
            @PathVariable String filename,
            Model model) {
        return handleDocumentRequest(category, subcategory, filename, model);
    }

    /**
     * 문서 요청 처리 공통 로직
     */
    private String handleDocumentRequest(String category, String subcategory, String filename, Model model) {
        String fullCategory = subcategory != null ? category + "/" + subcategory : category;
        Optional<DocumentDto> document = documentService.getDocument(fullCategory, filename);

        if (document.isEmpty()) {
            if (categoryService.categoryExists(category)) {
                model.addAttribute("category", fullCategory);
                model.addAttribute("documentTitle", formatTitle(filename));
                return "coming-soon";
            }
            return "error/404";
        }

        model.addAttribute("document", document.get());
        model.addAttribute("categories", categoryService.getCategories());
        return "document";
    }

    /**
     * 파일명을 제목 형식으로 변환
     */
    private String formatTitle(String filename) {
        return filename.replace("-", " ")
                .replace("_", " ")
                .substring(0, 1).toUpperCase() + filename.substring(1).replace("-", " ");
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
            results = searchService.searchByKeyword(keyword);
            searchType = "keyword";
            searchValue = keyword;
        } else if (q != null && !q.trim().isEmpty()) {
            results = searchService.search(q);
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
        model.addAttribute("popularKeywords", searchService.getAllKeywords().stream()
                .limit(SEARCH_RESULT_LIMIT)
                .toList());

        return "search";
    }

    /**
     * 키워드 목록 페이지
     */
    @GetMapping("/keywords")
    public String keywords(Model model) {
        model.addAttribute("keywords", searchService.getAllKeywords());
        model.addAttribute("totalDocuments", documentService.getAllDocuments().size());
        return "keywords";
    }
}

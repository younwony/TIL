package com.til.csweb.controller;

import com.til.csweb.dto.DocumentDto;
import com.til.csweb.service.MarkdownService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}

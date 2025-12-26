package com.til.csweb.service;

import com.til.csweb.dto.CategoryDto;
import com.til.csweb.dto.DocumentDto;
import com.til.csweb.repository.DocumentRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 카테고리 관리 서비스 구현체
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final DocumentRepository documentRepository;
    private final DocumentService documentService;

    public CategoryServiceImpl(DocumentRepository documentRepository,
                               @Lazy DocumentService documentService) {
        this.documentRepository = documentRepository;
        this.documentService = documentService;
    }

    @Override
    public List<String> getCategories() {
        return documentRepository.findAllCategoryNames();
    }

    @Override
    public List<String> getSubcategories(String category) {
        return documentRepository.findSubcategoryNames(category);
    }

    @Override
    public boolean categoryExists(String category) {
        return documentRepository.existsCategory(category);
    }

    @Override
    public Optional<CategoryDto> getCategoryInfo(String categoryPath) {
        return getCategoryInfo(categoryPath, null);
    }

    private Optional<CategoryDto> getCategoryInfo(String categoryPath, String parentCategory) {
        if (!documentRepository.existsCategory(categoryPath)) {
            return Optional.empty();
        }

        String name = extractCategoryName(categoryPath);
        List<String> subcategoryNames = getSubcategories(categoryPath);
        List<DocumentDto> documents = documentService.getDocumentsInCategory(categoryPath);

        List<CategoryDto> subcategories = subcategoryNames.stream()
                .map(subName -> getCategoryInfo(categoryPath + "/" + subName, categoryPath))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        int totalDocs = documents.size();
        for (CategoryDto sub : subcategories) {
            totalDocs += sub.getTotalDocumentCount();
        }

        return Optional.of(CategoryDto.builder()
                .name(name)
                .path(categoryPath)
                .parentCategory(parentCategory)
                .subcategories(subcategories)
                .documents(documents)
                .totalDocumentCount(totalDocs)
                .build());
    }

    @Override
    public List<CategoryDto> getAllCategoryInfos() {
        return getCategories().stream()
                .map(this::getCategoryInfo)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private String extractCategoryName(String categoryPath) {
        int lastSlash = categoryPath.lastIndexOf('/');
        return lastSlash >= 0 ? categoryPath.substring(lastSlash + 1) : categoryPath;
    }
}

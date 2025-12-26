package com.til.csweb.service;

import com.til.csweb.domain.LevelInfo;
import com.til.csweb.domain.PrerequisiteInfo;
import com.til.csweb.dto.DocumentDto;
import com.til.csweb.repository.DocumentRepository;
import com.til.csweb.util.MarkdownMetadataExtractor;
import com.til.csweb.util.MarkdownParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.til.csweb.constant.DocumentConstants.MARKDOWN_EXTENSION;

/**
 * 문서 조회 서비스 구현체
 */
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final MarkdownParser markdownParser;
    private final MarkdownMetadataExtractor metadataExtractor;
    private final CategoryService categoryService;

    @Override
    public Optional<DocumentDto> getDocument(String category, String filename) {
        return documentRepository.readDocument(category, filename)
                .map(markdown -> buildDocument(category, filename, markdown, true));
    }

    @Override
    public List<DocumentDto> getDocumentsInCategory(String category) {
        return documentRepository.findDocumentPaths(category).stream()
                .map(path -> createDocumentSummary(category, path))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(this::compareByLevelThenTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentDto> getDocumentsInCategoryRecursive(String category) {
        return documentRepository.findDocumentPathsRecursive(category).stream()
                .map(path -> {
                    String subCategory = documentRepository.extractCategory(category, path);
                    return createDocumentSummary(subCategory, path);
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(this::compareByLevelThenTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentDto> getAllDocuments() {
        return categoryService.getCategories().stream()
                .flatMap(category -> getDocumentsInCategory(category).stream())
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<DocumentDto>> getDocumentsByCategory() {
        return categoryService.getCategories().stream()
                .collect(Collectors.toMap(
                        category -> category,
                        this::getDocumentsInCategoryRecursive,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Map<String, List<DocumentDto>> getDocumentsByCategoryDirect() {
        return categoryService.getCategories().stream()
                .collect(Collectors.toMap(
                        category -> category,
                        this::getDocumentsInCategory,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    private Optional<DocumentDto> createDocumentSummary(String category, Path path) {
        String filename = extractFilename(path);
        return documentRepository.readDocument(path)
                .map(markdown -> buildDocument(category, filename, markdown, false));
    }

    private DocumentDto buildDocument(String category, String filename, String markdown, boolean includeHtml) {
        String title = metadataExtractor.extractTitle(markdown);
        String description = metadataExtractor.extractDescription(markdown);
        LevelInfo levelInfo = metadataExtractor.extractLevel(markdown);
        List<PrerequisiteInfo> prereqs = metadataExtractor.extractPrerequisites(markdown, category);
        List<String> keywords = metadataExtractor.extractKeywords(markdown);

        var builder = DocumentDto.builder()
                .category(category)
                .filename(filename)
                .title(title)
                .description(description)
                .level(levelInfo.getLevel())
                .levelName(levelInfo.getName())
                .prerequisites(convertPrerequisites(prereqs))
                .keywords(keywords);

        if (includeHtml) {
            builder.htmlContent(markdownParser.toHtml(markdown));
        }

        return builder.build();
    }

    private List<DocumentDto.Prerequisite> convertPrerequisites(List<PrerequisiteInfo> prereqs) {
        return prereqs.stream()
                .map(p -> new DocumentDto.Prerequisite(p.getTitle(), p.getPath()))
                .collect(Collectors.toList());
    }

    private String extractFilename(Path path) {
        String filename = path.getFileName().toString();
        return filename.substring(0, filename.length() - MARKDOWN_EXTENSION.length());
    }

    private int compareByLevelThenTitle(DocumentDto a, DocumentDto b) {
        Integer levelA = a.getLevel();
        Integer levelB = b.getLevel();

        if (levelA == null && levelB == null) {
            return a.getTitle().compareToIgnoreCase(b.getTitle());
        }
        if (levelA == null) return 1;
        if (levelB == null) return -1;

        int levelCompare = levelA.compareTo(levelB);
        if (levelCompare != 0) {
            return levelCompare;
        }

        return a.getTitle().compareToIgnoreCase(b.getTitle());
    }
}

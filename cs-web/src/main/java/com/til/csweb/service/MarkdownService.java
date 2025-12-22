package com.til.csweb.service;

import com.til.csweb.dto.DocumentDto;
import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * 마크다운 문서 파싱 및 변환 서비스
 */
@Service
public class MarkdownService {

    private static final String MARKDOWN_EXTENSION = ".md";
    private static final String README_FILENAME = "README.md";
    private static final Pattern LEVEL_PATTERN = Pattern.compile("\\[([1-5])\\]\\s*(.+)");
    private static final Pattern PREREQUISITE_LINK_PATTERN = Pattern.compile("\\[([^\\]]+)\\]\\(([^)]+)\\)");

    private final Parser parser;
    private final HtmlRenderer renderer;
    private final Path docsPath;

    public MarkdownService(@Value("${cs.docs.path}") String docsPathString) {
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(
                TablesExtension.create(),
                StrikethroughExtension.create(),
                TocExtension.create(),
                AnchorLinkExtension.create()
        ));
        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
        options.set(AnchorLinkExtension.ANCHORLINKS_SET_ID, true);
        options.set(AnchorLinkExtension.ANCHORLINKS_WRAP_TEXT, false);
        options.set(AnchorLinkExtension.ANCHORLINKS_TEXT_PREFIX, "");
        options.set(AnchorLinkExtension.ANCHORLINKS_TEXT_SUFFIX, "");
        options.set(HtmlRenderer.GENERATE_HEADER_ID, true);

        this.parser = Parser.builder(options).build();
        this.renderer = HtmlRenderer.builder(options).build();
        this.docsPath = Paths.get(docsPathString).toAbsolutePath().normalize();
    }

    /**
     * 모든 카테고리 목록 조회
     */
    public List<String> getCategories() {
        try (Stream<Path> paths = Files.list(docsPath)) {
            return paths
                    .filter(Files::isDirectory)
                    .map(path -> path.getFileName().toString())
                    .filter(name -> !name.startsWith("."))
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    /**
     * 카테고리별 문서 목록 조회
     */
    public Map<String, List<DocumentDto>> getDocumentsByCategory() {
        return getCategories().stream()
                .collect(Collectors.toMap(
                        category -> category,
                        this::getDocumentsInCategory,
                        (a, b) -> a,
                        java.util.LinkedHashMap::new
                ));
    }

    /**
     * 특정 카테고리의 문서 목록 조회
     */
    public List<DocumentDto> getDocumentsInCategory(String category) {
        Path categoryPath = docsPath.resolve(category);
        if (!Files.isDirectory(categoryPath)) {
            return Collections.emptyList();
        }

        try (Stream<Path> paths = Files.list(categoryPath)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(MARKDOWN_EXTENSION))
                    .filter(path -> !path.getFileName().toString().equals(README_FILENAME))
                    .map(path -> createDocumentSummary(category, path))
                    .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    /**
     * 특정 문서 상세 조회
     */
    public DocumentDto getDocument(String category, String filename) {
        Path filePath = docsPath.resolve(category).resolve(filename + MARKDOWN_EXTENSION);

        if (!Files.exists(filePath)) {
            return null;
        }

        try {
            String markdown = Files.readString(filePath, StandardCharsets.UTF_8);
            String htmlContent = convertToHtml(markdown);
            String title = extractTitle(markdown);
            String description = extractDescription(markdown);
            LevelInfo levelInfo = extractLevel(markdown);
            List<PrerequisiteInfo> prereqs = extractPrerequisites(markdown, category);

            return DocumentDto.builder()
                    .category(category)
                    .filename(filename)
                    .title(title)
                    .description(description)
                    .htmlContent(htmlContent)
                    .level(levelInfo.level)
                    .levelName(levelInfo.name)
                    .prerequisites(convertPrerequisites(prereqs))
                    .build();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 마크다운을 HTML로 변환
     */
    public String convertToHtml(String markdown) {
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }

    /**
     * 마크다운에서 제목(H1) 추출
     */
    String extractTitle(String markdown) {
        String[] lines = markdown.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("# ")) {
                return trimmed.substring(2).trim();
            }
        }
        return "Untitled";
    }

    /**
     * 마크다운에서 난이도 레벨 추출
     * 형식: > `[N] 레벨명` · 선수 지식: ... 또는 **난이도: [N] 레벨명**
     */
    LevelInfo extractLevel(String markdown) {
        String[] lines = markdown.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();

            // **난이도: [N] 이름** 형식 찾기
            if (trimmed.contains("난이도") || trimmed.contains("레벨") || trimmed.contains("Difficulty")) {
                // ** 제거
                String cleaned = trimmed.replaceAll("\\*\\*", "").replaceAll("\\*", "");
                // : 이후 부분 추출
                int colonIndex = cleaned.indexOf(":");
                if (colonIndex != -1) {
                    String levelPart = cleaned.substring(colonIndex + 1).trim();
                    Matcher matcher = LEVEL_PATTERN.matcher(levelPart);
                    if (matcher.find()) {
                        int level = Integer.parseInt(matcher.group(1));
                        String name = cleanLevelName(matcher.group(2).trim());
                        return new LevelInfo(level, name);
                    }
                }
            }

            // > `[N] 이름` 형식 (blockquote 내부)
            if (trimmed.startsWith(">")) {
                String content = trimmed.substring(1).trim();
                // 백틱 제거
                content = content.replaceAll("`", "");
                Matcher matcher = LEVEL_PATTERN.matcher(content);
                if (matcher.find()) {
                    int level = Integer.parseInt(matcher.group(1));
                    String name = cleanLevelName(matcher.group(2).trim());
                    return new LevelInfo(level, name);
                }
            }
        }

        return new LevelInfo(null, null);
    }

    /**
     * 레벨 이름에서 불필요한 부분 제거
     * "심화 · 선수 지식: ..." -> "심화"
     */
    private String cleanLevelName(String name) {
        // · 구분자가 있으면 앞부분만 추출
        if (name.contains("·")) {
            name = name.substring(0, name.indexOf("·")).trim();
        }
        // 선수 지식 키워드가 있으면 앞부분만 추출
        if (name.contains("선수 지식")) {
            name = name.substring(0, name.indexOf("선수 지식")).trim();
        }
        if (name.contains("선행 지식")) {
            name = name.substring(0, name.indexOf("선행 지식")).trim();
        }
        return name;
    }

    /**
     * 마크다운에서 설명 추출 (첫 번째 인용구 또는 첫 번째 문단)
     */
    String extractDescription(String markdown) {
        String[] lines = markdown.split("\n");
        StringBuilder description = new StringBuilder();
        boolean foundContent = false;
        boolean passedLevelLine = false;

        for (String line : lines) {
            String trimmed = line.trim();

            // 제목 건너뛰기
            if (trimmed.startsWith("#")) {
                continue;
            }

            // 난이도 줄 건너뛰기
            if (trimmed.contains("난이도") || trimmed.contains("레벨") || trimmed.contains("Difficulty")) {
                passedLevelLine = true;
                continue;
            }

            // 빈 줄 건너뛰기 (아직 내용을 찾지 못한 경우)
            if (trimmed.isEmpty() && !foundContent) {
                continue;
            }

            // 인용구 추출 (> 로 시작)
            if (trimmed.startsWith(">")) {
                String quote = trimmed.substring(1).trim();
                // 백틱 제거
                quote = quote.replaceAll("`", "");
                // 레벨 패턴이 아닌 경우만 설명으로 사용
                if (!quote.isEmpty() && !LEVEL_PATTERN.matcher(quote).matches()) {
                    return truncate(quote);
                }
                continue;
            }

            // 일반 텍스트 추출
            if (!trimmed.isEmpty() && !trimmed.startsWith("-") && !trimmed.startsWith("|")) {
                foundContent = true;
                description.append(trimmed).append(" ");
                if (description.length() > 150) {
                    break;
                }
            }

            if (foundContent && trimmed.isEmpty()) {
                break;
            }
        }

        return truncate(description.toString().trim());
    }

    private DocumentDto createDocumentSummary(String category, Path path) {
        String filename = path.getFileName().toString();
        filename = filename.substring(0, filename.length() - MARKDOWN_EXTENSION.length());

        try {
            String markdown = Files.readString(path, StandardCharsets.UTF_8);
            String title = extractTitle(markdown);
            String description = extractDescription(markdown);
            LevelInfo levelInfo = extractLevel(markdown);
            List<PrerequisiteInfo> prereqs = extractPrerequisites(markdown, category);

            return DocumentDto.builder()
                    .category(category)
                    .filename(filename)
                    .title(title)
                    .description(description)
                    .level(levelInfo.level)
                    .levelName(levelInfo.name)
                    .prerequisites(convertPrerequisites(prereqs))
                    .build();
        } catch (IOException e) {
            return DocumentDto.builder()
                    .category(category)
                    .filename(filename)
                    .title(filename)
                    .description("")
                    .build();
        }
    }

    /**
     * PrerequisiteInfo 리스트를 DocumentDto.Prerequisite 리스트로 변환
     */
    private List<DocumentDto.Prerequisite> convertPrerequisites(List<PrerequisiteInfo> prereqs) {
        return prereqs.stream()
                .map(p -> new DocumentDto.Prerequisite(p.title, p.path))
                .collect(Collectors.toList());
    }

    private String truncate(String text) {
        int maxLength = 150;
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength).trim() + "...";
    }

    /**
     * 마크다운에서 선수 지식 추출
     * 형식: > `[N] 레벨` · 선수 지식: [문서명](링크), [문서명2](링크2)
     */
    List<PrerequisiteInfo> extractPrerequisites(String markdown, String currentCategory) {
        List<PrerequisiteInfo> prerequisites = new ArrayList<>();
        String[] lines = markdown.split("\n");

        for (String line : lines) {
            String trimmed = line.trim();

            // 선수 지식 키워드가 포함된 줄에서만 링크 추출
            if (trimmed.contains("선수 지식") || trimmed.contains("선행 지식")) {
                // "선수 지식: 없음" 패턴 무시
                if (trimmed.contains("없음") || trimmed.contains("없습니다")) {
                    continue;
                }

                Matcher matcher = PREREQUISITE_LINK_PATTERN.matcher(trimmed);
                while (matcher.find()) {
                    String title = matcher.group(1).trim();
                    String link = matcher.group(2).trim();

                    // 내부 링크인 경우 (상대 경로)
                    if (!link.startsWith("http")) {
                        String path = convertToWebPath(link, currentCategory);
                        if (path != null) {
                            prerequisites.add(new PrerequisiteInfo(title, path));
                        }
                    }
                }

                // 첫 번째 선수 지식 줄에서만 추출 (보통 문서 상단에 한 번만 있음)
                if (!prerequisites.isEmpty()) {
                    break;
                }
            }

            // 최대 5개까지만 추출
            if (prerequisites.size() >= 5) {
                break;
            }
        }

        return prerequisites;
    }

    /**
     * 마크다운 링크를 웹 경로로 변환
     * ./filename.md -> /docs/{currentCategory}/filename
     * ../category/filename.md -> /docs/category/filename
     */
    private String convertToWebPath(String mdLink, String currentCategory) {
        String normalized = mdLink.replace("\\", "/");

        if (!normalized.endsWith(MARKDOWN_EXTENSION)) {
            return null;
        }

        // ./filename.md 형식 (같은 카테고리 내 파일)
        if (normalized.startsWith("./") && !normalized.substring(2).contains("/")) {
            String filename = normalized.substring(2, normalized.length() - MARKDOWN_EXTENSION.length());
            return "/docs/" + currentCategory + "/" + filename;
        }

        // ../category/filename.md 형식 (다른 카테고리 파일)
        if (normalized.startsWith("../")) {
            String[] parts = normalized.split("/");
            if (parts.length >= 3) {
                String category = parts[parts.length - 2];
                String filename = parts[parts.length - 1];
                filename = filename.substring(0, filename.length() - MARKDOWN_EXTENSION.length());
                return "/docs/" + category + "/" + filename;
            }
        }

        // category/filename.md 형식 (직접 경로)
        String[] parts = normalized.split("/");
        if (parts.length >= 2) {
            String category = parts[parts.length - 2];
            String filename = parts[parts.length - 1];
            filename = filename.substring(0, filename.length() - MARKDOWN_EXTENSION.length());
            if (!category.startsWith(".")) {
                return "/docs/" + category + "/" + filename;
            }
        }

        return null;
    }

    /**
     * 레벨 정보를 담는 내부 클래스
     */
    static class LevelInfo {
        final Integer level;
        final String name;

        LevelInfo(Integer level, String name) {
            this.level = level;
            this.name = name;
        }
    }

    /**
     * 선수 지식 정보를 담는 내부 클래스
     */
    static class PrerequisiteInfo {
        final String title;
        final String path;

        PrerequisiteInfo(String title, String path) {
            this.title = title;
            this.path = path;
        }
    }
}

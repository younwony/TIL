package com.til.csweb.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.til.csweb.constant.DocumentConstants.MARKDOWN_EXTENSION;

/**
 * 경로 해결 유틸리티
 */
public final class PathResolver {

    private PathResolver() {
    }

    /**
     * CS 문서 경로를 해결합니다.
     * 1. 환경변수/설정값이 있으면 해당 경로 사용
     * 2. 없으면 프로젝트 구조에서 자동 탐지
     */
    public static Path resolveCsDocsPath(String configuredPath) {
        if (configuredPath != null && !configuredPath.isEmpty()) {
            Path configPath = Paths.get(configuredPath).toAbsolutePath().normalize();
            if (Files.isDirectory(configPath)) {
                return configPath;
            }
        }

        Path userDir = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();

        List<Path> candidates = Arrays.asList(
                userDir.resolve("../cs"),
                userDir.resolve("cs"),
                userDir.resolve("../../cs"),
                userDir.getParent().resolve("cs")
        );

        for (Path candidate : candidates) {
            Path normalized = candidate.toAbsolutePath().normalize();
            if (Files.isDirectory(normalized) && Files.exists(normalized.resolve("README.md"))) {
                return normalized;
            }
        }

        Path defaultPath = userDir.resolve("../cs").toAbsolutePath().normalize();
        System.err.println("[WARN] CS docs path not found. Using default: " + defaultPath);
        return defaultPath;
    }

    /**
     * 마크다운 링크를 웹 경로로 변환
     * ./filename.md -> /docs/{currentCategory}/filename
     * ../category/filename.md -> /docs/category/filename
     */
    public static Optional<String> convertToWebPath(String mdLink, String currentCategory) {
        String normalized = mdLink.replace("\\", "/");

        if (!normalized.endsWith(MARKDOWN_EXTENSION)) {
            return Optional.empty();
        }

        // ./filename.md 형식 (같은 카테고리 내 파일)
        if (normalized.startsWith("./") && !normalized.substring(2).contains("/")) {
            String filename = normalized.substring(2, normalized.length() - MARKDOWN_EXTENSION.length());
            return Optional.of("/docs/" + currentCategory + "/" + filename);
        }

        // ../category/filename.md 형식 (다른 카테고리 파일)
        if (normalized.startsWith("../")) {
            String[] parts = normalized.split("/");
            if (parts.length >= 3) {
                String category = parts[parts.length - 2];
                String filename = parts[parts.length - 1];
                filename = filename.substring(0, filename.length() - MARKDOWN_EXTENSION.length());
                return Optional.of("/docs/" + category + "/" + filename);
            }
        }

        // category/filename.md 형식 (직접 경로)
        String[] parts = normalized.split("/");
        if (parts.length >= 2) {
            String category = parts[parts.length - 2];
            String filename = parts[parts.length - 1];
            filename = filename.substring(0, filename.length() - MARKDOWN_EXTENSION.length());
            if (!category.startsWith(".")) {
                return Optional.of("/docs/" + category + "/" + filename);
            }
        }

        return Optional.empty();
    }
}

package com.til.csweb.repository;

import com.til.csweb.util.PathResolver;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.til.csweb.constant.DocumentConstants.MARKDOWN_EXTENSION;
import static com.til.csweb.constant.DocumentConstants.README_FILENAME;

/**
 * 파일 시스템 기반 문서 저장소 구현체
 */
@Repository
public class FileSystemDocumentRepository implements DocumentRepository {

    @Getter
    private final Path docsPath;

    public FileSystemDocumentRepository(@Value("${cs.docs.path:}") String docsPathString) {
        this.docsPath = PathResolver.resolveCsDocsPath(docsPathString);
    }

    @Override
    public List<String> findAllCategoryNames() {
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

    @Override
    public List<String> findSubcategoryNames(String category) {
        Path categoryPath = docsPath.resolve(category);
        if (!Files.isDirectory(categoryPath)) {
            return Collections.emptyList();
        }

        try (Stream<Path> paths = Files.list(categoryPath)) {
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

    @Override
    public boolean existsCategory(String category) {
        Path categoryPath = docsPath.resolve(category);
        return Files.isDirectory(categoryPath);
    }

    @Override
    public List<Path> findDocumentPaths(String category) {
        Path categoryPath = docsPath.resolve(category);
        if (!Files.isDirectory(categoryPath)) {
            return Collections.emptyList();
        }

        try (Stream<Path> paths = Files.list(categoryPath)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(MARKDOWN_EXTENSION))
                    .filter(path -> !path.getFileName().toString().equals(README_FILENAME))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Path> findDocumentPathsRecursive(String category) {
        Path categoryPath = docsPath.resolve(category);
        if (!Files.isDirectory(categoryPath)) {
            return Collections.emptyList();
        }

        List<Path> documents = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(categoryPath)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(MARKDOWN_EXTENSION))
                    .filter(path -> !path.getFileName().toString().equals(README_FILENAME))
                    .forEach(documents::add);
        } catch (IOException e) {
            return Collections.emptyList();
        }

        return documents;
    }

    @Override
    public Optional<String> readDocument(String category, String filename) {
        Path filePath = docsPath.resolve(category).resolve(filename + MARKDOWN_EXTENSION);

        if (!Files.exists(filePath)) {
            return Optional.empty();
        }

        try {
            return Optional.of(Files.readString(filePath, StandardCharsets.UTF_8));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> readDocument(Path path) {
        if (!Files.exists(path)) {
            return Optional.empty();
        }

        try {
            return Optional.of(Files.readString(path, StandardCharsets.UTF_8));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public String extractCategory(String baseCategory, Path path) {
        Path categoryPath = docsPath.resolve(baseCategory);
        Path relativePath = categoryPath.relativize(path.getParent());
        return relativePath.toString().isEmpty()
                ? baseCategory
                : baseCategory + "/" + relativePath.toString().replace("\\", "/");
    }
}

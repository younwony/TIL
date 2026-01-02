package com.til.csweb.controller;

import com.til.csweb.util.PathResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * CS 문서 내 이미지 파일 서빙 컨트롤러
 */
@Controller
public class ImageController {

    private static final Map<String, MediaType> MEDIA_TYPES = Map.of(
            "svg", MediaType.valueOf("image/svg+xml"),
            "png", MediaType.IMAGE_PNG,
            "jpg", MediaType.IMAGE_JPEG,
            "jpeg", MediaType.IMAGE_JPEG,
            "gif", MediaType.IMAGE_GIF,
            "webp", MediaType.valueOf("image/webp")
    );

    private final Path csDocsPath;

    public ImageController(@Value("${cs.docs.path:}") String configuredPath) {
        this.csDocsPath = PathResolver.resolveCsDocsPath(configuredPath);
    }

    /**
     * /docs/{category}/images/{filename} 요청 처리
     * 예: /docs/system-design/images/circuit-breaker-state.svg
     */
    @GetMapping("/docs/{category}/images/{filename:.+}")
    public ResponseEntity<Resource> serveImage(
            @PathVariable String category,
            @PathVariable String filename) {

        Path imagePath = csDocsPath.resolve(category).resolve("images").resolve(filename);

        if (!Files.exists(imagePath) || !Files.isRegularFile(imagePath)) {
            return ResponseEntity.notFound().build();
        }

        // 보안: cs 디렉토리 외부 접근 방지
        if (!imagePath.normalize().startsWith(csDocsPath.normalize())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String extension = getExtension(filename);
        MediaType mediaType = MEDIA_TYPES.getOrDefault(extension, MediaType.APPLICATION_OCTET_STREAM);

        Resource resource = new FileSystemResource(imagePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400")
                .contentType(mediaType)
                .body(resource);
    }

    private String getExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot + 1).toLowerCase() : "";
    }
}

package com.til.csweb.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ImageController 테스트")
class ImageControllerTest {

    @TempDir
    Path tempDir;

    @Nested
    @DisplayName("serveImage 메서드")
    class ServeImageTest {

        @Test
        @DisplayName("존재하는 SVG 이미지를 반환한다")
        void returnsSvgImage() throws IOException {
            // given
            Path categoryDir = tempDir.resolve("system-design").resolve("images");
            Files.createDirectories(categoryDir);
            Path svgFile = categoryDir.resolve("test.svg");
            String svgContent = "<svg xmlns=\"http://www.w3.org/2000/svg\"></svg>";
            Files.writeString(svgFile, svgContent);

            ImageController controller = new ImageController(tempDir.toString());

            // when
            ResponseEntity<?> response = controller.serveImage("system-design", "test.svg");

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(MediaType.valueOf("image/svg+xml"), response.getHeaders().getContentType());
        }

        @Test
        @DisplayName("존재하는 PNG 이미지를 반환한다")
        void returnsPngImage() throws IOException {
            // given
            Path categoryDir = tempDir.resolve("network").resolve("images");
            Files.createDirectories(categoryDir);
            Path pngFile = categoryDir.resolve("diagram.png");
            Files.write(pngFile, new byte[]{(byte) 0x89, 'P', 'N', 'G'});

            ImageController controller = new ImageController(tempDir.toString());

            // when
            ResponseEntity<?> response = controller.serveImage("network", "diagram.png");

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(MediaType.IMAGE_PNG, response.getHeaders().getContentType());
        }

        @Test
        @DisplayName("존재하지 않는 이미지는 404를 반환한다")
        void returnsNotFoundForMissingImage() {
            // given
            ImageController controller = new ImageController(tempDir.toString());

            // when
            ResponseEntity<?> response = controller.serveImage("system-design", "nonexistent.svg");

            // then
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        @DisplayName("디렉토리 경로는 404를 반환한다")
        void returnsNotFoundForDirectory() throws IOException {
            // given
            Path categoryDir = tempDir.resolve("system-design").resolve("images").resolve("subdir");
            Files.createDirectories(categoryDir);

            ImageController controller = new ImageController(tempDir.toString());

            // when
            ResponseEntity<?> response = controller.serveImage("system-design", "subdir");

            // then
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        @DisplayName("캐시 헤더가 설정된다")
        void setsCacheHeader() throws IOException {
            // given
            Path categoryDir = tempDir.resolve("os").resolve("images");
            Files.createDirectories(categoryDir);
            Path svgFile = categoryDir.resolve("process.svg");
            Files.writeString(svgFile, "<svg></svg>");

            ImageController controller = new ImageController(tempDir.toString());

            // when
            ResponseEntity<?> response = controller.serveImage("os", "process.svg");

            // then
            assertTrue(response.getHeaders().getCacheControl().contains("max-age=86400"));
        }
    }
}

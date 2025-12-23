package com.til.csweb.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DocumentDto 테스트")
class DocumentDtoTest {

    @Nested
    @DisplayName("키워드 관련 메서드")
    class KeywordMethodsTest {

        @Test
        @DisplayName("hasKeywords - 키워드가 있으면 true 반환")
        void hasKeywordsWhenPresent() {
            // given
            DocumentDto doc = DocumentDto.builder()
                    .category("test")
                    .filename("test-file")
                    .title("Test")
                    .keywords(List.of("keyword1", "keyword2"))
                    .build();

            // then
            assertTrue(doc.hasKeywords());
        }

        @Test
        @DisplayName("hasKeywords - 키워드가 없으면 false 반환")
        void hasKeywordsWhenEmpty() {
            // given
            DocumentDto doc = DocumentDto.builder()
                    .category("test")
                    .filename("test-file")
                    .title("Test")
                    .build();

            // then
            assertFalse(doc.hasKeywords());
        }

        @Test
        @DisplayName("getDisplayKeywords - 5개 이하면 전체 반환")
        void getDisplayKeywordsWhenLessThanFive() {
            // given
            List<String> keywords = List.of("k1", "k2", "k3");
            DocumentDto doc = DocumentDto.builder()
                    .category("test")
                    .filename("test-file")
                    .title("Test")
                    .keywords(keywords)
                    .build();

            // when
            List<String> displayKeywords = doc.getDisplayKeywords();

            // then
            assertEquals(3, displayKeywords.size());
            assertEquals(keywords, displayKeywords);
        }

        @Test
        @DisplayName("getDisplayKeywords - 5개 초과면 5개만 반환")
        void getDisplayKeywordsWhenMoreThanFive() {
            // given
            List<String> keywords = List.of("k1", "k2", "k3", "k4", "k5", "k6", "k7");
            DocumentDto doc = DocumentDto.builder()
                    .category("test")
                    .filename("test-file")
                    .title("Test")
                    .keywords(keywords)
                    .build();

            // when
            List<String> displayKeywords = doc.getDisplayKeywords();

            // then
            assertEquals(5, displayKeywords.size());
            assertEquals(List.of("k1", "k2", "k3", "k4", "k5"), displayKeywords);
        }

        @Test
        @DisplayName("getMoreKeywordsCount - 5개 이하면 0 반환")
        void getMoreKeywordsCountWhenLessThanFive() {
            // given
            DocumentDto doc = DocumentDto.builder()
                    .category("test")
                    .filename("test-file")
                    .title("Test")
                    .keywords(List.of("k1", "k2", "k3"))
                    .build();

            // when
            int count = doc.getMoreKeywordsCount();

            // then
            assertEquals(0, count);
        }

        @Test
        @DisplayName("getMoreKeywordsCount - 5개 초과면 초과 개수 반환")
        void getMoreKeywordsCountWhenMoreThanFive() {
            // given
            List<String> keywords = List.of("k1", "k2", "k3", "k4", "k5", "k6", "k7", "k8");
            DocumentDto doc = DocumentDto.builder()
                    .category("test")
                    .filename("test-file")
                    .title("Test")
                    .keywords(keywords)
                    .build();

            // when
            int count = doc.getMoreKeywordsCount();

            // then
            assertEquals(3, count); // 8 - 5 = 3
        }

        @Test
        @DisplayName("키워드 null이면 빈 리스트로 처리")
        void nullKeywordsHandledAsEmptyList() {
            // given
            DocumentDto doc = DocumentDto.builder()
                    .category("test")
                    .filename("test-file")
                    .title("Test")
                    .keywords(null)
                    .build();

            // then
            assertFalse(doc.hasKeywords());
            assertTrue(doc.getDisplayKeywords().isEmpty());
            assertEquals(0, doc.getMoreKeywordsCount());
        }
    }

    @Nested
    @DisplayName("기본 메서드")
    class BasicMethodsTest {

        @Test
        @DisplayName("getPath - 올바른 경로 반환")
        void getPathReturnsCorrectPath() {
            // given
            DocumentDto doc = DocumentDto.builder()
                    .category("system-design")
                    .filename("docker")
                    .title("Docker")
                    .build();

            // when
            String path = doc.getPath();

            // then
            assertEquals("/docs/system-design/docker", path);
        }

        @Test
        @DisplayName("hasLevel - 레벨 정보가 있으면 true 반환")
        void hasLevelWhenPresent() {
            // given
            DocumentDto doc = DocumentDto.builder()
                    .category("test")
                    .filename("test-file")
                    .title("Test")
                    .level(3)
                    .levelName("중급")
                    .build();

            // then
            assertTrue(doc.hasLevel());
        }

        @Test
        @DisplayName("hasLevel - 레벨 정보가 없으면 false 반환")
        void hasLevelWhenMissing() {
            // given
            DocumentDto doc = DocumentDto.builder()
                    .category("test")
                    .filename("test-file")
                    .title("Test")
                    .build();

            // then
            assertFalse(doc.hasLevel());
        }

        @Test
        @DisplayName("hasPrerequisites - 선수 지식이 있으면 true 반환")
        void hasPrerequisitesWhenPresent() {
            // given
            DocumentDto doc = DocumentDto.builder()
                    .category("test")
                    .filename("test-file")
                    .title("Test")
                    .prerequisites(List.of(new DocumentDto.Prerequisite("Docker", "/docs/system-design/docker")))
                    .build();

            // then
            assertTrue(doc.hasPrerequisites());
        }
    }
}

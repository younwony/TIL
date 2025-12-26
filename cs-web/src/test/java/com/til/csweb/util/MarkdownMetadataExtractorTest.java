package com.til.csweb.util;

import com.til.csweb.domain.LevelInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MarkdownMetadataExtractor 테스트")
class MarkdownMetadataExtractorTest {

    private MarkdownMetadataExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new MarkdownMetadataExtractor();
    }

    @Nested
    @DisplayName("extractTitle 메서드")
    class ExtractTitleTest {

        @Test
        @DisplayName("H1 제목을 추출한다")
        void extractH1Title() {
            // given
            String markdown = "# AI Agent란\n\n> 정의...";

            // when
            String title = extractor.extractTitle(markdown);

            // then
            assertEquals("AI Agent란", title);
        }

        @Test
        @DisplayName("제목이 없으면 Untitled를 반환한다")
        void returnUntitledWhenNoTitle() {
            // given
            String markdown = "Just some text without title";

            // when
            String title = extractor.extractTitle(markdown);

            // then
            assertEquals("Untitled", title);
        }

        @Test
        @DisplayName("H2는 제목으로 추출하지 않는다")
        void ignoreH2Title() {
            // given
            String markdown = "## This is H2\n\nSome content";

            // when
            String title = extractor.extractTitle(markdown);

            // then
            assertEquals("Untitled", title);
        }
    }

    @Nested
    @DisplayName("extractLevel 메서드")
    class ExtractLevelTest {

        @Test
        @DisplayName("인용구에서 레벨 정보를 추출한다")
        void extractLevelFromBlockquote() {
            // given
            String markdown = "# Title\n\n> `[3] 중급` · 선수 지식: Docker";

            // when
            LevelInfo levelInfo = extractor.extractLevel(markdown);

            // then
            assertEquals(3, levelInfo.getLevel());
            assertEquals("중급", levelInfo.getName());
        }

        @Test
        @DisplayName("레벨 정보가 없으면 빈 LevelInfo를 반환한다")
        void returnEmptyLevelInfoWhenNoLevel() {
            // given
            String markdown = "# Title\n\n본문만 있는 마크다운";

            // when
            LevelInfo levelInfo = extractor.extractLevel(markdown);

            // then
            assertFalse(levelInfo.hasLevel());
            assertNull(levelInfo.getLevel());
            assertNull(levelInfo.getName());
        }
    }

    @Nested
    @DisplayName("extractDescription 메서드")
    class ExtractDescriptionTest {

        @Test
        @DisplayName("인용구에서 설명을 추출한다")
        void extractDescriptionFromBlockquote() {
            // given
            String markdown = "# Title\n\n> 이것은 설명입니다.\n\n본문 내용";

            // when
            String description = extractor.extractDescription(markdown);

            // then
            assertEquals("이것은 설명입니다.", description);
        }

        @Test
        @DisplayName("백틱이 포함된 인용구에서 백틱을 제거한다")
        void removeBackticksFromDescription() {
            // given
            String markdown = "# Title\n\n> `[1] 정의` · 선수 지식: 없음\n\n본문";

            // when
            String description = extractor.extractDescription(markdown);

            // then
            assertFalse(description.contains("`"));
            assertTrue(description.contains("[1] 정의"));
        }

        @Test
        @DisplayName("긴 설명은 150자로 잘린다")
        void truncateLongDescription() {
            // given
            String longText = "A".repeat(200);
            String markdown = "# Title\n\n" + longText;

            // when
            String description = extractor.extractDescription(markdown);

            // then
            assertTrue(description.length() <= 153); // 150 + "..."
            assertTrue(description.endsWith("..."));
        }
    }

    @Nested
    @DisplayName("extractKeywords 메서드")
    class ExtractKeywordsTest {

        @Test
        @DisplayName("백틱으로 감싼 해시태그 키워드를 추출한다")
        void extractHashtagKeywords() {
            // given
            String markdown = "# Title\n\n`#Docker` `#컨테이너` `#DevOps`\n\n본문";

            // when
            List<String> keywords = extractor.extractKeywords(markdown);

            // then
            assertEquals(3, keywords.size());
            assertTrue(keywords.contains("Docker"));
            assertTrue(keywords.contains("컨테이너"));
            assertTrue(keywords.contains("DevOps"));
        }

        @Test
        @DisplayName("중복 키워드는 제거한다")
        void removeDuplicateKeywords() {
            // given
            String markdown = "`#Docker` `#Docker` `#컨테이너`";

            // when
            List<String> keywords = extractor.extractKeywords(markdown);

            // then
            assertEquals(2, keywords.size());
        }

        @Test
        @DisplayName("키워드가 없으면 빈 리스트를 반환한다")
        void returnEmptyListWhenNoKeywords() {
            // given
            String markdown = "# Title\n\n본문만 있는 마크다운";

            // when
            List<String> keywords = extractor.extractKeywords(markdown);

            // then
            assertTrue(keywords.isEmpty());
        }

        @Test
        @DisplayName("한글과 영문 키워드를 모두 추출한다")
        void extractKoreanAndEnglishKeywords() {
            // given
            String markdown = "`#캐싱` `#Caching` `#Redis` `#인메모리`";

            // when
            List<String> keywords = extractor.extractKeywords(markdown);

            // then
            assertEquals(4, keywords.size());
            assertTrue(keywords.contains("캐싱"));
            assertTrue(keywords.contains("Caching"));
            assertTrue(keywords.contains("Redis"));
            assertTrue(keywords.contains("인메모리"));
        }

        @Test
        @DisplayName("공백이 있는 키워드도 추출한다")
        void extractKeywordsWithSpaces() {
            // given
            String markdown = "`#Cold Start` `#Warm Start`";

            // when
            List<String> keywords = extractor.extractKeywords(markdown);

            // then
            assertEquals(2, keywords.size());
            assertTrue(keywords.contains("Cold Start"));
            assertTrue(keywords.contains("Warm Start"));
        }
    }

    @Nested
    @DisplayName("extractPrerequisites 메서드")
    class ExtractPrerequisitesTest {

        @Test
        @DisplayName("선수 지식 링크를 추출한다")
        void extractPrerequisiteLinks() {
            // given
            String markdown = "# Title\n\n> `[3] 중급` · 선수 지식: [Docker](./docker.md)\n\n본문";

            // when
            var prerequisites = extractor.extractPrerequisites(markdown, "system-design");

            // then
            assertEquals(1, prerequisites.size());
            assertEquals("Docker", prerequisites.get(0).getTitle());
            assertEquals("/docs/system-design/docker", prerequisites.get(0).getPath());
        }

        @Test
        @DisplayName("선수 지식이 없으면 빈 리스트를 반환한다")
        void returnEmptyListWhenNoPrerequisites() {
            // given
            String markdown = "# Title\n\n> `[1] 정의` · 선수 지식: 없음\n\n본문";

            // when
            var prerequisites = extractor.extractPrerequisites(markdown, "system-design");

            // then
            assertTrue(prerequisites.isEmpty());
        }
    }
}

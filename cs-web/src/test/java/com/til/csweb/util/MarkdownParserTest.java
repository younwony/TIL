package com.til.csweb.util;

import com.til.csweb.config.MarkdownParserConfig;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MarkdownParser 테스트")
class MarkdownParserTest {

    private MarkdownParser markdownParser;

    @BeforeEach
    void setUp() {
        MarkdownParserConfig config = new MarkdownParserConfig();
        MutableDataSet options = config.markdownOptions();
        Parser parser = config.markdownParser(options);
        HtmlRenderer renderer = config.htmlRenderer(options);
        markdownParser = new MarkdownParser(parser, renderer);
    }

    @Nested
    @DisplayName("toHtml 메서드")
    class ToHtmlTest {

        @Test
        @DisplayName("기본 마크다운을 HTML로 변환한다")
        void convertBasicMarkdown() {
            // given
            String markdown = "# Hello World\n\nThis is a paragraph.";

            // when
            String html = markdownParser.toHtml(markdown);

            // then
            assertTrue(html.contains("<h1"));
            assertTrue(html.contains("Hello World"));
            assertTrue(html.contains("</h1>"));
            assertTrue(html.contains("<p>This is a paragraph.</p>"));
        }

        @Test
        @DisplayName("테이블을 HTML로 변환한다")
        void convertTableMarkdown() {
            // given
            String markdown = "| Name | Age |\n|------|-----|\n| Kim | 25 |";

            // when
            String html = markdownParser.toHtml(markdown);

            // then
            assertTrue(html.contains("<table>"));
            assertTrue(html.contains("<th>Name</th>"));
            assertTrue(html.contains("<td>Kim</td>"));
        }

        @Test
        @DisplayName("코드 블록을 HTML로 변환한다")
        void convertCodeBlockMarkdown() {
            // given
            String markdown = "```java\npublic class Test {}\n```";

            // when
            String html = markdownParser.toHtml(markdown);

            // then
            assertTrue(html.contains("<code"));
            assertTrue(html.contains("public class Test {}"));
        }

        @Test
        @DisplayName("인용구를 HTML로 변환한다")
        void convertBlockquoteMarkdown() {
            // given
            String markdown = "> This is a quote";

            // when
            String html = markdownParser.toHtml(markdown);

            // then
            assertTrue(html.contains("<blockquote>"));
            assertTrue(html.contains("This is a quote"));
        }
    }
}

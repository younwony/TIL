package com.til.csweb.util;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 마크다운을 HTML로 변환하는 파서
 */
@Component
@RequiredArgsConstructor
public class MarkdownParser {

    private final Parser parser;
    private final HtmlRenderer renderer;

    /**
     * 마크다운을 HTML로 변환
     */
    public String toHtml(String markdown) {
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }
}

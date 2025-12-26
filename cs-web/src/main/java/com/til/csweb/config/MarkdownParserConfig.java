package com.til.csweb.config;

import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Flexmark 마크다운 파서 설정
 */
@Configuration
public class MarkdownParserConfig {

    @Bean
    public MutableDataSet markdownOptions() {
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
        return options;
    }

    @Bean
    public Parser flexmarkParser(MutableDataSet markdownOptions) {
        return Parser.builder(markdownOptions).build();
    }

    @Bean
    public HtmlRenderer htmlRenderer(MutableDataSet markdownOptions) {
        return HtmlRenderer.builder(markdownOptions).build();
    }
}

package com.til.csweb.constant;

import java.util.regex.Pattern;

/**
 * 문서 관련 상수 정의
 */
public final class DocumentConstants {

    private DocumentConstants() {
    }

    // 파일 확장자
    public static final String MARKDOWN_EXTENSION = ".md";
    public static final String README_FILENAME = "README.md";

    // 제한값
    public static final int DESCRIPTION_MAX_LENGTH = 150;
    public static final int SEARCH_RESULT_LIMIT = 20;
    public static final int DISPLAY_KEYWORDS_LIMIT = 5;
    public static final int PREREQUISITE_LIMIT = 5;

    // 기본값
    public static final String DEFAULT_TITLE = "Untitled";

    // 정규표현식 패턴
    public static final Pattern LEVEL_PATTERN = Pattern.compile("\\[([1-5])\\]\\s*(.+)");
    public static final Pattern PREREQUISITE_LINK_PATTERN = Pattern.compile("\\[([^\\]]+)\\]\\(([^)]+)\\)");
    public static final Pattern KEYWORD_PATTERN = Pattern.compile("`#([^`]+)`");
}

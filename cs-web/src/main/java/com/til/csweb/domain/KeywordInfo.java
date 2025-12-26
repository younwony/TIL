package com.til.csweb.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 키워드 정보 (키워드 및 사용 빈도)
 */
@Getter
@RequiredArgsConstructor
public class KeywordInfo {

    private final String keyword;
    private final int count;
}

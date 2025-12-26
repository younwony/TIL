package com.til.csweb.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 선수 지식 정보
 */
@Getter
@RequiredArgsConstructor
public class PrerequisiteInfo {

    private final String title;
    private final String path;
}

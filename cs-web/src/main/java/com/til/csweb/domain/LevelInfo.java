package com.til.csweb.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 문서 난이도 정보
 */
@Getter
@RequiredArgsConstructor
public class LevelInfo {

    private final Integer level;
    private final String name;

    public static LevelInfo empty() {
        return new LevelInfo(null, null);
    }

    public boolean hasLevel() {
        return level != null && name != null;
    }
}

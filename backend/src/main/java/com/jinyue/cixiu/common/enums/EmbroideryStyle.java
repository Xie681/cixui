package com.jinyue.cixiu.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Locale;

/**
 * Embroidery style enum.
 */
@Getter
public enum EmbroideryStyle {

    SU("Su embroidery"),
    XIANG("Xiang embroidery"),
    SHU("Shu embroidery"),
    YUE("Yue embroidery");

    private final String description;

    EmbroideryStyle(String description) {
        this.description = description;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static EmbroideryStyle fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return switch (value.trim().toLowerCase(Locale.ROOT)) {
            case "su", "suxiu", "苏绣" -> SU;
            case "xiang", "xiangxiu", "湘绣" -> XIANG;
            case "shu", "shuxiu", "蜀绣" -> SHU;
            case "yue", "yuexiu", "粤绣" -> YUE;
            default -> EmbroideryStyle.valueOf(value.trim().toUpperCase(Locale.ROOT));
        };
    }
}

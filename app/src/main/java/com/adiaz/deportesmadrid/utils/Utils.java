package com.adiaz.deportesmadrid.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

public class Utils {

    /**
     * If input is null or empty return "-"
     * Else return input with first letter in uppercase.
     *
     * @param input
     * @return
     */
    public static final String normalizeString(String input) {
        if (StringUtils.isEmpty(input)) {
            return "-";
        } else {
            return WordUtils.capitalizeFully(input.toLowerCase());
        }
    }
}

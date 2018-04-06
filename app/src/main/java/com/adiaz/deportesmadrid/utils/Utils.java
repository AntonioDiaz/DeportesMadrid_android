package com.adiaz.deportesmadrid.utils;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.View;

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


    public static final void showSnack(View view, String text) {
        final Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}

package com.adiaz.deportesmadrid.utils;

import android.support.annotation.IntDef;

import com.adiaz.deportesmadrid.fragments.SettingsFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class StateAnnotation {

    // Constants
    public static final int PENDIENTE = 0;
    public static final int REPROGRAMADO = 1;
    public static final int COMITE = 2;
    public static final int FINALIZADO = 3;
    public static final int SUSPENDIDO = 4;
    public static final int NO_PRESENTADO = 5;
    public static final int APLAZADO = 6;
    public static final int DESCONOCIDO = 7;
    public static final int DESCANSA = 8;
    public static final String PENDIENTE_KEY = "PENDIENTE";
    public static final String REPROGRAMADO_KEY = "REPROGRAMADO";
    public static final String COMITE_KEY = "COMITE";
    public static final String FINALIZADO_KEY = "FINALIZADO";
    public static final String SUSPENDIDO_KEY = "SUSPENDIDO";
    public static final String NO_PRESENTADO_KEY = "NO_PRESENTADO";
    public static final String APLAZADO_KEY = "APLAZADO";
    public static final String DESCONOCIDO_KEY = "DESCONOCIDO";
    public static final String DESCANSA_KEY = "DESCANSA";

    public static final String stringKey(@StateDef int state) {
        String description = null;
        switch (state) {
            case PENDIENTE:
                description = PENDIENTE_KEY;
                break;
            case REPROGRAMADO:
                description = REPROGRAMADO_KEY;
                break;
            case COMITE:
                description = COMITE_KEY;
                break;
            case FINALIZADO:
                description = FINALIZADO_KEY;
                break;
            case SUSPENDIDO:
                description = SUSPENDIDO_KEY;
                break;
            case NO_PRESENTADO:
                description = NO_PRESENTADO_KEY;
                break;
            case APLAZADO:
                description = APLAZADO_KEY;
                break;
            case DESCONOCIDO:
                description = DESCONOCIDO_KEY;
                break;
            case DESCANSA:
                description = DESCANSA_KEY;
                break;
        }
        return description;
    }

    // Declare the @IntDef for these constants
    @IntDef({PENDIENTE, REPROGRAMADO, COMITE, FINALIZADO, SUSPENDIDO, NO_PRESENTADO, APLAZADO, DESCONOCIDO, DESCANSA})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StateDef {
    }

}

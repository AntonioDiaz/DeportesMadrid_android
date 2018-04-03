package com.adiaz.deportesmadrid.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.adiaz.deportesmadrid.retrofit.competitions.CompetitionRetrofitEntity;

/**
 * Created by adiaz on 22/3/18.
 */

public class DbContract {

    public static final String AUTHORITY = "com.adiaz.deportesmadrid";
    public static final Uri BASE_CONTENT = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_COMPETITIONS = "competitions";


    public static final class CompetitionEntry {
        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_COMPETITIONS).build();
        //table
        public static final String TABLE_NAME = "COMPETITIONS";
        //columns
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_COD_TEMPORADA = "COD_TEMPORADA";
        public static final String COLUMN_COD_COMPETICION = "COD_COMPETICION";
        public static final String COLUMN_COD_FASE = "COD_FASE";
        public static final String COLUMN_COD_GRUPO = "COD_GRUPO";
        public static final String COLUMN_NOM_TEMPORADA = "NOM_TEMPORADA";
        public static final String COLUMN_NOM_COMPETICION = "NOM_COMPETICION";
        public static final String COLUMN_NOM_FASE = "NOM_FASE";
        public static final String COLUMN_NOM_GRUPO = "NOM_GRUPO";
        public static final String COLUMN_DEPORTE = "DEPORTE";
        public static final String COLUMN_DISTRITO = "DISTRITO";

        public static final String[] PROJECTION = {
                COLUMN_ID,
                COLUMN_COD_TEMPORADA,
                COLUMN_COD_COMPETICION,
                COLUMN_COD_FASE,
                COLUMN_COD_GRUPO,
                COLUMN_NOM_TEMPORADA,
                COLUMN_NOM_COMPETICION,
                COLUMN_NOM_FASE,
                COLUMN_NOM_GRUPO,
                COLUMN_DEPORTE,
                COLUMN_DISTRITO
        };

        public static final int INDEX_ID = 0;
        public static final int INDEX_COD_TEMPORADA = 1;
        public static final int INDEX_COD_COMPETICION = 2;
        public static final int INDEX_COD_FASE = 3;
        public static final int INDEX_COD_GRUPO = 4;
        public static final int INDEX_NOM_TEMPORADA = 5;
        public static final int INDEX_NOM_COMPETICION = 6;
        public static final int INDEX_NOM_FASE = 7;
        public static final int INDEX_NOM_GRUPO = 8;
        public static final int INDEX_DEPORTE = 9;
        public static final int INDEX_DISTRITO = 10;

        public static ContentValues retrofitEntityToContentValue(CompetitionRetrofitEntity competitionRetrofitEntity) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_ID, competitionRetrofitEntity.getId());
            cv.put(COLUMN_COD_TEMPORADA, competitionRetrofitEntity.getCodTemporada());
            cv.put(COLUMN_COD_COMPETICION, competitionRetrofitEntity.getCodCompeticion());
            cv.put(COLUMN_COD_FASE, competitionRetrofitEntity.getCodFase());
            cv.put(COLUMN_COD_GRUPO, competitionRetrofitEntity.getCodGrupo());
            cv.put(COLUMN_NOM_TEMPORADA, competitionRetrofitEntity.getNombreTemporada());
            cv.put(COLUMN_NOM_COMPETICION, competitionRetrofitEntity.getNombreCompeticion());
            cv.put(COLUMN_NOM_FASE, competitionRetrofitEntity.getNombreFase());
            cv.put(COLUMN_NOM_GRUPO, competitionRetrofitEntity.getNombreGrupo());
            cv.put(COLUMN_DEPORTE, competitionRetrofitEntity.getDeporte());
            cv.put(COLUMN_DISTRITO, competitionRetrofitEntity.getDistrito());
            return cv;
        }

        public static CompetitionEntry cursorToEntry(Cursor cursor) {
            return null;
        }
    }

}

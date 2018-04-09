package com.adiaz.deportesmadrid.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.adiaz.deportesmadrid.db.entities.Competition;
import com.adiaz.deportesmadrid.db.entities.Favorite;
import com.adiaz.deportesmadrid.retrofit.competitions.CompetitionRetrofitEntity;
import com.adiaz.deportesmadrid.retrofit.matches.MatchRetrofitEntity;
import com.adiaz.deportesmadrid.utils.Utils;

/**
 * Created by adiaz on 22/3/18.
 */

public class DbContract {
    //private static final String TAG = DbContract.class.getSimpleName();
    public static final String AUTHORITY = "com.adiaz.deportesmadrid";
    public static final Uri BASE_CONTENT = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_COMPETITIONS = "competitions";
    public static final String PATH_FAVORITES = "favorites";
    //public static final String PATH_MATCHES = "matches";


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
        public static final String COLUMN_CATEGORIA = "CATEGORIA";

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
                COLUMN_DISTRITO,
                COLUMN_CATEGORIA
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
        public static final int INDEX_CATEGORIA = 11;

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
            cv.put(COLUMN_DEPORTE, Utils.normalizeString(competitionRetrofitEntity.getDeporte()));
            cv.put(COLUMN_DISTRITO, Utils.normalizeString(competitionRetrofitEntity.getDistrito()));
            cv.put(COLUMN_CATEGORIA, Utils.normalizeString(competitionRetrofitEntity.getCategoria()));
            return cv;
        }

        public static Competition cursorToEntity(Cursor cursor) {
            //Competition competition
            return Competition.builder()
                    .id(cursor.getString(INDEX_ID))
                    .codTemporada(cursor.getInt(INDEX_COD_TEMPORADA))
                    .codCompeticion(cursor.getInt(INDEX_COD_COMPETICION))
                    .codFase(cursor.getInt(INDEX_COD_FASE))
                    .codGrupo(cursor.getInt(INDEX_COD_GRUPO))
                    .nomTemporada(cursor.getString(INDEX_NOM_TEMPORADA))
                    .nomCompeticion(cursor.getString(INDEX_NOM_COMPETICION))
                    .nomFase(cursor.getString(INDEX_NOM_FASE))
                    .nomGrupo(cursor.getString(INDEX_NOM_GRUPO))
                    .deporte(cursor.getString(INDEX_DEPORTE))
                    .distrito(cursor.getString(INDEX_DISTRITO))
                    .categoria(cursor.getString(INDEX_CATEGORIA))
                    .build();
        }
    }

    public static final class FavoritesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final Uri buildFavoritesUri(Long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id.toString())
                    .build();
        }

        public static final String TABLE_NAME = "Favorites";
        public static final String COLUMN_ID_COMPETITION = "id_competition";
        public static final String COLUMN_ID_TEAM = "id_team";

        public static final String[] PROJECTION = {_ID, COLUMN_ID_COMPETITION, COLUMN_ID_TEAM};
        public static final int INDEX_ID = 0;
        public static final int INDEX_ID_COMPETITION = 1;
        public static final int INDEX_ID_TEAM = 2;


        public static Favorite initEntity(Cursor cursor) {
            return Favorite.builder()
                    .id(cursor.getLong(INDEX_ID))
                    .idCompetition(cursor.getString(INDEX_ID_COMPETITION))
                    .idTeam(cursor.getString(INDEX_ID_TEAM)).build();
        }

        public static ContentValues entityToContentValues(Favorite favorite) {
            ContentValues cv = new ContentValues();
            cv.put(_ID, favorite.id());
            cv.put(COLUMN_ID_COMPETITION, favorite.idCompetition());
            cv.put(COLUMN_ID_TEAM, favorite.idTeam());
            return cv;
        }
    }

    /*
    public static final class MatchEntry {
        public static final Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_MATCHES).build();
        //table
        public static final String TABLE_NAME = "MATCHES";
        //columns
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_COD_COMPETITION = "ID_COMPETITION";
        public static final String COLUMN_COD_TEAM_LOCAL = "COD_TEAM_LOCAL";
        public static final String COLUMN_COD_TEAM_VISITOR = "COD_TEAM_VISITOR";
        public static final String COLUMN_COD_PLACE = "COD_PLACE";
        public static final String COLUMN_NUM_WEEK = "NUM_WEEK";
        public static final String COLUMN_NUM_MATCH = "NUM_MATCH";
        public static final String COLUMN_SCORE_LOCAL = "SCORE_LOCAL";
        public static final String COLUMN_SCORE_VISITOR = "SCORE_VISITOR";
        public static final String COLUMN_STATE = "STATE";
        public static final String COLUMN_DATE = "DATE";


        public static final String[] PROJECTION = {
                COLUMN_ID,
                COLUMN_COD_COMPETITION,
                COLUMN_COD_TEAM_LOCAL,
                COLUMN_COD_TEAM_VISITOR,
                COLUMN_COD_PLACE,
                COLUMN_NUM_WEEK,
                COLUMN_NUM_MATCH,
                COLUMN_SCORE_LOCAL,
                COLUMN_SCORE_VISITOR,
                COLUMN_STATE,
                COLUMN_DATE
        };

        public static final int INDEX_ID = 0;
        public static final int INDEX_COD_COMPETITION = 1;
        public static final int INDEX_COD_TEAM_LOCAL = 2;
        public static final int INDEX_COD_TEAM_VISITOR = 3;
        public static final int INDEX_COD_PLACE = 4;
        public static final int INDEX_NUM_WEEK = 5;
        public static final int INDEX_NUM_MATCH = 6;
        public static final int INDEX_SCORE_LOCAL = 7;
        public static final int INDEX_SCORE_VISITOR = 8;
        public static final int INDEX_STATE = 9;
        public static final int INDEX_DATE = 10;


        public static ContentValues retrofitEntityToContentValue(MatchRetrofitEntity matchRetrofitEntity) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_ID, matchRetrofitEntity.getId());
            cv.put(COLUMN_COD_COMPETITION, matchRetrofitEntity.getIdCompetition());
            cv.put(COLUMN_COD_TEAM_LOCAL, matchRetrofitEntity.getIdTeamLocal());
            cv.put(COLUMN_COD_TEAM_VISITOR, matchRetrofitEntity.getIdTeamVisitor());
            cv.put(COLUMN_COD_PLACE, matchRetrofitEntity.getIdPlace());
            cv.put(COLUMN_NUM_WEEK, matchRetrofitEntity.getNumWeek());
            cv.put(COLUMN_NUM_MATCH, matchRetrofitEntity.getNumMatch());
            cv.put(COLUMN_SCORE_LOCAL, matchRetrofitEntity.getScoreLocal());
            cv.put(COLUMN_SCORE_VISITOR, matchRetrofitEntity.getScoreVisitor());
            cv.put(COLUMN_STATE, matchRetrofitEntity.getState());
            cv.put(COLUMN_DATE, matchRetrofitEntity.getDate());

            return cv;
        }

        public static Match cursorToEntity(Cursor cursor) {
            //Competition competition
            return Competition.builder()
                    .id(cursor.getString(INDEX_ID))
                    .codTemporada(cursor.getInt(INDEX_COD_TEMPORADA))
                    .codCompeticion(cursor.getInt(INDEX_COD_COMPETICION))
                    .codFase(cursor.getInt(INDEX_COD_FASE))
                    .codGrupo(cursor.getInt(INDEX_COD_GRUPO))
                    .nomTemporada(cursor.getString(INDEX_NOM_TEMPORADA))
                    .nomCompeticion(cursor.getString(INDEX_NOM_COMPETICION))
                    .nomFase(cursor.getString(INDEX_NOM_FASE))
                    .nomGrupo(cursor.getString(INDEX_NOM_GRUPO))
                    .deporte(cursor.getString(INDEX_DEPORTE))
                    .distrito(cursor.getString(INDEX_DISTRITO))
                    .categoria(cursor.getString(INDEX_CATEGORIA))
                    .build();
        }
    }
    */


}
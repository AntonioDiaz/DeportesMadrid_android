package com.adiaz.deportesmadrid.db.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.adiaz.deportesmadrid.db.entities.Competition;
import com.adiaz.deportesmadrid.retrofit.competitions.CompetitionRetrofitEntity;

import static com.adiaz.deportesmadrid.db.DbContract.CompetitionEntry;

import java.util.ArrayList;
import java.util.List;

public class CompetitionsDAO {


    public static void insertCompetitions(Context context, List<CompetitionRetrofitEntity> competitionsList) {
        List<ContentValues> contentValues = new ArrayList<>();
        for (CompetitionRetrofitEntity competitionRetrofitEntity : competitionsList) {
            ContentValues cv = CompetitionEntry.retrofitEntityToContentValue(competitionRetrofitEntity);
            contentValues.add(cv);
        }
        ContentValues[] array = contentValues.toArray(new ContentValues[contentValues.size()]);

        //before insert it is necessary to delete all.
        context.getContentResolver().delete(CompetitionEntry.CONTENT_URI, null, null);
        context.getContentResolver().bulkInsert(CompetitionEntry.CONTENT_URI, array);
    }

    public static List<Competition> queryAllCompetitions(Context context) {
        List<Competition> competitions = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(CompetitionEntry.CONTENT_URI, CompetitionEntry.PROJECTION, null, null, null);
            while (cursor!=null && cursor.moveToNext()) {
                competitions.add(CompetitionEntry.cursorToEntity(cursor));
            }
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        return competitions;
    }

    public static Competition queryCompetitionsById(Context context, String idCompetion) {
        String selection = CompetitionEntry.COLUMN_ID + "=?";
        String[] selectionArgs = new String[]{idCompetion};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(CompetitionEntry.CONTENT_URI, CompetitionEntry.PROJECTION, selection, selectionArgs, null);
            if (cursor!=null && cursor.moveToNext()) {
                return CompetitionEntry.cursorToEntity(cursor);
            }
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        return null;
    }

    public static List<Competition> queryCompetitionsBySport(Context context, String sportName) {
        List<Competition> competitions = new ArrayList<>();
        String selection = CompetitionEntry.COLUMN_DEPORTE + "=?";
        String[] selectionArgs = new String[]{sportName};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(CompetitionEntry.CONTENT_URI, CompetitionEntry.PROJECTION, selection, selectionArgs, null);
            while (cursor!=null && cursor.moveToNext()) {
                competitions.add(CompetitionEntry.cursorToEntity(cursor));
            }
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        return competitions;
    }

    public static List<Competition> queryCompetitionsBySportAndDistrict(Context context, String sportName, String district) {
        List<Competition> competitions = new ArrayList<>();
        String selection = CompetitionEntry.COLUMN_DEPORTE + "=?";
        selection += "and " + CompetitionEntry.COLUMN_DISTRITO + "=?";
        String[] selectionArgs = new String[]{sportName, district};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(CompetitionEntry.CONTENT_URI, CompetitionEntry.PROJECTION, selection, selectionArgs, null);
            while (cursor!=null && cursor.moveToNext()) {
                competitions.add(CompetitionEntry.cursorToEntity(cursor));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return competitions;
    }

    public static List<Competition> queryCompetitionsBySportAndDistrictAndCategory(Context context, String sportName, String district, String category) {
        List<Competition> competitions = new ArrayList<>();
        String selection = CompetitionEntry.COLUMN_DEPORTE + "=?";
        selection += "and " + CompetitionEntry.COLUMN_DISTRITO + "=?";
        selection += "and " + CompetitionEntry.COLUMN_CATEGORIA + "=?";
        String[] selectionArgs = new String[]{sportName, district, category};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(CompetitionEntry.CONTENT_URI, CompetitionEntry.PROJECTION, selection, selectionArgs, null);
            while (cursor!=null && cursor.moveToNext()) {
                competitions.add(CompetitionEntry.cursorToEntity(cursor));
            }
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        return competitions;
    }
}

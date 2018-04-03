package com.adiaz.deportesmadrid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.adiaz.deportesmadrid.db.entities.Competition;
import com.adiaz.deportesmadrid.retrofit.competitions.CompetitionRetrofitEntity;

import static com.adiaz.deportesmadrid.db.DbContract.CompetitionEntry;

import java.util.ArrayList;
import java.util.List;

public class CompetitionsDAO {


    public final static Integer insertCompetitions(Context context, List<CompetitionRetrofitEntity> competitionsList) {
        List<ContentValues> contentValues = new ArrayList<>();
        for (CompetitionRetrofitEntity competitionRetrofitEntity : competitionsList) {
            ContentValues cv = CompetitionEntry.retrofitEntityToContentValue(competitionRetrofitEntity);
            contentValues.add(cv);
        }
        ContentValues[] array = contentValues.toArray(new ContentValues[contentValues.size()]);

        //before insert it is necessary to delete all.
        context.getContentResolver().delete(CompetitionEntry.CONTENT_URI, null, null);
        int newRecords = context.getContentResolver().bulkInsert(CompetitionEntry.CONTENT_URI, array);
        return newRecords;
    }

    public final static List<Competition> queryAllCompetitions(Context context) {
        List<Competition> competitions = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(CompetitionEntry.CONTENT_URI, CompetitionEntry.PROJECTION, null, null, null);
        while (cursor.moveToNext()) {
            //CompetitionEntry.
            competitions.add(CompetitionEntry.cursorToEntity(cursor));
        }
        return competitions;
    }

    public final static List<Competition> queryCompetitionsBySport(Context context, String sportName) {
        List<Competition> competitions = new ArrayList<>();
        String selection = CompetitionEntry.COLUMN_DEPORTE + "=?";
        String[] selectionArgs = new String[]{sportName};
        Cursor cursor = context.getContentResolver().query(CompetitionEntry.CONTENT_URI, CompetitionEntry.PROJECTION, selection, selectionArgs, null);
        while (cursor.moveToNext()) {
            //CompetitionEntry.
            competitions.add(CompetitionEntry.cursorToEntity(cursor));
        }
        return competitions;
    }

}

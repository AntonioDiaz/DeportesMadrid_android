package com.adiaz.deportesmadrid.db;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.adiaz.deportesmadrid.db.DbContract.CompetitionEntry;
import com.adiaz.deportesmadrid.db.DbContract.FavoritesEntry;

/**
 * Created by adiaz on 22/3/18.
 */

public class DbHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "deportesMadrid.db";
    public static final String REFRESH_BROADCAST = "REFRESH_COMPETITIONS_BROADCAST";

    public static final int DATABASE_VERSION = 6;
    private Context mContext;


    public DbHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_TABLE_COMPETITIONS =
                "CREATE TABLE " + CompetitionEntry.TABLE_NAME +
                        "(" +
                        CompetitionEntry.COLUMN_ID + " TEXT PRIMARY KEY, " +
                        CompetitionEntry.COLUMN_COD_TEMPORADA + " INTEGER NOT NULL, " +
                        CompetitionEntry.COLUMN_COD_COMPETICION + " INTEGER NOT NULL, " +
                        CompetitionEntry.COLUMN_COD_FASE + " INTEGER NOT NULL, " +
                        CompetitionEntry.COLUMN_COD_GRUPO + " INTEGER NOT NULL, " +
                        CompetitionEntry.COLUMN_NOM_TEMPORADA + " TEXT, " +
                        CompetitionEntry.COLUMN_NOM_COMPETICION + " TEXT, " +
                        CompetitionEntry.COLUMN_NOM_FASE + " TEXT, " +
                        CompetitionEntry.COLUMN_NOM_GRUPO + " TEXT, " +
                        CompetitionEntry.COLUMN_DEPORTE + " TEXT, " +
                        CompetitionEntry.COLUMN_DISTRITO + " TEXT, " +
                        CompetitionEntry.COLUMN_CATEGORIA + " TEXT " +
                        ") ";
        String SQL_CREATE_TABLE_FAVORITES =
                "CREATE TABLE " + FavoritesEntry.TABLE_NAME +
                        "(" +
                        FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoritesEntry.COLUMN_ID_COMPETITION + " INTEGER NOT NULL, " +
                        FavoritesEntry.COLUMN_ID_TEAM + " TEXT " +
                        ") ";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_COMPETITIONS);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_FAVORITES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CompetitionEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
        mContext.sendBroadcast(new Intent(DbHelper.REFRESH_BROADCAST));
    }
}

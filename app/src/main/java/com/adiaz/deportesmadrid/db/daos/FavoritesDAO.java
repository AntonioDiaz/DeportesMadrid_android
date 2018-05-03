package com.adiaz.deportesmadrid.db.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.adiaz.deportesmadrid.db.entities.Favorite;

import java.util.ArrayList;
import java.util.List;

import static com.adiaz.deportesmadrid.db.DbContract.FavoritesEntry;

public class FavoritesDAO {

    public static void insertFavorite(Context context, Favorite favorite) {
        ContentValues cv = FavoritesEntry.entityToContentValues(favorite);
        context.getContentResolver().insert(FavoritesEntry.CONTENT_URI, cv);
    }

    public static void deleteFavorite(Context context, Long idFavorite) {
        String where = FavoritesEntry._ID + "=?";
        String[] selectionArgs = new String[]{idFavorite.toString()};
        context.getContentResolver().delete(FavoritesEntry.CONTENT_URI, where, selectionArgs);
    }

    public static List<Favorite> queryFavorites(Context context) {
        List<Favorite> favorites = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(FavoritesEntry.CONTENT_URI,
                    FavoritesEntry.PROJECTION, null, null, null);
            while (cursor!=null && cursor.moveToNext()) {
                favorites.add(FavoritesEntry.initEntity(cursor));
            }
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        return favorites;
    }

    public static Favorite queryFavorite(Context context, String idCompetition) {
        Favorite favoriteFound = null;
        String where = FavoritesEntry.COLUMN_ID_GROUP + "=?";
        where += "and " + FavoritesEntry.COLUMN_ID_TEAM + " is null";
        String[] selectionArgs = new String[]{idCompetition};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(FavoritesEntry.CONTENT_URI,
                    FavoritesEntry.PROJECTION, where, selectionArgs, null);
            if (cursor!=null && cursor.moveToNext()) {
                favoriteFound = FavoritesEntry.initEntity(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return favoriteFound;
    }

    public static Favorite queryFavorite(Context context, String idCompetition, String idTeam) {
        Favorite favoriteFound = null;
        String where = FavoritesEntry.COLUMN_ID_TEAM + "=?";
        where += "AND " + FavoritesEntry.COLUMN_ID_GROUP + "=?";
        String[] selectionArgs = new String[]{idTeam, idCompetition};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(FavoritesEntry.CONTENT_URI,
                    FavoritesEntry.PROJECTION, where, selectionArgs, null);
            if (cursor!=null && cursor.moveToNext()) {
                favoriteFound = FavoritesEntry.initEntity(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return favoriteFound;
    }
}

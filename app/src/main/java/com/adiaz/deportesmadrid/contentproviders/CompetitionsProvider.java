package com.adiaz.deportesmadrid.contentproviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.adiaz.deportesmadrid.db.DbHelper;
import com.adiaz.deportesmadrid.db.DbContract;
import com.adiaz.deportesmadrid.db.DbContract.GroupEntry;
import com.adiaz.deportesmadrid.db.DbContract.FavoritesEntry;

/**
 * Created by adiaz on 23/3/18.
 */

public class CompetitionsProvider extends ContentProvider {

    //private static final String TAG = CompetitionsProvider.class.getSimpleName();

    private DbHelper mDbHelper;

    //private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int COMPETITIONS = 100;
    private static final int FAVORITES = 200;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_COMPETITIONS, COMPETITIONS);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_FAVORITES, FAVORITES);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int rowsInserted = 0;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        switch (buildUriMatcher().match(uri)) {
            case COMPETITIONS:
                try {
                    db.beginTransaction();
                    for (ContentValues value : values) {
                        long id = db.insert(GroupEntry.TABLE_NAME, null, value);
                        if (id!=-1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
        }
        if (rowsInserted>0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsInserted;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        Cursor cursor;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        switch (buildUriMatcher().match(uri)) {
            case COMPETITIONS:
                cursor = db.query(GroupEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy);
                break;
            case FAVORITES:
                cursor = db.query(FavoritesEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy);
                break;
            default:
                throw new UnsupportedOperationException("error " + uri);
        }
        if (cursor!=null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri returnedUri;
        switch (buildUriMatcher().match(uri)) {
            case FAVORITES:
                long idNew = db.insert(FavoritesEntry.TABLE_NAME, null, contentValues);
                if (idNew>0) {
                    returnedUri = FavoritesEntry.buildFavoritesUri(idNew);
                } else {
                    throw new SQLException("error on insert" + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("error " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnedUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int deletedItems;
        switch (buildUriMatcher().match(uri)) {
            case COMPETITIONS:
                deletedItems = db.delete(GroupEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITES:
                deletedItems = db.delete(FavoritesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("error " + uri);
        }
        if (deletedItems>0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedItems;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

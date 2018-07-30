package com.adiaz.deportesmadrid.db.daos

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

import com.adiaz.deportesmadrid.db.entities.Favorite

import java.util.ArrayList

import com.adiaz.deportesmadrid.db.DbContract.FavoritesEntry

object FavoritesDAO {

    fun insertFavorite(context: Context, favorite: Favorite) {
        val cv = FavoritesEntry.entityToContentValues(favorite)
        context.contentResolver.insert(FavoritesEntry.CONTENT_URI, cv)
    }

    fun deleteFavorite(context: Context, idFavorite: Long) {
        val where = FavoritesEntry._ID + "=?"
        val selectionArgs = arrayOf(idFavorite.toString())
        context.contentResolver.delete(FavoritesEntry.CONTENT_URI, where, selectionArgs)
    }

    fun queryFavoritesYear(context: Context, year: String?): List<Favorite> {
        val favorites = ArrayList<Favorite>()
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(FavoritesEntry.CONTENT_URI,
                    FavoritesEntry.PROJECTION, null, null, null)
            while (cursor != null && cursor.moveToNext()) {
                val favorite = FavoritesEntry.initEntity(cursor)
                if (year == null || favorite.idGroup.startsWith(year)) {
                    favorites.add(favorite)
                }
            }
        } finally {
            cursor?.close()
        }
        return favorites
    }

    fun queryFavorite(context: Context, idCompetition: String): Favorite? {
        var favoriteFound: Favorite? = null
        var where = FavoritesEntry.COLUMN_ID_GROUP + "=?"
        where += "and " + FavoritesEntry.COLUMN_ID_TEAM + " is null"
        val selectionArgs = arrayOf(idCompetition)
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(FavoritesEntry.CONTENT_URI,
                    FavoritesEntry.PROJECTION, where, selectionArgs, null)
            if (cursor != null && cursor.moveToNext()) {
                favoriteFound = FavoritesEntry.initEntity(cursor)
            }
        } finally {
            cursor?.close()
        }
        return favoriteFound
    }

    fun queryFavorite(context: Context, idCompetition: String, idTeam: Long): Favorite? {
        var favoriteFound: Favorite? = null
        var where = FavoritesEntry.COLUMN_ID_TEAM + "=?"
        where += "AND " + FavoritesEntry.COLUMN_ID_GROUP + "=?"
        val selectionArgs = arrayOf(idTeam.toString(), idCompetition)
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(FavoritesEntry.CONTENT_URI,
                    FavoritesEntry.PROJECTION, where, selectionArgs, null)
            if (cursor != null && cursor.moveToNext()) {
                favoriteFound = FavoritesEntry.initEntity(cursor)
            }
        } finally {
            cursor?.close()
        }
        return favoriteFound
    }

    fun queryFavorite(context: Context, idTeam: Long): Favorite? {
        var favoriteFound: Favorite? = null
        val where = FavoritesEntry.COLUMN_ID_TEAM + "=?"
        val selectionArgs = arrayOf(idTeam.toString())
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(FavoritesEntry.CONTENT_URI,
                    FavoritesEntry.PROJECTION, where, selectionArgs, null)
            if (cursor != null && cursor.moveToNext()) {
                favoriteFound = FavoritesEntry.initEntity(cursor)
            }
        } finally {
            cursor?.close()
        }
        return favoriteFound
    }
}

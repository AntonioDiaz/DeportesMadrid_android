package com.adiaz.deportesmadrid.contentproviders

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

import com.adiaz.deportesmadrid.db.DbHelper
import com.adiaz.deportesmadrid.db.DbContract
import com.adiaz.deportesmadrid.db.DbContract.GroupEntry
import com.adiaz.deportesmadrid.db.DbContract.FavoritesEntry

/**
 * Created by adiaz on 23/3/18.
 */

class CompetitionsProvider : ContentProvider() {

    private var mDbHelper: DbHelper? = null

    override fun onCreate(): Boolean {
        mDbHelper = DbHelper(context!!)
        return true
    }

    override fun bulkInsert(uri: Uri, values: Array<ContentValues>): Int {
        var rowsInserted = 0
        val db = mDbHelper!!.writableDatabase
        when (buildUriMatcher().match(uri)) {
            COMPETITIONS -> try {
                db.beginTransaction()
                for (value in values) {
                    val id = db.insert(GroupEntry.TABLE_NAME, null, value)
                    if (id != -1L) {
                        rowsInserted++
                    }
                }
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }
        if (rowsInserted > 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return rowsInserted
    }

    override fun query(uri: Uri, columns: Array<String>?, selection: String?, selectionArgs: Array<String>?, orderBy: String?): Cursor? {
        val cursor: Cursor?
        val db = mDbHelper!!.readableDatabase
        when (buildUriMatcher().match(uri)) {
            COMPETITIONS -> cursor = db.query(GroupEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy)
            FAVORITES -> cursor = db.query(FavoritesEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy)
            else -> throw UnsupportedOperationException("error $uri")
        }
        cursor?.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val db = mDbHelper!!.writableDatabase
        val returnedUri: Uri
        when (buildUriMatcher().match(uri)) {
            FAVORITES -> {
                val idNew = db.insert(FavoritesEntry.TABLE_NAME, null, contentValues)
                if (idNew > 0) {
                    returnedUri = FavoritesEntry.buildFavoritesUri(idNew)
                } else {
                    throw SQLException("error on insert$uri")
                }
            }
            else -> throw UnsupportedOperationException("error $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return returnedUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db = mDbHelper!!.writableDatabase
        val deletedItems: Int
        when (buildUriMatcher().match(uri)) {
            COMPETITIONS -> deletedItems = db.delete(GroupEntry.TABLE_NAME, selection, selectionArgs)
            FAVORITES -> deletedItems = db.delete(FavoritesEntry.TABLE_NAME, selection, selectionArgs)
            else -> throw UnsupportedOperationException("error $uri")
        }
        if (deletedItems > 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return deletedItems
    }

    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<String>?): Int {
        return 0
    }

    companion object {

        private val COMPETITIONS = 100
        private val FAVORITES = 200

        private fun buildUriMatcher(): UriMatcher {
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_COMPETITIONS, COMPETITIONS)
            uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_FAVORITES, FAVORITES)
            return uriMatcher
        }
    }
}

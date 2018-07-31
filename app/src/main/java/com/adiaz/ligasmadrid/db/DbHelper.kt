package com.adiaz.ligasmadrid.db

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.adiaz.ligasmadrid.db.DbContract.GroupEntry
import com.adiaz.ligasmadrid.db.DbContract.FavoritesEntry
import com.adiaz.ligasmadrid.db.DbContract.FavoritesEntry.Companion.SQL_CREATE_TABLE_FAVORITES
import com.adiaz.ligasmadrid.db.DbContract.FavoritesEntry.Companion.SQL_DELETE_TABLE_FAVORITES
import com.adiaz.ligasmadrid.db.DbContract.GroupEntry.SQL_CREATE_TABLE_GROUPS
import com.adiaz.ligasmadrid.db.DbContract.GroupEntry.SQL_DELETE_TABLE_GROUPS

/**
 * Created by adiaz on 22/3/18.
 */

class DbHelper(private val mContext: Context) : SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_GROUPS)
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_FAVORITES)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_GROUPS)
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_FAVORITES)
        onCreate(sqLiteDatabase)
        mContext.sendBroadcast(Intent(DbHelper.REFRESH_BROADCAST))
    }

    companion object {
        val DATABASE_NAME = "ligasMadrid.db"
        val REFRESH_BROADCAST = "REFRESH_COMPETITIONS_BROADCAST"
        val DATABASE_VERSION = 10
    }
}

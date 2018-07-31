package com.adiaz.ligasmadrid.db.daos

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

import com.adiaz.ligasmadrid.db.entities.Group
import com.adiaz.ligasmadrid.retrofit.groupslist.GroupRetrofitEntity
import com.adiaz.ligasmadrid.utils.Constants

import com.adiaz.ligasmadrid.db.DbContract.GroupEntry

import java.util.ArrayList

object GroupsDAO {


    fun insertCompetitions(context: Context, competitionsList: List<GroupRetrofitEntity>) {
        val contentValues = ArrayList<ContentValues>()
        for (GroupRetrofitEntity in competitionsList) {
            if (GroupRetrofitEntity.distrito.equals(Constants.DGD, ignoreCase = true)) {
                GroupRetrofitEntity.distrito = Constants.DISTRITO_UNICO
            }
            val cv = GroupEntry.retrofitEntityToContentValue(GroupRetrofitEntity)
            contentValues.add(cv)
        }
        val array = contentValues.toTypedArray()

        //before insert it is necessary to delete all.
        context.contentResolver.delete(GroupEntry.CONTENT_URI, null, null)
        context.contentResolver.bulkInsert(GroupEntry.CONTENT_URI, array)
    }

    fun queryAllCompetitions(context: Context): List<Group> {
        val groups = ArrayList<Group>()
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(GroupEntry.CONTENT_URI, GroupEntry.PROJECTION, null, null, null)
            while (cursor != null && cursor.moveToNext()) {
                groups.add(GroupEntry.cursorToEntity(cursor))
            }
        } finally {
            cursor?.close()
        }
        return groups
    }

    fun queryCompetitionsById(context: Context, idCompetion: String): Group? {
        val selection = GroupEntry.COLUMN_ID + "=?"
        val selectionArgs = arrayOf(idCompetion)
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(GroupEntry.CONTENT_URI, GroupEntry.PROJECTION, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToNext()) {
                return GroupEntry.cursorToEntity(cursor)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    fun queryCompetitionsBySport(context: Context, sportName: String): List<Group> {
        val groups = ArrayList<Group>()
        val selection = GroupEntry.COLUMN_DEPORTE + "=?"
        val selectionArgs = arrayOf(sportName)
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(GroupEntry.CONTENT_URI, GroupEntry.PROJECTION, selection, selectionArgs, null)
            while (cursor != null && cursor.moveToNext()) {
                groups.add(GroupEntry.cursorToEntity(cursor))
            }
        } finally {
            cursor?.close()
        }
        return groups
    }

    fun queryCompetitionsBySportAndDistrict(context: Context, sportName: String, district: String): List<Group> {
        val groups = ArrayList<Group>()
        var selection = GroupEntry.COLUMN_DEPORTE + "=?"
        selection += "and " + GroupEntry.COLUMN_DISTRITO + "=?"
        val selectionArgs = arrayOf(sportName, district)
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(GroupEntry.CONTENT_URI, GroupEntry.PROJECTION, selection, selectionArgs, null)
            while (cursor != null && cursor.moveToNext()) {
                groups.add(GroupEntry.cursorToEntity(cursor))
            }
        } finally {
            cursor?.close()
        }
        return groups
    }

    fun queryCompetitionsBySportAndDistrictAndCategory(context: Context, sportName: String, district: String, category: String): List<Group> {
        val groups = ArrayList<Group>()
        var selection = GroupEntry.COLUMN_DEPORTE + "=?"
        selection += "and " + GroupEntry.COLUMN_DISTRITO + "=?"
        selection += "and " + GroupEntry.COLUMN_CATEGORIA + "=?"
        val selectionArgs = arrayOf(sportName, district, category)
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(GroupEntry.CONTENT_URI, GroupEntry.PROJECTION, selection, selectionArgs, null)
            while (cursor != null && cursor.moveToNext()) {
                groups.add(GroupEntry.cursorToEntity(cursor))
            }
        } finally {
            cursor?.close()
        }
        return groups
    }
}

package com.adiaz.deportesmadrid.db.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.adiaz.deportesmadrid.db.entities.Group;
import com.adiaz.deportesmadrid.retrofit.groupslist.GroupRetrofitEntity;
import com.adiaz.deportesmadrid.utils.Constants;

import static com.adiaz.deportesmadrid.db.DbContract.GroupEntry;

import java.util.ArrayList;
import java.util.List;

public class GroupsDAO {


    public static void insertCompetitions(Context context, List<GroupRetrofitEntity> competitionsList) {
        List<ContentValues> contentValues = new ArrayList<>();
        for (GroupRetrofitEntity GroupRetrofitEntity : competitionsList) {
            if (GroupRetrofitEntity.getDistrito().equalsIgnoreCase(Constants.DGD)) {
                GroupRetrofitEntity.setDistrito(Constants.DISTRITO_UNICO);
            }
            ContentValues cv = GroupEntry.retrofitEntityToContentValue(GroupRetrofitEntity);
            contentValues.add(cv);
        }
        ContentValues[] array = contentValues.toArray(new ContentValues[contentValues.size()]);

        //before insert it is necessary to delete all.
        context.getContentResolver().delete(GroupEntry.CONTENT_URI, null, null);
        context.getContentResolver().bulkInsert(GroupEntry.CONTENT_URI, array);
    }

    public static List<Group> queryAllCompetitions(Context context) {
        List<Group> groups = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(GroupEntry.CONTENT_URI, GroupEntry.PROJECTION, null, null, null);
            while (cursor!=null && cursor.moveToNext()) {
                groups.add(GroupEntry.cursorToEntity(cursor));
            }
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        return groups;
    }

    public static Group queryCompetitionsById(Context context, String idCompetion) {
        String selection = GroupEntry.COLUMN_ID + "=?";
        String[] selectionArgs = new String[]{idCompetion};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(GroupEntry.CONTENT_URI, GroupEntry.PROJECTION, selection, selectionArgs, null);
            if (cursor!=null && cursor.moveToNext()) {
                return GroupEntry.cursorToEntity(cursor);
            }
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        return null;
    }

    public static List<Group> queryCompetitionsBySport(Context context, String sportName) {
        List<Group> groups = new ArrayList<>();
        String selection = GroupEntry.COLUMN_DEPORTE + "=?";
        String[] selectionArgs = new String[]{sportName};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(GroupEntry.CONTENT_URI, GroupEntry.PROJECTION, selection, selectionArgs, null);
            while (cursor!=null && cursor.moveToNext()) {
                groups.add(GroupEntry.cursorToEntity(cursor));
            }
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        return groups;
    }

    public static List<Group> queryCompetitionsBySportAndDistrict(Context context, String sportName, String district) {
        List<Group> groups = new ArrayList<>();
        String selection = GroupEntry.COLUMN_DEPORTE + "=?";
        selection += "and " + GroupEntry.COLUMN_DISTRITO + "=?";
        String[] selectionArgs = new String[]{sportName, district};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(GroupEntry.CONTENT_URI, GroupEntry.PROJECTION, selection, selectionArgs, null);
            while (cursor!=null && cursor.moveToNext()) {
                groups.add(GroupEntry.cursorToEntity(cursor));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return groups;
    }

    public static List<Group> queryCompetitionsBySportAndDistrictAndCategory(Context context, String sportName, String district, String category) {
        List<Group> groups = new ArrayList<>();
        String selection = GroupEntry.COLUMN_DEPORTE + "=?";
        selection += "and " + GroupEntry.COLUMN_DISTRITO + "=?";
        selection += "and " + GroupEntry.COLUMN_CATEGORIA + "=?";
        String[] selectionArgs = new String[]{sportName, district, category};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(GroupEntry.CONTENT_URI, GroupEntry.PROJECTION, selection, selectionArgs, null);
            while (cursor!=null && cursor.moveToNext()) {
                groups.add(GroupEntry.cursorToEntity(cursor));
            }
        } finally {
            if (cursor!=null) {
                cursor.close();
            }
        }
        return groups;
    }
}

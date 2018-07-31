package com.adiaz.ligasmadrid.db

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import com.adiaz.ligasmadrid.db.entities.Favorite
import com.adiaz.ligasmadrid.db.entities.Group
import com.adiaz.ligasmadrid.retrofit.groupslist.GroupRetrofitEntity
import com.adiaz.ligasmadrid.utils.Utils

/**
 * Created by adiaz on 22/3/18.
 */

object DbContract {
    //private static final String TAG = DbContract.class.getSimpleName();
    const val AUTHORITY = "com.adiaz.ligasmadrid"
    val BASE_CONTENT = Uri.parse("content://$AUTHORITY")!!

    const val PATH_COMPETITIONS = "competitions"
    const val PATH_FAVORITES = "favorites"
    //public static final String PATH_MATCHES = "matches";


    object GroupEntry {
        val CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_COMPETITIONS).build()!!
        //table
        const val TABLE_NAME = "GROUPS"
        //columns
        const val COLUMN_ID = "ID"
        const val COLUMN_COD_TEMPORADA = "COD_TEMPORADA"
        const val COLUMN_COD_COMPETICION = "COD_COMPETICION"
        const val COLUMN_COD_FASE = "COD_FASE"
        const val COLUMN_COD_GRUPO = "COD_GRUPO"
        const val COLUMN_NOM_TEMPORADA = "NOM_TEMPORADA"
        const val COLUMN_NOM_COMPETICION = "NOM_COMPETICION"
        const val COLUMN_NOM_FASE = "NOM_FASE"
        const val COLUMN_NOM_GRUPO = "NOM_GRUPO"
        const val COLUMN_DEPORTE = "DEPORTE"
        const val COLUMN_DISTRITO = "DISTRITO"
        const val COLUMN_CATEGORIA = "CATEGORIA"

        val PROJECTION = arrayOf(COLUMN_ID, COLUMN_COD_TEMPORADA, COLUMN_COD_COMPETICION, COLUMN_COD_FASE, COLUMN_COD_GRUPO, COLUMN_NOM_TEMPORADA,
                COLUMN_NOM_COMPETICION, COLUMN_NOM_FASE, COLUMN_NOM_GRUPO, COLUMN_DEPORTE, COLUMN_DISTRITO, COLUMN_CATEGORIA)

        const val INDEX_ID = 0
        const val INDEX_COD_TEMPORADA = 1
        const val INDEX_COD_COMPETICION = 2
        const val INDEX_COD_FASE = 3
        const val INDEX_COD_GRUPO = 4
        const val INDEX_NOM_TEMPORADA = 5
        const val INDEX_NOM_COMPETICION = 6
        const val INDEX_NOM_FASE = 7
        const val INDEX_NOM_GRUPO = 8
        const val INDEX_DEPORTE = 9
        const val INDEX_DISTRITO = 10
        const val INDEX_CATEGORIA = 11

        const val SQL_CREATE_TABLE_GROUPS =
                """CREATE TABLE $TABLE_NAME(
                    $COLUMN_ID TEXT PRIMARY KEY,
                    $COLUMN_COD_TEMPORADA INTEGER NOT NULL,
                    $COLUMN_COD_COMPETICION INTEGER NOT NULL,
                    $COLUMN_COD_FASE INTEGER NOT NULL,
                    $COLUMN_COD_GRUPO INTEGER NOT NULL,
                    $COLUMN_NOM_TEMPORADA TEXT,
                    $COLUMN_NOM_COMPETICION TEXT,
                    $COLUMN_NOM_FASE TEXT,
                    $COLUMN_NOM_GRUPO TEXT,
                    $COLUMN_DEPORTE TEXT,
                    $COLUMN_DISTRITO TEXT,
                    $COLUMN_CATEGORIA TEXT )"""

        const val SQL_DELETE_TABLE_GROUPS = "DROP TABLE IF EXISTS $TABLE_NAME"

        fun retrofitEntityToContentValue(GroupRetrofitEntity: GroupRetrofitEntity): ContentValues {
            val cv = ContentValues()
            cv.put(COLUMN_ID, GroupRetrofitEntity.id)
            cv.put(COLUMN_COD_TEMPORADA, GroupRetrofitEntity.codTemporada)
            cv.put(COLUMN_COD_COMPETICION, GroupRetrofitEntity.codCompeticion)
            cv.put(COLUMN_COD_FASE, GroupRetrofitEntity.codFase)
            cv.put(COLUMN_COD_GRUPO, GroupRetrofitEntity.codGrupo)
            cv.put(COLUMN_NOM_TEMPORADA, GroupRetrofitEntity.nombreTemporada)
            cv.put(COLUMN_NOM_COMPETICION, GroupRetrofitEntity.nombreCompeticion)
            cv.put(COLUMN_NOM_FASE, GroupRetrofitEntity.nombreFase)
            cv.put(COLUMN_NOM_GRUPO, GroupRetrofitEntity.nombreGrupo)
            cv.put(COLUMN_DEPORTE, Utils.normalizeString(GroupRetrofitEntity.deporte))
            cv.put(COLUMN_DISTRITO, Utils.normalizeString(GroupRetrofitEntity.distrito))
            cv.put(COLUMN_CATEGORIA, Utils.normalizeString(GroupRetrofitEntity.categoria))
            return cv
        }

        fun cursorToEntity(cursor: Cursor): Group {
            val group = Group()
            group.id = cursor.getString(INDEX_ID)
            group.codTemporada = cursor.getInt(INDEX_COD_COMPETICION)
            group.codFase = cursor.getInt(INDEX_COD_FASE)
            group.codGrupo = cursor.getInt(INDEX_COD_GRUPO)
            group.nomTemporada = cursor.getString(INDEX_NOM_TEMPORADA)
            group.nomCompeticion = cursor.getString(INDEX_NOM_COMPETICION)
            group.nomFase = cursor.getString(INDEX_NOM_FASE)
            group.nomGrupo = cursor.getString(INDEX_NOM_GRUPO)
            group.deporte = cursor.getString(INDEX_DEPORTE)
            group.distrito = cursor.getString(INDEX_DISTRITO)
            group.categoria = cursor.getString(INDEX_CATEGORIA)
            return group
        }
    }


    class FavoritesEntry : BaseColumns {
        companion object {
            val CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_FAVORITES).build()!!

            fun buildFavoritesUri(id: Long): Uri {
                return CONTENT_URI.buildUpon()
                        .appendPath(id.toString())
                        .build()
            }

            val TABLE_NAME = "Favorites"
            val _ID = "_id"
            val COLUMN_ID_GROUP = "id_group"
            val COLUMN_ID_TEAM = "id_team"
            val COLUMN_NAME_TEAM = "name_team"

            val PROJECTION = arrayOf(BaseColumns._ID, COLUMN_ID_GROUP, COLUMN_ID_TEAM, COLUMN_NAME_TEAM)
            val INDEX_ID = 0
            val INDEX_ID_GROUP = 1
            val INDEX_ID_TEAM = 2
            val INDEX_NAME_TEAM = 3

            val SQL_CREATE_TABLE_FAVORITES =
                    """CREATE TABLE $TABLE_NAME(
                    $_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_ID_GROUP INTEGER NOT NULL,
                    $COLUMN_ID_TEAM INTEGER,
                    $COLUMN_NAME_TEAM TEXT ) """

            val SQL_DELETE_TABLE_FAVORITES = "DROP TABLE IF EXISTS $TABLE_NAME"

            fun initEntity(cursor: Cursor): Favorite {
                return Favorite(
                        cursor.getLong(INDEX_ID),
                        cursor.getString(INDEX_ID_GROUP),
                        if (cursor.isNull(INDEX_ID_TEAM)) null else cursor.getLong(INDEX_ID_TEAM),
                        cursor.getString(INDEX_NAME_TEAM))
            }

            fun entityToContentValues(favorite: Favorite): ContentValues {
                val cv = ContentValues()
                cv.put(BaseColumns._ID, favorite.id)
                cv.put(COLUMN_ID_GROUP, favorite.idGroup)
                cv.put(COLUMN_ID_TEAM, favorite.idTeam)
                cv.put(COLUMN_NAME_TEAM, favorite.nameTeam)
                return cv
            }
        }
    }
}

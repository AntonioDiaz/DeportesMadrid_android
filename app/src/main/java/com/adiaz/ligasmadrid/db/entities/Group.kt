package com.adiaz.ligasmadrid.db.entities

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by adiaz on 22/3/18.
 */
class Group() : Parcelable {
    var id: String? = null
    var codTemporada: Int? = null
    var codCompeticion: Int? = null
    var codFase: Int? = null
    var codGrupo: Int? = null
    var nomTemporada: String? = null
    var nomCompeticion: String? = null
    var nomFase: String? = null
    var nomGrupo: String? = null
    var deporte: String? = null
    var distrito: String? = null
    var categoria: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        codTemporada = parcel.readValue(Int::class.java.classLoader) as? Int
        codCompeticion = parcel.readValue(Int::class.java.classLoader) as? Int
        codFase = parcel.readValue(Int::class.java.classLoader) as? Int
        codGrupo = parcel.readValue(Int::class.java.classLoader) as? Int
        nomTemporada = parcel.readString()
        nomCompeticion = parcel.readString()
        nomFase = parcel.readString()
        nomGrupo = parcel.readString()
        deporte = parcel.readString()
        distrito = parcel.readString()
        categoria = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeValue(codTemporada)
        parcel.writeValue(codCompeticion)
        parcel.writeValue(codFase)
        parcel.writeValue(codGrupo)
        parcel.writeString(nomTemporada)
        parcel.writeString(nomCompeticion)
        parcel.writeString(nomFase)
        parcel.writeString(nomGrupo)
        parcel.writeString(deporte)
        parcel.writeString(distrito)
        parcel.writeString(categoria)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Group> {
        override fun createFromParcel(parcel: Parcel): Group {
            return Group(parcel)
        }

        override fun newArray(size: Int): Array<Group?> {
            return arrayOfNulls(size)
        }
    }

}
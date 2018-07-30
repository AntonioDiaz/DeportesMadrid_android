package com.adiaz.deportesmadrid.db.entities

import android.os.Parcel
import android.os.Parcelable

class Favorite (val id: Long?=null, val idGroup: String, val idTeam: Long?=null, val nameTeam: String? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id!!)
        parcel.writeString(idGroup)
        parcel.writeLong(idTeam!!)
        parcel.writeString(nameTeam)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Favorite> {
        override fun createFromParcel(parcel: Parcel): Favorite {
            return Favorite(parcel)
        }

        override fun newArray(size: Int): Array<Favorite?> {
            return arrayOfNulls(size)
        }
    }

}

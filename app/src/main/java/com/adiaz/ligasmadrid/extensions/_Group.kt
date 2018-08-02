package com.adiaz.ligasmadrid.extensions

import android.content.Context
import com.adiaz.ligasmadrid.db.entities.Group
import com.adiaz.ligasmadrid.utils.Utils

fun Group.getSportNameLocalized(context: Context): String {
    return Utils.getSportNameLocalized(context, this.deporte!!)
}
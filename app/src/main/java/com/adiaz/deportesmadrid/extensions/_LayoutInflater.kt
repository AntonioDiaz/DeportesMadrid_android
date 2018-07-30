package com.adiaz.deportesmadrid.extensions

import android.content.Context
import android.view.LayoutInflater

val Context.layoutInflater get() = LayoutInflater.from(this)
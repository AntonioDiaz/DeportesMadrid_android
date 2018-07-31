package com.adiaz.ligasmadrid.extensions

import android.content.Context
import android.view.LayoutInflater

val Context.layoutInflater get() = LayoutInflater.from(this)
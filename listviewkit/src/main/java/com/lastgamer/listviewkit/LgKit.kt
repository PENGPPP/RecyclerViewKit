package com.lastgamer.listviewkit

import android.content.Context

object LgKit {

    private lateinit var mContext: Context

    fun setup(context: Context) {
        mContext = context.applicationContext
    }

    fun context(): Context = mContext
}
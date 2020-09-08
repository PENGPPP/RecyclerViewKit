package com.lastgamer.listviewkit

import android.content.Context.WINDOW_SERVICE
import android.util.DisplayMetrics
import android.view.WindowManager


object DisplayUtils {

    var screenWidth = 0
        private set
        public get() {
            if (field == 0) get()
            log("$field")
            return field
        }
    var screenHeight = 0
        public get() {
            if (field == 0) get()
            log("$field")
            return field
        }

    fun get(){

        val outMetrics = DisplayMetrics()
        (LgKit.context().getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(outMetrics)
        screenWidth = outMetrics.widthPixels
        screenHeight = outMetrics.heightPixels
    }

}
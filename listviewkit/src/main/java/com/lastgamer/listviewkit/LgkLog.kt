package com.lastgamer.listviewkit

import android.util.Log

fun log(tag: String, msg: String, isError: Boolean = false) {
    if (isError) {
        Log.i(tag , msg)
    } else {
        Log.e(tag, msg)
    }
}

fun log(msg: String) {
    log(getInvokedMethodInfo(), msg)
}

fun elog(msg: String) {
    log(getInvokedMethodInfo(), msg, true)
}

fun <T> logIfNull(obj: T?, tag: String): Boolean {
    if (obj == null) {
        log("check null -> ${getInvokedMethodInfo()}", tag, true)
        return true
    }
    return false
}

inline fun <T, R> T?.letOrLog(nullMsg: String, block: (T) -> R): R? {
    return if (this == null){
        log(nullMsg)
        null
    }else {
        block(this)
    }
}

fun getInvokedMethodInfo(): String{
    Throwable().stackTrace.getOrNull(2)?.let {
        return "${it.className}::${it.methodName} "
    }
    return "unknown"
}
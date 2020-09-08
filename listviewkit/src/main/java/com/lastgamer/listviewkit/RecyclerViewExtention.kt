package com.lastgamer.listviewkit

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.getLastVisiblePosition(): Int {
    if (this.layoutManager !is LinearLayoutManager) {
        log("layout manager must be ${LinearLayoutManager::class.java.simpleName}")
        return -1
    }
    return (this.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
}

fun RecyclerView.getFirstVisiblePosition(): Int {
    if (this.layoutManager !is LinearLayoutManager) {
        log("layout manager must be ${LinearLayoutManager::class.java.simpleName}")
        return -1
    }
    return (this.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
}

fun RecyclerView.getLastCompletelyVisiblePosition(): Int {
    if (this.layoutManager !is LinearLayoutManager) {
        log("layout manager must be ${LinearLayoutManager::class.java.simpleName}")
        return -1
    }
    return (this.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
}

fun RecyclerView.getFirstCompletelyVisiblePosition(): Int {
    if (this.layoutManager !is LinearLayoutManager) {
        log("layout manager must be ${LinearLayoutManager::class.java.simpleName}")
        return -1
    }
    return (this.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
}

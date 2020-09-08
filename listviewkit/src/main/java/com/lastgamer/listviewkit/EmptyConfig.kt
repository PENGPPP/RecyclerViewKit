package com.lastgamer.listviewkit

import android.view.ViewGroup

data class EmptyConfig(val refreshBtnTextResId: Int? = 0,
                       val tipsTextResId: Int? = 0,
                       val showTips: Boolean = (tipsTextResId != 0 && tipsTextResId != null),
                       val showRefresh: Boolean = (refreshBtnTextResId != 0 && refreshBtnTextResId != null)) {

    var height: Int = ViewGroup.LayoutParams.MATCH_PARENT

    fun haveTips(): Boolean = showTips && tipsTextResId != null && tipsTextResId != 0
    fun haveRefresh(): Boolean = showRefresh && refreshBtnTextResId != null && refreshBtnTextResId != 0

    fun getTips(): String {
        return if (haveTips()) {
            LgKit.context().getString(tipsTextResId ?: return "")
        } else {
            ""
        }
    }

    fun getRefreshBtnText(): String {
        return if (haveRefresh()) {
            LgKit.context().getString(refreshBtnTextResId ?: return "")
        } else {
            ""
        }
    }
}

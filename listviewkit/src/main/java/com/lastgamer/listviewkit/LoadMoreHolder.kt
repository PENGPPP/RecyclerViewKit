package com.lastgamer.listviewkit

import com.lastgamer.listviewkit.databinding.LgkHolderFooterBinding

class LoadMoreHolder(val binding: LgkHolderFooterBinding) : BaseRecyclerViewHolder<LoadMoreSign>(binding.root) {
    companion object : ViewHolderFactory<LgkHolderFooterBinding, LoadMoreHolder> {
        override fun getViewHolderClass(): Class<*> = LoadMoreHolder::class.java
        override fun getLayoutId(): Int = R.layout.lgk_holder_footer
    }
}

class LoadMoreSign
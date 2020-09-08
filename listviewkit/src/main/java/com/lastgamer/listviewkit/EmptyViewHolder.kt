package com.lastgamer.listviewkit

import com.lastgamer.listviewkit.databinding.LgkViewEmptyBinding

class EmptyViewHolder(binding: LgkViewEmptyBinding) : BaseRecyclerViewHolder<Any>(binding.root) {
    companion object : ViewHolderFactory<LgkViewEmptyBinding, EmptyViewHolder> {
        override fun getViewHolderClass(): Class<*> = EmptyViewHolder::class.java
        override fun getLayoutId(): Int = R.layout.lgk_view_empty
    }
}
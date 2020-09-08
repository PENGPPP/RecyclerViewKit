package com.lastgamer.listviewkit

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lastgamer.listviewkit.databinding.LgkHolderEmptyTipsBinding

abstract class PagerAdapter : BaseRecyclerAdapter() {

    var emptyConfig: EmptyConfig? = null
    private var recyclerView: RecyclerView? = null

    fun checkEmpty() {
        val recyclerView = this.recyclerView ?: return
        var optionItemCount = 0
        if (hasFooter()) {
            optionItemCount += 1
        }
        val hasHeader = hasHeader()
        if (hasHeader) {
            optionItemCount += 1
        }

        if (itemList.size - optionItemCount > 0) {
            return
        }

        registerDefault(EmptyConfig::class.java, EmptyHolder)
        var emptyHolderHeight = ViewGroup.LayoutParams.MATCH_PARENT

        val headerChildView = recyclerView.getChildAt(0)
        if (hasHeader && headerChildView != null) {
            val headerHeight = headerChildView.apply {
                measure(View.MeasureSpec.makeMeasureSpec(DisplayUtils.screenWidth,
                        View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(DisplayUtils.screenHeight, View.MeasureSpec.AT_MOST))
            }.measuredHeight

            emptyHolderHeight = recyclerView.measuredHeight - headerHeight
        }

        if (emptyConfig == null) {
            emptyConfig = EmptyConfig()
        }

        emptyConfig?.apply { height = emptyHolderHeight }
                ?.let {
                    attachItem(it)
                }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    class EmptyHolder(val binding: LgkHolderEmptyTipsBinding) : BaseRecyclerViewHolder<EmptyConfig>(binding.root) {
        companion object : ViewHolderFactory<LgkHolderEmptyTipsBinding, EmptyHolder> {
            override fun getLayoutId(): Int = R.layout.lgk_holder_empty_tips
            override fun getViewHolderClass(): Class<*> = EmptyHolder::class.java
        }

        override fun configView(model: EmptyConfig) {
            binding.root.layoutParams.height = model.height
            binding.emptyConfig = model
        }
    }
}
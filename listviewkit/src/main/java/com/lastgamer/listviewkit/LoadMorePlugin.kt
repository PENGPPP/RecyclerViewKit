package com.lastgamer.listviewkit

import androidx.recyclerview.widget.RecyclerView

class LoadMorePlugin(val pagerOffset: Int = 0,
                     val recyclerView: RecyclerView,
                     val callback: () -> Unit) {

    private var loadMoreListener: LoadMoreListener? = null
    private val loadMoreSign by lazy { LoadMoreSign() }
    private var loadMoreStart = false

    fun enableLoadMore() {
        checkAdapterType()
        (recyclerView.adapter as BaseRecyclerAdapter).apply {
            registerFooter(LoadMoreSign::class.java, LoadMoreHolder.Companion)
            if (!hasFooter()) {
                attachItem(loadMoreSign)
            }
        }
        if (loadMoreListener == null) {
            loadMoreListener = LoadMoreListener(this, LoadMoreSign::class.java, callback)
            recyclerView.addOnScrollListener(loadMoreListener!!)
        }
    }

    fun disableLoadMore() {
        loadMoreStart = false
        checkAdapterType()
        (recyclerView.adapter as? BaseRecyclerAdapter)?.dispatchItem(loadMoreSign)
    }

    fun loadMoreEnd() {
        loadMoreStart = false
    }

    private fun checkAdapterType() {
        if (recyclerView.adapter !is BaseRecyclerAdapter) {
            throw RuntimeException("adapter must be ${BaseRecyclerAdapter::class.java.simpleName}'s sub class")
        }
    }

    class LoadMoreListener(private val loadMorePlugin: LoadMorePlugin,
                           private val loadMoreSign: Class<*>,
                           private val callback: () -> Unit) : RecyclerView.OnScrollListener() {

        private var adapter: BaseRecyclerAdapter? = null

        init {
            loadMorePlugin.checkAdapterType()
            adapter = loadMorePlugin.recyclerView.adapter as BaseRecyclerAdapter
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (loadMorePlugin.loadMoreStart) return
            adapter?.let {
                val lastPosition = recyclerView.getLastVisiblePosition()
                if (loadMorePlugin.pagerOffset != 0) {
                    log("http: ${lastPosition + loadMorePlugin.pagerOffset}  ${it.itemCount - 1}")
                    if (lastPosition + loadMorePlugin.pagerOffset >= it.itemCount - 1) {
                        callback.invoke()
                    }
                } else {
                    if (newState == 0) {
                        if (lastPosition == it.itemCount - 1 && loadMoreSign.isInstance(it.getData(lastPosition))) {
                            loadMorePlugin.loadMoreStart = true
                            callback.invoke()
                        }
                    }
                }
            }
        }
    }
}
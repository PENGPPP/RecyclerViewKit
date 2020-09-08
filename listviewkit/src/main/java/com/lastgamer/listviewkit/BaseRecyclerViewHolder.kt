package com.lastgamer.listviewkit

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var model: T? = null
        set(value) {
            field = value
            value?.let {
                configView(value)
            }
        }

    @Suppress("UNCHECKED_CAST")
    open fun configModel(model: Any) {
        this.model = model as T
    }

    open fun configView(model: T) {

    }

    fun bindViewClickEvent(viewHolderConfig: ViewHolderConfig) {
        itemView.setOnClickListener {
            viewHolderConfig.itemClickListeners.forEach { click ->
                click.invoke(model, it)
            }
            viewHolderConfig.viewHolderClickListeners.forEach { click ->
                click.invoke(this)
            }
        }

        itemView.setOnLongClickListener {
            viewHolderConfig.itemLongClickListener?.invoke(model) == true
        }

        viewHolderConfig.subviewOnClickListenerMap.forEach { entry ->
            val subView: View? = itemView.findViewById(entry.key)
            subView?.setOnClickListener {
                entry.value.invoke(model, it)
            }
        }

        viewHolderConfig.subviewViewFinder.forEach { entry ->
            val subView: View? = itemView.findViewById(entry.key)
            entry.value.invoke(model, subView)
        }

    }

    class ViewHolderConfig {
        var itemClickListeners = mutableListOf<((model: Any?, view: View?) -> Unit)>()
        var viewHolderClickListeners = mutableListOf<((viewHolder: BaseRecyclerViewHolder<*>) -> Unit)>()

        var itemLongClickListener: ((model: Any?) -> Boolean)? = null
        val subviewOnClickListenerMap = mutableMapOf<Int, (model: Any?, view: View) -> Unit>()
        val subviewViewFinder = mutableMapOf<Int, (model: Any?, view: View?) -> Unit>()
    }

}
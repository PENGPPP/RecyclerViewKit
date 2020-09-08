package com.lastgamer.listviewkit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import java.lang.IllegalArgumentException

interface ViewHolderFactory<R : ViewDataBinding, out T : BaseRecyclerViewHolder<*>> {

    fun getViewType(): Int {
        return this.hashCode()
    }

    @Suppress("UNCHECKED_CAST")
    fun creator(parentView: ViewGroup): T {
        val itemView = LayoutInflater.from(parentView.context).inflate(getLayoutId(), parentView, false)
        val binding = try {
            DataBindingUtil.bind<R>(itemView)
        } catch (e: IllegalArgumentException){
            null
        }
        val result = if (binding == null){
            createByView(itemView)
        } else {
            createByBinding(binding)
        }
        return result
    }

    private fun createByBinding(binding: R): T {
        val constructor = getViewHolderClass()
                .getDeclaredConstructor(binding::class.java.superclass)
                .apply {
                    isAccessible = true
                }
        return constructor.newInstance(binding) as T
    }

    private fun createByView(itemView: View): T {
        val constructor = getViewHolderClass()
                .getDeclaredConstructor(View::class.java)
                .apply {
                    isAccessible = true
                }
        return constructor.newInstance(itemView) as T
    }

    fun getLayoutId(): Int
    fun getViewHolderClass(): Class<*>

}
package com.lastgamer.listviewkit

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalArgumentException
import kotlin.math.max

abstract class BaseRecyclerAdapter : RecyclerView.Adapter<BaseRecyclerViewHolder<*>>() {

    val viewHolderConfig = BaseRecyclerViewHolder.ViewHolderConfig()
    protected var itemList = mutableListOf<Any>()
    private val defaultType = this.hashCode()
    private val viewHolderFactoryMap = mutableMapOf<Int, ViewHolderFactory<*, BaseRecyclerViewHolder<*>>>()
    private val footerRegister = mutableMapOf<Class<*>, ViewHolderFactory<*, *>>()
    private val headerRegister = mutableMapOf<Class<*>, ViewHolderFactory<*, *>>()
    private val defaultRegister = mutableMapOf<Class<*>, ViewHolderFactory<*, *>>()

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<*>, position: Int) {
        holder.configModel(itemList[position])
        holder.bindViewClickEvent(this@BaseRecyclerAdapter.viewHolderConfig)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<*> {
        return viewHolderFactoryMap[viewType]?.creator(parent)
                ?: throw UnsupportedOperationException("not found viewType.")
    }

    open fun onCreateViewHolderByType(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<*>? {
        return null
    }

    override fun getItemViewType(position: Int): Int {
        val viewHolderFactory = getViewHolderFactoryByPosition(position)
                ?: footerRegister[itemList[position]::class.java]
                ?: headerRegister[itemList[position]::class.java]
                ?: defaultRegister[itemList[position]::class.java]
                ?: getViewHolderFactoryByModel(itemList[position])
                ?: EmptyViewHolder

        return viewHolderFactoryMap.getOrPut(viewHolderFactory.getViewType(), { viewHolderFactory }).getViewType()
    }


    open fun getViewHolderFactoryByPosition(position: Int): ViewHolderFactory<*, BaseRecyclerViewHolder<*>>? {
        return null
    }

    fun registerFooter(dataClass: Class<*>, holderFactory: ViewHolderFactory<*, *>) {
        footerRegister.clear()
        footerRegister[dataClass] = holderFactory
    }

    fun registerHeader(dataClass: Class<*>, holderFactory: ViewHolderFactory<*, *>) {
        headerRegister.clear()
        headerRegister[dataClass] = holderFactory
    }

    fun registerDefault(dataClass: Class<*>, holderFactory: ViewHolderFactory<*, *>) {
        defaultRegister[dataClass] = holderFactory
    }

    abstract fun getViewHolderFactoryByModel(model: Any): ViewHolderFactory<*, BaseRecyclerViewHolder<*>>?

    open fun attachItem(model: Any, attached: Boolean = true) {
        val insertPosition = when {
            footerRegister[model::class.java] != null -> {
                removeFooter()
                itemList.size
            }
            headerRegister[model::class.java] != null -> {
                removeHeader()
                0
            }
            else -> {
                getInsertPosition()
            }
        }

        itemList.add(insertPosition, model)
        if (attached) {
            notifyItemInserted(insertPosition)
        }
    }

    fun dispatchItem(model: Any, attached: Boolean = true) {
        if (model is Iterable<*>) {
            throw IllegalArgumentException("dispatchItem method can not receive a iterable")
        }
        if (itemList.size <= 0) {
            log("dispatch failed")
            return
        }

        val position = itemList.indexOf(model)

        if (position < 0 || position >= itemList.size) {
            log("dispatch failed")
            return
        }

        itemList.removeAt(position)
        if (attached) {
            notifyItemRemoved(position)
        }
    }

    open fun attachItems(models: List<Any>, attached: Boolean = true) {
        val insertPosition = getInsertPosition()
        itemList.addAll(insertPosition, models)
        if (attached) {
            notifyItemRangeInserted(insertPosition, models.size)
        }
    }

    open fun attachItems(models: List<Any>, position: Int, attached: Boolean = true) {
        val insertPosition = getInsertPosition(position)
        itemList.addAll(insertPosition, models)
        if (attached) {
            notifyItemRangeInserted(insertPosition, models.size)
        }
    }

    open fun updateItem(item: Any) {
        val index = itemList.indexOf(item)
        if (index >= 0 && index < itemList.size) {
            notifyItemChanged(index)
        }
    }

    private fun getInsertPosition(): Int {
        val hasHeader = hasHeader()
        val hasFooter = hasFooter()
        return if ((hasHeader && !hasFooter) || (!hasHeader && !hasFooter)) {
            itemList.size
        } else if (!hasHeader && hasFooter) {
            if (itemList.size == 1) {
                0
            } else {
                itemList.size - 1
            }
        } else if (hasHeader && hasFooter) {
            itemList.size - 1
        } else {
            itemList.size
        }
    }

    private fun getInsertPosition(position: Int): Int {
        val hasHeader = hasHeader()
        val hasFooter = hasFooter()
        var realPosition = position
        if (realPosition == 0 && hasHeader) {
            realPosition += 1
        }

        if (realPosition == itemList.size && hasFooter) {
            realPosition = itemList.size - 1
        }

        return realPosition
    }

    fun clearAllData() {
        var headerItem: Any? = null
        if (hasHeader()) {
            headerItem = itemList.firstOrNull()
        }

        var footerItem: Any? = null
        if (hasFooter()) {
            footerItem = itemList.lastOrNull()
        }

        val size = itemList.size
        itemList.clear()
        notifyItemRangeRemoved(0, size)
        headerItem?.let { itemList.add(headerItem) }
        footerItem?.let { itemList.add(footerItem) }
        notifyDataSetChanged()
    }

    fun removeHeader(): Boolean {
        val firstItem = itemList.firstOrNull()
        if (firstItem == null) {
            return false
        } else {
            if (headerRegister.containsKey(firstItem::class.java)) {
                itemList.remove(firstItem)
                notifyItemRemoved(0)
                return true
            }
            return false
        }
    }

    fun removeFooter(): Boolean {
        val lastItem = itemList.lastOrNull()
        if (lastItem == null) {
            return false
        } else {
            if (footerRegister.containsKey(lastItem::class.java)) {
                itemList.remove(lastItem)
                notifyItemRemoved(itemList.size - 1)
                return true
            }
            return false
        }
    }

    fun hasHeader(): Boolean {
        val firstItem = itemList.firstOrNull()
        return if (firstItem == null) {
            false
        } else {
            headerRegister.containsKey(firstItem::class.java)
        }
    }

    fun hasFooter(): Boolean {
        val lastItem = itemList.lastOrNull()
        return if (lastItem == null) {
            false
        } else {
            footerRegister.containsKey(lastItem::class.java)
        }
    }

    fun dataCount(): Int {
        var optionItemCount = 0
        if (hasFooter()) {
            optionItemCount += 1
        }
        val hasHeader = hasHeader()
        if (hasHeader) {
            optionItemCount += 1
        }

        return max(itemList.size - optionItemCount, 0)
    }

    fun <T> getAllData(): List<T?> {
        return itemList.map { it as? T }
    }

    fun <T> getAllData(clazz: Class<T>): List<T> {
        return itemList.asSequence()
                .filter { clazz.isInstance(it) }
                .map { clazz.cast(it)!! }.toList()
    }

    open fun getData(position: Int): Any? {
        return itemList.getOrNull(position)
    }

    fun bindDataSource(list: MutableList<Any>) {
        itemList = list
    }

}
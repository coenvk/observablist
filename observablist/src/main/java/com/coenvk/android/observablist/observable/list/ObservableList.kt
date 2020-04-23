package com.coenvk.android.observablist.observable.list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import com.coenvk.android.observablist.diff.Diffable
import com.coenvk.android.observablist.ktx.dispatchUpdatesTo
import com.coenvk.android.observablist.observable.DataObservable
import com.coenvk.android.observablist.observer.DataObserver
import java.util.*
import java.util.function.Predicate
import java.util.function.UnaryOperator

class ObservableList<T>(private val items: MutableList<T> = mutableListOf()) :
    MutableList<T> by items {

    private val diffCallback: DiffCallback<T> = DiffCallback(emptyList(), emptyList())
    private val observable: DataObservable = DataObservable()

    constructor(items: Array<out T>) : this(items.toMutableList())
    constructor(items: Iterable<T>) : this(items.toMutableList())

    fun registerObserver(observer: DataObserver) {
        observable.registerObserver(observer)
    }

    fun unregisterObserver(observer: DataObserver) {
        observable.unregisterObserver(observer)
    }

    fun move(fromIndex: Int, toIndex: Int) {
        val item = items.removeAt(fromIndex)
        items.add(toIndex, item)
        observable.notifyItemRangeMoved(fromIndex, toIndex, 1)
    }

    fun swap(i: Int, j: Int) {
        Collections.swap(items, i, j)
        observable.notifyItemRangeChanged(i, 1)
        observable.notifyItemRangeChanged(j, 1)
    }

    override fun add(element: T): Boolean {
        val ret = items.add(element)
        if (ret) observable.notifyItemRangeInserted(size - 1, 1)
        return ret
    }

    override fun add(index: Int, element: T) {
        items.add(index, element)
        observable.notifyItemRangeInserted(index, 1)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val startRange = size
        val ret = items.addAll(elements)
        if (ret) observable.notifyItemRangeInserted(startRange, elements.size)
        return ret
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val ret = items.addAll(index, elements)
        if (ret) observable.notifyItemRangeInserted(index, elements.size)
        return ret
    }

    override fun remove(element: T): Boolean {
        val index = indexOf(element)
        val ret = if (index < 0) false else {
            items.removeAt(index)
            true
        }
        if (ret) observable.notifyItemRangeRemoved(index, 1)
        return ret
    }

    override fun removeAt(index: Int): T {
        val item = items.removeAt(index)
        observable.notifyItemRangeRemoved(index, 1)
        return item
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        if (elements.size == 1) return remove(elements.single())
        val oldItems = items.toMutableList()
        val ret = items.removeAll(elements)
        if (ret) {
            diffCallback.oldItems = oldItems
            diffCallback.newItems = items
            DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(observable)
        }
        return ret
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun replaceAll(operator: UnaryOperator<T>) {
        super.replaceAll(operator)
        observable.notifyChanged()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun removeIf(filter: Predicate<in T>): Boolean {
        var removed = false
        val each: MutableListIterator<T> = listIterator()
        while (each.hasNext()) {
            val i = each.nextIndex()
            if (filter.test(each.next())) {
                each.remove()
                removed = true
                observable.notifyItemRangeRemoved(i, 1)
            }
        }
        return removed
    }

    override fun subList(fromIndex: Int, toIndex: Int): ObservableList<T> {
        return ObservableList(items.subList(fromIndex, toIndex))
    }

    override fun set(index: Int, element: T): T {
        val item = items.set(index, element)
        observable.notifyItemRangeChanged(index, 1)
        return item
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val oldItems = items.toMutableList()
        val ret = items.retainAll(elements)
        if (ret) {
            diffCallback.oldItems = oldItems
            diffCallback.newItems = items
            DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(observable)
        }
        return ret
    }

    override fun clear() {
        items.clear()
        observable.notifyChanged()
    }

    private class DiffCallback<T>(var oldItems: List<T>, var newItems: List<T>) :
        DiffUtil.Callback() {

        @Suppress("UNCHECKED_CAST")
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]
            return (oldItem as? Diffable<T>)?.areItemsTheSame(newItem)
                    ?: (newItem as? Diffable<T>)?.areItemsTheSame(oldItem)
                    ?: oldItem == newItem
        }

        override fun getOldListSize(): Int {
            return oldItems.size
        }

        override fun getNewListSize(): Int {
            return newItems.size
        }

        @Suppress("UNCHECKED_CAST")
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]
            return (oldItem as? Diffable<T>)?.areContentsTheSame(newItem)
                ?: (newItem as? Diffable<T>)?.areContentsTheSame(oldItem)
                ?: false
        }

    }

}
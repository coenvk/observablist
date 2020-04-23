package com.coenvk.android.observablist.observer

abstract class DataObserver : Observer() {

    open fun onItemRangeChanged(positionStart: Int, itemCount: Int) = Unit
    open fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) =
        onItemRangeChanged(positionStart, itemCount)

    open fun onItemRangeInserted(positionStart: Int, itemCount: Int) = Unit
    open fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = Unit
    open fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) = Unit

}
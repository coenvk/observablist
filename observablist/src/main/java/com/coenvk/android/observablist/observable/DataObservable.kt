package com.coenvk.android.observablist.observable

import com.coenvk.android.observablist.observer.DataObserver

open class DataObservable : Observable<DataObserver>() {

    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) =
        notifyItemRangeChanged(positionStart, itemCount, null)

    fun notifyItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        synchronized(mObservers) {
            for (i in mObservers.indices.reversed()) {
                mObservers[i].onItemRangeChanged(positionStart, itemCount, payload)
            }
        }
    }

    fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
        synchronized(mObservers) {
            for (i in mObservers.indices.reversed()) {
                mObservers[i].onItemRangeInserted(positionStart, itemCount)
            }
        }
    }

    fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
        synchronized(mObservers) {
            for (i in mObservers.indices.reversed()) {
                mObservers[i].onItemRangeRemoved(positionStart, itemCount)
            }
        }
    }

    fun notifyItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        synchronized(mObservers) {
            for (i in mObservers.indices.reversed()) {
                mObservers[i].onItemRangeMoved(fromPosition, toPosition, itemCount)
            }
        }
    }

}
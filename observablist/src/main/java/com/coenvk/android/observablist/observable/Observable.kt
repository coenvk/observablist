package com.coenvk.android.observablist.observable

import android.database.Observable
import com.coenvk.android.observablist.observer.Observer
import java.io.Serializable

abstract class Observable<T : Observer> :
    Cloneable, Serializable,
    Observable<T>() {

    final override fun unregisterObserver(observer: T) = super.unregisterObserver(observer)
    final override fun registerObserver(observer: T) = super.registerObserver(observer)
    final override fun unregisterAll() = super.unregisterAll()

    fun hasObservers(): Boolean = mObservers.isNotEmpty()
    fun notifyChanged() {
        synchronized(mObservers) {
            mObservers.reversed().forEach { it.onChanged() }
        }
    }

}
package com.coenvk.android.observablist.ktx

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.coenvk.android.observablist.observable.DataObservable

internal fun DiffUtil.DiffResult.dispatchUpdatesTo(observable: DataObservable) {
    dispatchUpdatesTo(UpdateCallback(observable))
}

private class UpdateCallback(private val observable: DataObservable) : ListUpdateCallback {

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        observable.notifyItemRangeChanged(position, count, payload)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        observable.notifyItemRangeMoved(fromPosition, toPosition, 1)
    }

    override fun onInserted(position: Int, count: Int) {
        observable.notifyItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        observable.notifyItemRangeRemoved(position, count)
    }

}
package com.coenvk.android.observablist.diff

interface Diffable<in E> {

    fun areItemsTheSame(other: E): Boolean

    fun areContentsTheSame(other: E): Boolean

}
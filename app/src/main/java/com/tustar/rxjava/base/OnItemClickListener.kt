package com.tustar.rxjava.base

interface OnItemClickListener<T> {
    fun onItemClick(item: T)
}

interface OnLongItemClickListener<T> {
    fun onLongItemClick(item: T)
}
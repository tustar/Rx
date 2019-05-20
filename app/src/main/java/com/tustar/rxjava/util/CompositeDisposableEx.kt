package com.tustar.rxjava.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

operator fun CompositeDisposable.plus(disposable: Disposable): CompositeDisposable {
    add(disposable)
    return this
}

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}
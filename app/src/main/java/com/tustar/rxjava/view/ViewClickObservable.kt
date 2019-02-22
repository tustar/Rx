@file:JvmName("RxView")
@file:JvmMultifileClass

package com.tustar.view

import android.view.View
import android.view.View.OnClickListener
import androidx.annotation.CheckResult
import com.tustar.internal.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import java.util.concurrent.TimeUnit


@CheckResult
fun View.clicks(): Observable<Unit> {
    return ViewClickObservable(this)
}

@CheckResult
fun View.tap(): Observable<Unit> {
    return ViewClickObservable(this)
            .throttleFirst(500,
                    TimeUnit.MILLISECONDS)
}

private class ViewClickObservable(
        private val view: View
) : Observable<Unit>() {

    override fun subscribeActual(observer: Observer<in Unit>) {
        if (!checkMainThread(observer)) {
            return
        }

        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.setOnClickListener(listener)
    }

    private class Listener(
            private val view: View,
            private val observer: Observer<in Unit>
    ) : MainThreadDisposable(), OnClickListener {

        override fun onClick(v: View) {
            if (!isDisposed) {
                observer.onNext(Unit)
            }
        }

        override fun onDispose() {
            view.setOnClickListener(null)
        }
    }
}


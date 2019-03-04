package com.tustar.action

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class RxBus private constructor() {

    private val mBus: Subject<Any> = PublishSubject.create<Any>().toSerialized()

    fun post(obj: Any) {
        mBus.onNext(obj)
    }

    fun <T> toObservable(clazz: Class<T>): Observable<T> {
        return mBus.ofType(clazz)
    }

    fun toObservable(): Observable<Any> {
        return mBus
    }

    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }

    private object Holder {
        internal val BUS = RxBus()
    }

    companion object {

        fun get(): RxBus {
            return Holder.BUS
        }
    }
}

package com.tustar.rxjava.util.scheduler

import io.reactivex.*
import org.reactivestreams.Publisher

abstract class BaseScheduler<T> constructor(
        private val subscribeOnScheduler: Scheduler,
        private val observeOnScheduler: Scheduler
) : ObservableTransformer<T, T>, FlowableTransformer<T, T>,
        SingleTransformer<T, T>, MaybeTransformer<T, T>,
        CompletableTransformer {

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
    }

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
    }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {
        return upstream
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
    }

    override fun apply(upstream: Completable): CompletableSource {
        return upstream
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
    }
}
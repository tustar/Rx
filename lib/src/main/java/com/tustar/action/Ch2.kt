package com.tustar.action

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


fun main(args: Array<String>) {
    ch2_2_4_maybe_4()
}

private fun ch2_2_4_maybe() {
    println("ch2_2_4_maybe")
    Maybe.create<String> {
        it.onSuccess("test")
    }.subscribe{
        println("onSuccess :: $it")
    }
}

private fun ch2_2_4_maybe_2() {
    println("ch2_2_4_maybe_2")
    Maybe.create<String> {
        it.onSuccess("testA")
        it.onSuccess("testB")
    }.subscribe{
        println("onSuccess :: $it")
    }
}

private fun ch2_2_4_maybe_3() {
    println("ch2_2_4_maybe_3")
    Maybe.create<String> {
        it.onComplete()
        it.onSuccess("test")
    }.subscribe{
        println("onSuccess :: $it")
    }
}

private fun ch2_2_4_maybe_4() {
    println("ch2_2_4_maybe_4")
    Maybe.create<String> {
        it.onComplete()
        it.onSuccess("test")
    }.subscribe({
        println("onSuccess :: $it")
    }, {
        println("onError :: Maybe")
    }, {
        println("onComplete :: Maybe")
    })
}

private fun ch2_2_4_completable_and_then() {
    println("ch2_2_4_completable_and_then")
    Completable.create {
        try {
            TimeUnit.SECONDS.sleep(1)
            it.onComplete()
        } catch (e: InterruptedException) {
            it.onError(e)
        }
    }
            .andThen(Observable.range(1, 10))
            .subscribe {
                println("onNext :: $it")
            }
}

private fun ch2_2_4_completable() {
    println("ch2_2_4_completable")
    Completable.fromAction {
        println("Hello World")
    }.subscribe()
}

private fun ch2_2_4_single() {
    println("ch2_2_4_single")
    Single.create<String> { it.onSuccess("test") }
            .subscribe(
                    { println("onSuccess :: $it") },
                    { println("onError ::") })
    Single.create<String> { it.onSuccess("test") }
            .subscribe { t1, t2 ->
                run {
                    println("$t1")
                }
            }
}

private fun ch2_2_2_share() {
    println("ch2_2_2_share")
    val subscriber1 = Consumer<Long> { println("subscriber1 :: $it") }
    val subscriber2 = Consumer<Long> { println("subscriber2 :: $it") }
    val subscriber3 = Consumer<Long> { println("subscriber3 :: $it") }
    val observable = Observable.create<Long> {
        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                .take(Int.MAX_VALUE.toLong())
                .subscribe(it::onNext)
    }.observeOn(Schedulers.newThread()).share()

    var disposable1 = observable.subscribe(subscriber1)
    var disposable2 = observable.subscribe(subscriber2)
    observable.subscribe(subscriber3)

    try {
        Thread.sleep(30L)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }

    disposable1.dispose()
    disposable2.dispose()

    println("重新开始数据流")

    disposable1 = observable.subscribe(subscriber1)
    disposable2 = observable.subscribe(subscriber2)

    try {
        Thread.sleep(20L)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}

private fun ch2_2_2_refCount_part() {
    println("ch2_2_2_refCount_part")
    val subscriber1 = Consumer<Long> { println("subscriber1 :: $it") }
    val subscriber2 = Consumer<Long> { println("subscriber2 :: $it") }
    val subscriber3 = Consumer<Long> { println("subscriber3 :: $it") }
    val connectableObservable = Observable.create<Long> {
        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                .take(Int.MAX_VALUE.toLong())
                .subscribe(it::onNext)
    }.observeOn(Schedulers.newThread()).publish()
    connectableObservable.connect()

    val observable = connectableObservable.refCount()

    var disposable1 = observable.subscribe(subscriber1)
    var disposable2 = observable.subscribe(subscriber2)
    observable.subscribe(subscriber3)

    try {
        Thread.sleep(30L)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }

    disposable1.dispose()
    disposable2.dispose()

    println("重新开始数据流")

    disposable1 = observable.subscribe(subscriber1)
    disposable2 = observable.subscribe(subscriber2)

    try {
        Thread.sleep(20L)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}

private fun ch2_2_2_refCount() {
    println("ch2_2_2_refCount")
    val subscriber1 = Consumer<Long> { println("subscriber1 :: $it") }
    val subscriber2 = Consumer<Long> { println("subscriber2 :: $it") }
    val connectableObservable = Observable.create<Long> {
        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                .take(Int.MAX_VALUE.toLong())
                .subscribe(it::onNext)
    }.observeOn(Schedulers.newThread()).publish()
    connectableObservable.connect()

    val observable = connectableObservable.refCount()

    var disposable1 = observable.subscribe(subscriber1)
    var disposable2 = observable.subscribe(subscriber2)

    try {
        Thread.sleep(20L)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }

    disposable1.dispose()
    disposable2.dispose()

    println("重新开始数据流")

    disposable1 = observable.subscribe(subscriber1)
    disposable2 = observable.subscribe(subscriber2)

    try {
        Thread.sleep(20L)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}

private fun ch2_2_2_subject() {
    println("ch2_2_2_subject")
    val subscriber1 = Consumer<Long> { println("subscriber1 :: $it") }
    val subscriber2 = Consumer<Long> { println("subscriber2 :: $it") }
    val subscriber3 = Consumer<Long> { println("subscriber3 :: $it") }
    val observable = Observable.create<Long> {
        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                .take(Int.MAX_VALUE.toLong())
                .subscribe(it::onNext)
    }.observeOn(Schedulers.newThread())

    val subject = PublishSubject.create<Long>()
    observable.subscribe(subject)

    subject.subscribe(subscriber1)
    subject.subscribe(subscriber2)

    try {
        Thread.sleep(20L)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }

    subject.subscribe(subscriber3)

    try {
        Thread.sleep(100L)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}

private fun ch2_2_2_cold_to_hot() {
    println("ch2_2_2_cold_to_hot")
    val subscriber1 = Consumer<Long> { println("subscriber1 :: $it") }
    val subscriber2 = Consumer<Long> { println("subscriber2 :: $it") }
    val subscriber3 = Consumer<Long> { println("subscriber3 :: $it") }
    val observable = Observable.create<Long> {
        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                .take(Int.MAX_VALUE.toLong())
                .subscribe(it::onNext)
    }.observeOn(Schedulers.newThread()).publish()
    observable.connect()

    observable.subscribe(subscriber1)
    observable.subscribe(subscriber2)

    try {
        Thread.sleep(20L)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }

    observable.subscribe(subscriber3)

    try {
        Thread.sleep(100L)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}

private fun ch2_2_2_cold() {
    println("ch2_2_2_cold")
    val subscriber1 = Consumer<Long> { println("subscriber1 :: $it") }
    val subscriber2 = Consumer<Long> { println("subscriber2 :: $it") }
    val observable = Observable.create<Long> {
        Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                .take(Int.MAX_VALUE.toLong())
                .subscribe(it::onNext)
    }.observeOn(Schedulers.newThread())

    observable.subscribe(subscriber1)
    observable.subscribe(subscriber2)

    try {
        Thread.sleep(120L)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}

private fun ch2_2_1_do() {
    println("ch2_2_1_do")
    Observable.just("Hello")
            .doOnNext { println("doOnNext :: $it") }
            .doAfterNext { println("doAfterNext :: $it") }
            .doOnComplete { println("doOnComplete ::") }
            .doOnSubscribe { println("doOnSubscribe ::") }
            .doAfterTerminate { println("doAfterTerminate ::") }
            .doFinally { println("doFinally :: ") }
            .doOnEach {
                val msg = when {
                    it.isOnNext -> "OnNext"
                    it.isOnError -> "OnError"
                    it.isOnComplete -> "OnComplete"
                    else -> ""
                }
                println("doOnEach :: $msg")
            }
            .doOnLifecycle(
                    { println("doOnLifecycle :: onSubscribe :: ${it.isDisposed}") },
                    { println("doOnLifecycle :: onDispose :: ") })
            .subscribe {
                println("onNext :: 收到消息 :: $it")
            }
}

fun ch_2_1() {
    println("ch_2_1")
    Observable.just("Hello World").subscribe { println("onNext::$it") }

    Observable.just("Hello World").subscribe(
            { println("onNext::$it") },
            { println("onError :: $it") })

    Observable.just("Hello World").subscribe(
            { println("onNext::$it") },
            { println("onError :: $it") },
            { println("onComplete ::") })

    Observable.just("Hello World").subscribe(
            { println("onNext::$it") },
            { println("onError :: $it") },
            { println("onComplete ::") },
            { println("onSubscribe :: $it") })
}

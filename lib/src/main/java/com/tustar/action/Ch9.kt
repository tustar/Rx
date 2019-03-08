package com.tustar.action

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers
import java.util.function.Function


fun main(args: Array<String>) {
    Observable.just("tu", "xing", "ping")
            .compose(RxTrace.logObservable<String>("first", RxTrace.LOG_SUBSCRIBE
                    or RxTrace.LOG_NEXT_DATA))
            .map {
                it.toUpperCase()
            }
            .compose(RxTrace.logObservable<String>("second", RxTrace.LOG_NEXT_DATA))
//            .compose {
//                it.subscribeOn(Schedulers.io())
//                        .observeOn(Schedulers.trampoline())
//            }
            .compose(RxTrace.logObservable<String>("third", RxTrace.LOG_COMPLETE
                    or RxTrace.LOG_TERMINATE))
            .subscribe {
                println("Result = $it")
            }
}

private fun transformer(): (Observable<Int>) -> Observable<String> {
    return { upstream ->
        upstream.map { "[$it]" }
    }
}


object RxTrace {

    @JvmField
    val LOG_NEXT_DATA = 1

    @JvmField
    val LOG_NEXT_EVENT = 1 shl 1

    @JvmField
    val LOG_ERROR = 1 shl 2

    @JvmField
    val LOG_COMPLETE = 1 shl 3

    @JvmField
    val LOG_SUBSCRIBE = 1 shl 4

    @JvmField
    val LOG_TERMINATE = 1 shl 5

    @JvmField
    val LOG_DISPOSE = 1 shl 6

    @JvmStatic
    fun <T> logObservable(tag: String, bitMask: Int): ObservableTransformer<T, T> {
        return ObservableTransformer {
            var upstream = it

            if (bitMask and LOG_NEXT_DATA > 0) {
                upstream = upstream.compose(oLogNext(tag))
            } else if (bitMask and LOG_NEXT_EVENT > 0) {
                upstream = upstream.compose(oLogNextEvent(tag))
            }

            if (bitMask and LOG_ERROR > 0) {
                upstream = upstream.compose(oLogError(tag))
            }

            if (bitMask and LOG_COMPLETE > 0) {
                upstream = upstream.compose(oLogComplete(tag))
            }

            if (bitMask and LOG_SUBSCRIBE > 0) {
                upstream = upstream.compose(oLogSubscribe(tag))
            }

            if (bitMask and LOG_DISPOSE > 0) {
                upstream = upstream.compose(oLogDispose(tag))
            }

            if (bitMask and LOG_TERMINATE > 0) {
                upstream = upstream.compose(oLogTerminate(tag))
            }

            upstream
        }
    }

    @JvmStatic
    fun <T> log(tag: String): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.compose(oLogAll(tag))
                    .compose(oLogNext(tag))
        }
    }

    private fun <T> oLogAll(tag: String): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.compose(oLogError(tag))
                    .compose(oLogComplete(tag))
                    .compose(oLogSubscribe(tag))
                    .compose(oLogTerminate(tag))
                    .compose(oLogDispose(tag))
        }
    }

    private fun <T> oLogNext(tag: String): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.doOnNext { data ->
                println("$tag [onNext] $data")
            }
        }
    }

    private fun <T> oLogNextEvent(tag: String): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.doOnNext {
                println("$tag [onNext]")
            }
        }
    }

    private fun <T> oLogError(tag: String): ObservableTransformer<T, T> {
        val message = Function<Throwable, String> {
            if (it.message != null) {
                it.message!!
            } else {
                it.javaClass.simpleName
            }
        }

        return ObservableTransformer {
            it.doOnError { throwable ->
                println("$tag [onError] ${message.apply(throwable)}")
            }
        }
    }

    private fun <T> oLogComplete(tag: String): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.doOnComplete {
                println("$tag [onComplete]")
            }
        }
    }

    private fun <T> oLogSubscribe(tag: String): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.doOnSubscribe {
                println("$tag [onSubscribe]")
            }
        }
    }

    private fun <T> oLogTerminate(tag: String): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.doOnTerminate {
                println("$tag [onTerminate]")
            }
        }
    }

    private fun <T> oLogDispose(tag: String): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.doOnDispose {
                println("$tag [onDispose]")
            }
        }
    }
}


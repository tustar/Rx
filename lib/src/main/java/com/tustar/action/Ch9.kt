package com.tustar.action

import io.reactivex.Observable


fun main(args: Array<String>) {
    Observable.just(123, 456)
            .compose(transformer())
            .subscribe {
                println("s=$it")
            }
}

private fun transformer(): (Observable<Int>) -> Observable<String> {
    return { upstream ->
        upstream.map { "[$it]" }
    }
}

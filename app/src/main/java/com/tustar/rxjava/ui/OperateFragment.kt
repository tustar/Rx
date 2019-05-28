package com.tustar.rxjava.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tustar.rxjava.util.Logger
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 */
class OperateFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.tustar.rxjava.R.layout.fragment_operate, container, false)
    }


    private fun create() {
        Observable.create(ObservableOnSubscribe<Int> { emitter ->
            try {
                if (!emitter.isDisposed) {
                    for (i in 0..9) {
                        emitter.onNext(i)
                    }
                    emitter.onComplete()
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }).subscribe(
                // onNext
                { Logger.d("onNext::$it") },
                // onError
                { Logger.d("onError::$it") },
                // onComplete
                { Logger.i("onComplete::") },
                // onSubscribe
                {
                    Logger.i("onSubscribe::")
                })

    }

    private fun just() {
        Observable.just("1", "2", "3", "4")
                .subscribe(
                        // onNext
                        { Logger.d("onNext::$it") },
                        // onError
                        { Logger.d("onError::$it") },
                        // onComplete
                        { Logger.i("onComplete::") },
                        // onSubscribe
                        {
                            Logger.i("onSubscribe::")
                        })
    }

    private fun from() {
        Observable.fromArray("hello", "from")
                .subscribe(
                        // onNext
                        { Logger.d("onNext::$it") },
                        // onError
                        { Logger.d("onError::$it") },
                        // onComplete
                        { Logger.i("onComplete::") },
                        // onSubscribe
                        {
                            Logger.i("onSubscribe::")
                        })

        val items = mutableListOf<Int>()
        for (i in 0..9) {
            items += i
        }
        Observable.fromIterable(items)
                .subscribe(
                        // onNext
                        { Logger.d("onNext::$it") },
                        // onError
                        { Logger.d("onError::$it") },
                        // onComplete
                        { Logger.i("onComplete::") },
                        // onSubscribe
                        {
                            Logger.i("onSubscribe::")
                        })

        val executorService = Executors.newCachedThreadPool()
        val future = executorService.submit(Callable<String> {
            Logger.i("模拟一些耗时的任务...")
            Thread.sleep(5000)
            "OK"
        })
        Observable.fromFuture(future)
                .subscribe(
                        // onNext
                        { Logger.d("onNext::$it") },
                        // onError
                        { Logger.d("onError::$it") },
                        // onComplete
                        { Logger.i("onComplete::") },
                        // onSubscribe
                        {
                            Logger.i("onSubscribe::")
                        })

        Observable.fromFuture(future, 4, TimeUnit.SECONDS)
                .subscribe(
                        // onNext
                        { Logger.d("onNext::$it") },
                        // onError
                        { Logger.d("onError::$it") },
                        // onComplete
                        { Logger.i("onComplete::") },
                        // onSubscribe
                        {
                            Logger.i("onSubscribe::")
                        })
    }

}// Required empty public constructor

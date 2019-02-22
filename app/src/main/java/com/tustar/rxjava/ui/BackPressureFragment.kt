package com.tustar.rxjava.ui


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tustar.rxjava.R
import com.tustar.rxjava.util.Logger
import com.tustar.rxjava.util.plus
import com.tustar.view.tap
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_backpressure.view.*
import org.reactivestreams.Subscription

/**
 * A simple [Fragment] subclass.
 *
 */
class BackPressureFragment : androidx.fragment.app.Fragment() {

    private var mInterrupted = true
    private var mDisposables = CompositeDisposable()
    private var mSubscription: Subscription? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_backpressure, container, false)
        mDisposables + view.test_back_pressure_1.tap().subscribe {
            mInterrupted = !mInterrupted
            testBackPressure1()
        }
        mDisposables + view.test_back_pressure_2.tap().subscribe {
            mInterrupted = !mInterrupted
            testBackPressure2()
        }
        mDisposables + view.test_back_pressure_3.tap().subscribe {
            mInterrupted = !mInterrupted
            testBackPressure3()
        }
        mDisposables + view.test_back_pressure_4.tap().subscribe {
            mInterrupted = !mInterrupted
            testBackPressure4()
        }
        mDisposables + view.test_back_pressure_4_request.tap().subscribe {
            mInterrupted = !mInterrupted
            onRequest()
        }
        mDisposables + view.test_back_pressure_5.tap().subscribe {
            mInterrupted = !mInterrupted
            testBackPressure5()
        }
        mDisposables + view.test_back_pressure_6.tap().subscribe {
            mInterrupted = !mInterrupted
            testBackPressure6()
        }
        return view
    }


    override fun onDestroy() {
        super.onDestroy()
        mDisposables.clear()
    }

    @SuppressLint("CheckResult")
    private fun testBackPressure1() {
        Observable.create<String> { e ->
            while (true) {
                if (mInterrupted) {
                    return@create
                }
                Logger.d("testBackPressure1")
                e.onNext("testBackPressure1")
            }
        }
                .subscribeOn(Schedulers.io())
                .subscribe { Logger.d("$it") }
    }

    @SuppressLint("CheckResult")
    private fun testBackPressure2() {
        Observable.create<String> { e ->
            while (true) {
                if (mInterrupted) {
                    return@create
                }
                Logger.d("testBackPressure2")
                e.onNext("testBackPressure2")
            }
        }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe {
                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                    Logger.d("$it")
                }
    }

    @SuppressLint("CheckResult")
    private fun testBackPressure3() {
        Flowable.create<String>({ e ->
            Logger.d("testBackPressure3-1")
            e.onNext("testBackPressure3-1")
            Logger.d("testBackPressure3-2")
            e.onNext("testBackPressure3-2")
            Logger.d("testBackPressure3-3")
            e.onNext("testBackPressure3-3")
        }, BackpressureStrategy.ERROR)
                .subscribe(
                        // onNext
                        { Logger.d("onNext::$it") },
                        // onError
                        { Logger.d("onError::$it") },
                        // onComplete
                        { Logger.i("onComplete::") },
                        // onSubscribe
                        {
                            it.request(2)
                            Logger.i("onSubscribe::")
                        })
    }

    @SuppressLint("CheckResult")
    private fun testBackPressure4() {
        Flowable.create<String>({ e ->
            Logger.d("testBackPressure4-1")
            e.onNext("testBackPressure4-1")
            Logger.d("testBackPressure4-2")
            e.onNext("testBackPressure4-2")
            Logger.d("testBackPressure4-3")
            e.onNext("testBackPressure4-3")
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io()) // 在io线程中订阅
                .observeOn(Schedulers.newThread()) // 在新线程中观察
                .subscribe(
                        // onNext
                        { Logger.d("onNext::$it") },
                        // onError
                        { Logger.d("onError::$it") },
                        // onComplete
                        { Logger.i("onComplete::") },
                        // onSubscribe
                        {
                            mSubscription = it
                            Logger.i("onSubscribe::")
                        })
    }

    private fun onRequest() {
        mSubscription?.request(3)
    }

    @SuppressLint("CheckResult")
    private fun testBackPressure5() {
        Flowable.create<String>({ e ->
            for (i in 0..Flowable.bufferSize()) {
                Logger.i("testBackPressure5-$i")
                e.onNext("testBackPressure5-$i")
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io()) // 在io线程中订阅
                .observeOn(Schedulers.newThread()) // 在新线程中观察
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

    @SuppressLint("CheckResult")
    private fun testBackPressure6() {
        Flowable.range(0, 300)
                .onBackpressureLatest()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
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
}

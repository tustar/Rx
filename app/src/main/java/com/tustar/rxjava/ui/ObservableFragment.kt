package com.tustar.rxjava.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.core.widget.ContentLoadingProgressBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.tustar.rxjava.util.Logger
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import com.tustar.rxjava.R


class ObservableFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = ObservableFragment()
    }

    private lateinit var viewModel: ObservableViewModel
    // View
    private lateinit var progressBar: ContentLoadingProgressBar
    private lateinit var countsView: TextView
    private lateinit var bytesView: TextView
    private lateinit var groupProgress: Group
    //
    private lateinit var pasteBtn: Button
    private lateinit var cancelBtn: Button
    //
    private val compositeDisposable = CompositeDisposable()
    //
    private var pasteDisposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_observable, container, false).apply {
            initViews(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ObservableViewModel::class.java)

        RxJavaPlugins.setErrorHandler { throwable -> Logger.e(throwable.message) }
    }

    override fun onResume() {
        super.onResume()
        // RxJava 2.0 中创建被观察者
        val observable = Observable.create(ObservableOnSubscribe<Int> { emitter ->
            emitter.onNext(1)
            emitter.onNext(2)
            emitter.onError(Exception("Error"))
            emitter.onComplete()
        })

        // RxJava 2.0中创建观察者
        val observer = object : Observer<Int> {

            override fun onSubscribe(d: Disposable) {
                Logger.d("$d")
            }

            override fun onNext(t: Int) {
                Logger.d("$t")
            }

            override fun onError(e: Throwable) {
                Logger.d(e.message)
            }

            override fun onComplete() {
                Logger.i()
            }
        }

        // Single
        Single.create(SingleOnSubscribe<Int> { emitter ->
            emitter.onSuccess(1)
        }).subscribe(object : SingleObserver<Int> {
            override fun onSubscribe(d: Disposable) {
                Logger.i("$d")
            }

            override fun onSuccess(t: Int) {
                Logger.i("$t")
            }

            override fun onError(e: Throwable) {
                Logger.i("${e.message}")
            }
        })

        // Completable
        Completable.create { emitter ->
            emitter.onComplete()
        }

        // Scheduler
        compositeDisposable.add(
                Observable.just(1, 2, 3, 4)
                        .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                        .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                        .subscribe {
                            Logger.d("$it")
                        })

    }

    private fun initViews(view: View) {
        with(view) {
            progressBar = findViewById(R.id.progressbar)
            countsView = findViewById(R.id.counts)
            bytesView = findViewById(R.id.bytes)
            groupProgress = findViewById(R.id.group_progress)
            //
            pasteBtn = findViewById(R.id.paste)
            pasteBtn.setOnClickListener {
                paste()
            }
            //
            cancelBtn = findViewById(R.id.cancel)
            cancelBtn.setOnClickListener {
                pasteDisposable?.let { disposable ->
                    if (!disposable.isDisposed) {
                        disposable.dispose()
                    }
                }

            }
        }
    }

    fun paste() {
        var totalBytes = 0
        val max = (1..2500).random()
        pasteDisposable = Flowable.create(
                { emitter: FlowableEmitter<PasteInfo> ->
                    for (i in 1..max) {
                        val byte = (0..1024 * 1024).random()
                        //
                        try {
                            Thread.sleep(10)
                        } catch (e: InterruptedException) {
                            emitter.onError(e)
                        }
                        emitter.onNext(PasteInfo(i, max, byte))
                    }
                    emitter.onComplete()
                }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    groupProgress.visibility = View.VISIBLE
                    progressBar.progress = 0
                    pasteBtn.isEnabled = false
                }
                .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            Logger.i("$it")
                            progressBar.progress = it.current * 100 / it.total
                            countsView.text = "${it.current}/${it.total}"

                            totalBytes += it.byte
                            bytesView.text = String.format("%.02fMB",
                                    totalBytes.toFloat() / (1024 * 1024))

                            if (it.current == it.total) {
                                pasteBtn.isEnabled = true
                            }
                        },
                        { t: Throwable ->
                            Logger.e("${t.message}")
                            pasteBtn.isEnabled = true
                        },
                        {
                            pasteBtn.isEnabled = true
                        })
        compositeDisposable.add(pasteDisposable!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    data class PasteInfo(val current: Int,
                         val total: Int,
                         val byte: Int) {
        override fun toString(): String {
            return "$current/$total, ${byte}B"
        }
    }
}

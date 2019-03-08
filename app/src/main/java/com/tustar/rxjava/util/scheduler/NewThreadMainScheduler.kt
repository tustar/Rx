package com.tustar.rxjava.util.scheduler

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewThreadMainScheduler<T> : BaseScheduler<T>(Schedulers.newThread(),
        AndroidSchedulers.mainThread())
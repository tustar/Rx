package com.tustar.rxjava.util.scheduler

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ComputationMainScheduler<T>:BaseScheduler<T>(Schedulers.computation(),
        AndroidSchedulers.mainThread())
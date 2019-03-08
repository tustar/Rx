package com.tustar.rxjava.util.scheduler

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TrampolineMainScheduler<T> : BaseScheduler<T>(Schedulers.trampoline(),
        AndroidSchedulers.mainThread())
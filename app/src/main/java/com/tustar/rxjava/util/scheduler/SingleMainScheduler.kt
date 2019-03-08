package com.tustar.rxjava.util.scheduler

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SingleMainScheduler<T>:BaseScheduler<T>(Schedulers.single(), AndroidSchedulers.mainThread())
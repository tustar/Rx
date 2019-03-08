package com.tustar.rxjava.util.scheduler

object SchedulerUtils {

    @JvmStatic
    fun <T> ioToMain(): BaseScheduler<T> = IoToMainScheduler()
}
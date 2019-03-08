package com.tustar.rxjava.util.scheduler

object SchedulerUtils {

    @JvmStatic
    fun <T> ioToMain(): IoMainScheduler<T> = IoMainScheduler()

    @JvmStatic
    fun <T> computationToMain(): ComputationMainScheduler<T> = ComputationMainScheduler()

    @JvmStatic
    fun <T> singleToMain(): SingleMainScheduler<T> = SingleMainScheduler()

    @JvmStatic
    fun <T> trampolineToMain(): TrampolineMainScheduler<T> = TrampolineMainScheduler()

    @JvmStatic
    fun <T> newThreadToMain(): NewThreadMainScheduler<T> = NewThreadMainScheduler()
}
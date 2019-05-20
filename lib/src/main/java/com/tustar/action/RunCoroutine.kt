package com.tustar.action

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    val start = System.currentTimeMillis()
    runBlocking {
        val jobs = List(10_000) {
            launch(Dispatchers.Default) {
                delay(1000)
                println("thread name=" + Thread.currentThread().name)
            }
        }

        jobs.forEach {
            it.join()
        }

        val spend = (System.currentTimeMillis() - start) / 100
        println("coroutines: spend= $spend s")
    }
}



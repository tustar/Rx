package com.tustar.action

import kotlin.concurrent.thread

fun main() {
    val start = System.currentTimeMillis()

    val threads = List(100_000) {
        // 创建新的线程
        thread {
            Thread.sleep(1000)
            println(Thread.currentThread().name)
        }
    }

    threads.forEach { it.join() }

    val spend = (System.currentTimeMillis()-start)/1000

    println("Threads: spend= $spend s")
}
package com.tustar.action

import kotlinx.coroutines.*

fun main() {
    val Archive_4 = arrayListOf(".zip", ".rar")
    println(Archive_4.joinToString(",", "(", ")") {
        "'$it'"
    })

   println("adaf.apk".substringAfterLast('.', ""))
}

private fun dIsActive() {
    runBlocking {

        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var tempTime = startTime
            var i = 0
            while (isActive) {

                if (System.currentTimeMillis() >= tempTime) {
                    println("count time: ${i++}")
                    tempTime += 500L
                }
            }
        }
        delay(2100L)
        job.cancelAndJoin()
    }
}

private fun dCancelAndJoin() {
    runBlocking {
        val job = launch {
            repeat(100) {
                println("count time $it")
                delay(500L)
            }
        }
        delay(2100L)
        job.cancelAndJoin()
    }
}

private fun dCancel() {
    val job = GlobalScope.launch {
        delay(1000L)
        println("Hello world")
    }
    job.cancel()
    println(job.isCancelled)
    Thread.sleep(2000L)
}


fun dAddWithContext() {
    runBlocking {
        GlobalScope.launch {
            val result1 = withContext(Dispatchers.Default) {
                delay(2000L)
                1
            }
            val result2 = withContext(Dispatchers.Default) {
                delay(1000L)
                2
            }
            val result = result1 + result2
            println("result:$result")
        }

        delay(5000L)
    }
}

fun dAdd() {
    runBlocking {
        GlobalScope.launch {
            val result1 = async {
                delay(2000L)
                1
            }
            val result2 = async {
                delay(1000L)
                2
            }
            val result = result1.await() + result2.await()
            println("result:$result")
        }

        delay(5000L)
    }
}

fun dLaunch() {
    val job = GlobalScope.launch {
        delay(1000L)
        print("Hello world")
    }

    Thread.sleep(2000L)
}

fun dAsync() {
    val deferred = GlobalScope.async {
        delay(1000L)
        print("Hello world")
    }

    Thread.sleep(2000L)
}

fun dRunBlocking() {
    runBlocking {
        launch {
            delay(1000L)
            print("Hello world")
        }

        delay(2000L)
    }
}
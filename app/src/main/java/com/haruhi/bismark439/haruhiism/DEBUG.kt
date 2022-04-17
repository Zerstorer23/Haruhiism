package com.haruhi.bismark439.haruhiism

object DEBUG {
    const val DEBUG_IMMEDIATE_ALARM = false

    fun printStack() {
        val sb = StringBuilder()
        val stack = Thread.currentThread().stackTrace
        for (line in stack) sb.append("$line \n")
        println(sb.toString())
    }
}
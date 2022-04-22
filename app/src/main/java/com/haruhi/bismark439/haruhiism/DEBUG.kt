package com.haruhi.bismark439.haruhiism

object DEBUG {
    const val DEBUG_IMMEDIATE_ALARM = false

    fun printStack() {
        val sb = StringBuilder()
        val stack = Thread.currentThread().stackTrace
        for (line in stack) sb.append("$line \n")
        println(sb.toString())
    }

    private var beginTime = 0L
    fun start(){
        beginTime = System.currentTimeMillis()
    }
    fun lap(tag:String){
        val elapsed = System.currentTimeMillis() - beginTime
        println("Elapsed $tag = $elapsed")
    }
}

/*
^(?!MediaPlayer)

* */
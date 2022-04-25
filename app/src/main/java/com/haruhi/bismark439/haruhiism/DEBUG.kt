package com.haruhi.bismark439.haruhiism

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

object DEBUG {
    const val DEBUG_IMMEDIATE_ALARM = false
    const val debugMode = true
    fun printStack() {
        val sb = StringBuilder()
        val stack = Thread.currentThread().stackTrace
        for (line in stack) sb.append("$line \n")
        DEBUG.appendLog(sb.toString())
    }

    private var beginTime = 0L
    fun start(){
        beginTime = System.currentTimeMillis()
    }
    fun lap(tag:String){
        val elapsed = System.currentTimeMillis() - beginTime
        DEBUG.appendLog("Elapsed $tag = $elapsed")
    }
    private var firstTime = true
    private fun checkFile(logFile:File){
        if(firstTime){
            logFile.delete()
            logFile.createNewFile()
            firstTime= false
        }else{
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    }
    fun appendLog(text: String?) {
        println(text)
        if(!debugMode) return
        val logFile =File("sdcard/log.txt")//File(MyApp.appContext.getExternalFilesDir("haruhiism"),)
        checkFile(logFile)
        try {
            //BufferedWriter for performance, true to set append to file flag
            val buf = BufferedWriter(FileWriter(logFile, true))
            buf.append(text)
            buf.newLine()
            buf.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

/*
^(?!MediaPlayer)

* */
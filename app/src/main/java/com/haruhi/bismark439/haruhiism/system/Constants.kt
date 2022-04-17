package com.haruhi.bismark439.haruhiism.system

object Constants {

//adb shell am broadcast -a android.intent.action.BOOT_COMPLETED
    //adb -s ce07171780f8c0030b7e shell am broadcast -a android.intent.action.BOOT_COMPLETED -p com.haruhi.bismark439.haruhiism
    //adb devices
    const val NOTIFICATION_CHANNEL_ID = "com.haruhi.bismark439.haruhiism:notification_channel"

    const val FILE_HARUHI_CHAN_MP3 = "haruhi-chan.mp3"
    const val FILE_WAKATERU_MP3 = "wakateru.mp3"

    const val FILE_KYON_MIKURU_KINSOKU_MP3 = "kinsoku/kyon_kinsoku.mp3"
}

fun String.replaceAt(index: Int, newChar: Char): String {
    return this.substring(0, index) + newChar + this.substring(index + 1)
}

fun String.isTrueAt(index: Int): Boolean {
    return this[index] != '0'
}

fun String.toDayName(index: Int): String {
    return when (index) {
        0 -> "월"
        1 -> "화"
        2 -> "수"
        3 -> "목"
        4 -> "금"
        5 -> "토"
        6 -> "일"
        else -> "NaN"
    }
}

fun String.getDays(): String {
    val sb = StringBuilder()
    if(this.length < 7) return "?"
    for(i in 0..6){
        if(this[i]!='0'){
            sb.append(this.toDayName(i))
        }
    }
    return sb.toString()
}


typealias VoidReturn = () -> Unit
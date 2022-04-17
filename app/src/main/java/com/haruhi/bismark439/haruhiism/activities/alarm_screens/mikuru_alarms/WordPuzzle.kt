package com.haruhi.bismark439.haruhiism.activities.alarm_screens.mikuru_alarms

import android.content.Context
import com.haruhi.bismark439.haruhiism.R

/**
 * Created by Bismark439 on 17/01/2018.
 */
class WordPuzzle(private val context: Context, private val type: WordType) {
    private var wordDB: ArrayList<Piece> = ArrayList()
    private lateinit var wordLists: List<String>
    val random: Piece
        get() {
            val rand = (Math.random() * wordDB.size).toInt()
            return wordDB[rand]
        }

    init {
        initialise()
    }

    private fun initialise() {
        val resources = when (type) {
            WordType.Place -> place
            WordType.Tool -> tool
            WordType.Verb -> verb
            WordType.Sentence -> sentence
            WordType.Verb2 -> verb2
            WordType.Adjective -> adjective
        }
        wordLists = resources.map { res -> context.resources.getString(res) }
        for (s in wordLists) {
            val tt = Piece(s, type)
            wordDB.add(tt)
        }
    }

    companion object {

        val place = arrayOf(R.string.future)
        val tool = arrayOf(
            R.string.q1,
            R.string.q2,
            R.string.q3,
            R.string.q4,
            R.string.q5,
        )
        val verb = arrayOf(
            R.string.q6,
            R.string.q7,
            R.string.q8,
            R.string.q9,
            R.string.q0,
            R.string.q10,
        )

        val sentence = arrayOf(
            R.string.q11,
            R.string.q12,
            R.string.q13,
            R.string.q14,
            R.string.q15,
            R.string.q16,
        )
        val verb2 = arrayOf(
            R.string.q18,
            R.string.q19,
            R.string.q20,
            R.string.q21,
            R.string.q22,
            R.string.q23,
        )
        val adjective = arrayOf(
            R.string.q24,
            R.string.q25,
            R.string.q26,
            R.string.q27,
            R.string.q28,
            R.string.q29,
        )
    }
}

enum class WordType {
    Place,
    Tool,
    Verb,
    Sentence,
    Verb2,
    Adjective
}

data class Piece(var word: String, var ansType: WordType)
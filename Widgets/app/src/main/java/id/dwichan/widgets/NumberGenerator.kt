package id.dwichan.widgets

import java.util.*


internal object NumberGenerator {
    fun generateRandomNumbers(max: Int): Int {
        val random = Random()
        return random.nextInt(max)
    }
}
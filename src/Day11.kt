import helper.util.println
import helper.util.readInput
import java.lang.Math.pow
import kotlin.math.log10
import kotlin.math.pow

fun main() {

    fun part1(input: List<String>): Long {
        return blink(input, 25).values.sum()
    }

    fun part2(input: List<String>): Long {
        return blink(input, 75).values.sum()
    }

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

internal fun blink(input: List<String>, times: Int): Map<Long, Long> {
    var counterMap = input.first()
        .split(" ")
        .map { it.toLong() }
        .groupingBy { it }
        .eachCount()
        .mapValues { it.value.toLong() }
    repeat(times) {
        val newMap = mutableMapOf<Long, Long>()
        for ((key, value) in counterMap) {
            if (key == 0L) {
                newMap.putIfAbsent(1, 0)
                newMap[1] = newMap[1]!! + value
            } else {
                val digits = key.toString().length
                if (digits.toLong() % 2 == 0L) {
                    val magnitude = 10.0.pow(digits / 2)
                    val left = (key / magnitude).toLong()
                    val right = (key % magnitude).toLong()
                    newMap.putIfAbsent(left, 0L)
                    newMap[left] = newMap[left]!! + value
                    newMap.putIfAbsent(right, 0L)
                    newMap[right] = newMap[right]!! + value
                } else {
                    val nextValue = key * 2024
                    newMap.putIfAbsent(nextValue, 0)
                    newMap[nextValue] = newMap[nextValue]!! + value
                }
            }
        }
        counterMap = newMap
    }
    return counterMap
}
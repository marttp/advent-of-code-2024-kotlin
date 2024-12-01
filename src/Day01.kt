import helper.util.println
import helper.util.readInput
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Long {
        val (firstList, secondList) = input.map { line ->
            line.split(" ")
                .filter { it.isNotBlank() }
                .map { it.toLong() }
        }
            .map { Pair(it[0], it[1]) }
            .unzip()
        val sortedFirstList = firstList.sorted()
        val sortedSecondList = secondList.sorted()

        return sortedFirstList.zip(sortedSecondList)
            .sumOf { abs(it.first - it.second) }
    }

    fun part2(input: List<String>): Long {
        val (firstList, secondList) = input.map { line ->
            line.split(" ")
                .filter { it.isNotBlank() }
                .map { it.toLong() }
        }
            .map { Pair(it[0], it[1]) }
            .unzip()

        val counterMap = firstList.associateWith { 0 }
            .toMutableMap()
        secondList.forEach {
            if (counterMap.containsKey(it)) {
                counterMap[it] = counterMap[it]!! + 1
            }
        }

        return counterMap.entries.sumOf { it.key * it.value }
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

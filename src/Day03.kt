import helper.util.println
import helper.util.readInput
import java.util.*

fun main() {

    fun part1(input: List<String>): Int {
        val longText = input.joinToString("")
        val regex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
        return regex.findAll(longText)
            .map { it.groupValues[1].toInt() * it.groupValues[2].toInt() }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val longText = input.joinToString("")
        val multiplyRegex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
        val multiplyQueue: LinkedList<MatchResult> = multiplyRegex.findAll(longText)
            .map { it }
            .toCollection(LinkedList())
        val commandRegex = Regex("do(?:n't)?\\(\\)")
        val commandQueue: LinkedList<MatchResult> = commandRegex.findAll(longText)
            .map { it }
            .toCollection(LinkedList())

        var isMultiplyEnabled = true
        var sum = 0

        while (multiplyQueue.isNotEmpty()) {
            val nextOperation = multiplyQueue.removeFirst()
            while (commandQueue.isNotEmpty() && commandQueue.peek().range.first < nextOperation.range.first) {
                val command = commandQueue.removeFirst()
                isMultiplyEnabled = command.value == "do()"
            }

            if (isMultiplyEnabled) {
                val op1 = nextOperation.groupValues[1].toInt()
                val op2 = nextOperation.groupValues[2].toInt()
                sum += op1 * op2
            }
        }

        return sum
    }

    fun part2Optimize(input: List<String>): Int {
        val longText = input.joinToString("")
        val multiplyRegex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)|do(?:n't)?\\(\\)")

        var isMultiplyEnabled = true
        var sum = 0

        multiplyRegex.findAll(longText)
            .forEach { matchResult ->
                when (matchResult.value) {
                    "do()", "don't()" -> isMultiplyEnabled = matchResult.value == "do()"
                    else -> {
                        if (isMultiplyEnabled) {
                            val op1 = matchResult.groupValues[1].toInt()
                            val op2 = matchResult.groupValues[2].toInt()
                            sum += op1 * op2
                        }
                    }
                }
            }

        return sum
    }


    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
    part2Optimize(input).println()
}

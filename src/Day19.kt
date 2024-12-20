import helper.util.println
import helper.util.readInput

fun main() {

    fun part1(input: List<String>): Int {
        val flags = input[0].split(", ")
        return input.drop(2).count { canConstruct(it, flags) }
    }

    fun part2(input: List<String>): Long {
        val flags = input[0].split(", ")
        return input.drop(2).sumOf { countConstruct(it, flags) }
    }

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}

fun canConstruct(pattern: String, flags: List<String>): Boolean {
    val length = pattern.length
    val dp = BooleanArray(length + 1)
    dp[0] = true
    for (i in pattern.indices) {
        if (dp[i]) {
            for (flag in flags) {
                val nextLength = i + flag.length
                if (nextLength <= length && pattern.substring(i, nextLength) == flag) {
                    dp[nextLength] = true
                }
            }
        }
    }
    return dp[length]
}

fun countConstruct(pattern: String, flags: List<String>): Long {
    val length = pattern.length
    val dp = LongArray(length + 1)
    dp[0] = 1L
    for (i in pattern.indices) {
        for (flag in flags) {
            val nextLength = i + flag.length
            if (nextLength <= length && pattern.substring(i, nextLength) == flag) {
                dp[nextLength] += dp[i]
            }
        }
    }
    return dp[length]
}
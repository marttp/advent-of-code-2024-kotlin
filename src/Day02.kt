import helper.util.println
import helper.util.readInput
import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        val reports = input.map { line ->
            line.split(" ").map { it.toInt() }
        }
        return reports.map { checkSafe(it) }.count { it }
    }

    fun part2(input: List<String>): Int {
        val reports = input.map { line ->
            line.split(" ").map { it.toInt() }
        }
        return reports.map { checkSafeWithDampener(it) }.count { it }
    }


    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

internal fun checkSafeWithDampener(report: List<Int>): Boolean {
    if (report.size == 1) {
        return true
    }
    if (checkSafe(report)) {
        return true
    }

    for (i in report.indices) {
        if (checkSafe(report.subList(0, i) + report.subList(i + 1, report.size))) {
            return true
        }
    }

    return false
}

internal fun checkSafe(report: List<Int>): Boolean {
    if (report.size == 1) {
        return true
    }

    var left = 0
    var right = 1
    val isAsc = report[0] < report[1]

    while (right < report.size) {
        val diff = abs(report[right] - report[left])
        if (diff < 1 || diff > 3 || (isAsc && report[right] < report[left]) || (!isAsc && report[right] > report[left])) {
            return false
        }
        left++
        right++
    }

    return true
}

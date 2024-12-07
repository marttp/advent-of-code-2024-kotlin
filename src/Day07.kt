import helper.util.println
import helper.util.readInput


fun main() {

    fun part1(input: List<String>): Long {
        return extractInput(input)
            .filter { validateEquation(it.equationResult, it.operands, 1, it.operands.first(), false) }
            .sumOf { it.equationResult }
    }

    fun part2(input: List<String>): Long {
        return extractInput(input)
            .filter { validateEquation(it.equationResult, it.operands, 1, it.operands.first(), true) }
            .sumOf { it.equationResult }
    }

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

internal fun validateEquation(
    equationResult: Long,
    operands: List<Long>,
    idx: Int,
    current: Long,
    isThirdOperator: Boolean
): Boolean {

    if (operands.size == idx) {
        return current == equationResult
    }
    if (current > equationResult && !isThirdOperator) {
        return false
    }

    val result = validateEquation(equationResult, operands, idx + 1, current + operands[idx], isThirdOperator) ||
            validateEquation(equationResult, operands, idx + 1, current * operands[idx], isThirdOperator)
    if (result) {
        return true
    }

    if (isThirdOperator) {
        var digits = operands[idx]
        var multiplier = 1
        while (digits > 0) {
            digits /= 10
            multiplier *= 10
        }
        val newValue = current * multiplier + operands[idx]
        if (validateEquation(equationResult, operands, idx + 1, newValue, isThirdOperator)) {
            return true
        }
    }

    return false
}

internal fun extractInput(input: List<String>): List<EquationInfo> {
    val (firstList, secondList) = input.map { line ->
        line.split(":")
            .map { it.trim() }
            .filter { it.isNotBlank() }
    }
        .map { Pair(it[0].toLong(), it[1].split(" ").map { it.toLong() }) }
        .unzip()
    return firstList.zip(secondList)
        .map { EquationInfo(it.first, it.second) }
}

internal data class EquationInfo(
    val equationResult: Long,
    val operands: List<Long>
)



package helper.util

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/input/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

val digitMap = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
)

val EIGHT_DIRECTIONS = listOf(
    Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
    Pair(0, -1), Pair(0, 1),
    Pair(1, -1), Pair(1, 0), Pair(1, 1)
)

val FOUR_DIRECTIONS = listOf(
    Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1)
)

fun <T> isInBounds(matrix: Array<Array<T>>, row: Int, col: Int): Boolean {
    return row in matrix.indices && col in matrix[0].indices
}

fun <T> isInBounds(matrix: List<List<T>>, row: Int, col: Int): Boolean {
    return row in matrix.indices && col in matrix[0].indices
}

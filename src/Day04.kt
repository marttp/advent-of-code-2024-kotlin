import helper.util.EIGHT_DIRECTIONS
import helper.util.println
import helper.util.readInput
import java.util.*

fun main() {

    fun part1(input: List<String>): Int {
        val table = input.map { it.toCharArray() }
        val xmas = "XMAS".toCharArray()
        var count = 0
        for (r in table.indices) {
            for (c in table[r].indices) {
                if (table[r][c] == 'X') {
                    for (dir in EIGHT_DIRECTIONS) {
                        if (checkDirection(table, r, c, dir.first, dir.second, xmas)) {
                            count++
                        }
                    }
                }
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        val table = input.map { it.toCharArray() }
        var count = 0
        /*
        it's an X-MAS puzzle in which you're supposed to find two MAS in the shape of an X.
        One way to achieve that is like this:
            M.S
            .A.
            M.S
        but it can be below pattern as well
            S.S
            .A.
            M.M
        */
        for (r in 0 until table.size - 2) {
            for (c in 0 until table[r].size - 2) {
                // Scan each character
                // Let say first character on first line of block 3 x 3
                if (checkXmasPattern(table, r, c)) {
                    count++
                }
            }
        }
        return count
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

fun checkDirection(table: List<CharArray>, r: Int, c: Int, dr: Int, dc: Int, xmas: CharArray): Boolean {
    for (i in xmas.indices) {
        val nr = r + (dr * i)
        val nc = c + (dc * i)
        if (!isInbound(table, nr, nc) || table[nr][nc] != xmas[i]) {
            return false
        }
    }
    return true
}

fun isInbound(table: List<CharArray>, r: Int, c: Int): Boolean =
    r >= 0 && c >= 0 && r < table.size && c < table[0].size

fun checkXmasPattern(table: List<CharArray>, r: Int, c: Int): Boolean {
    if (table[r + 1][c + 1] != 'A') {
        return false
    }
    val diagonalLine1 = (table[r][c] == 'M' && table[r + 2][c + 2] == 'S') ||
            (table[r][c] == 'S' && table[r + 2][c + 2] == 'M')
    val diagonalLine2 = (table[r + 2][c] == 'M' && table[r][c + 2] == 'S') ||
            (table[r + 2][c] == 'S' && table[r][c + 2] == 'M')
    return diagonalLine1 && diagonalLine2
}

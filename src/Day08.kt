import helper.point.Point2D
import helper.util.println
import helper.util.readInput
import kotlin.math.abs

internal const val EMPTY = '.'

fun main() {

    fun part1(input: List<String>): Long {
        val grid = input.map { it.toCharArray() }.toTypedArray()
        var total = 0L
        val seen = mutableSetOf<Point2D>()
        for (r in grid.indices) {
            for (c in grid[r].indices) {
                if (grid[r][c] != EMPTY) {
                    total += countResonant(grid, seen, r, c)
                }
            }
        }
        return total
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toCharArray() }.toTypedArray()
        val antinodes = mutableSetOf<Point2D>()
        val freqPositions = mutableMapOf<Char, MutableList<Point2D>>()

        for (r in grid.indices) {
            for (c in grid[r].indices) {
                val ch = grid[r][c]
                if (ch != EMPTY) {
                    freqPositions.getOrPut(ch) { mutableListOf() }.add(Point2D(r, c))
                }
            }
        }

        // Process each frequency
        for (positions in freqPositions.values) {
            // For each pair of antennas of the same frequency
            for (i in positions.indices) {
                for (j in i + 1 until positions.size) {
                    val (r1, c1) = positions[i]
                    val (r2, c2) = positions[j]

                    // Add the antennas themselves
                    antinodes.add(Point2D(r1, c1))
                    antinodes.add(Point2D(r2, c2))

                    // Check all points for collinearity
                    for (r in grid.indices) {
                        for (c in grid[r].indices) {
                            if (isCollinear(r1, c1, r2, c2, r, c)) {
                                antinodes.add(Point2D(r, c))
                            }
                        }
                    }
                }
            }
        }

        return antinodes.size
    }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

internal fun countResonant(grid: Array<CharArray>, seen: MutableSet<Point2D>, initRow: Int, initCol: Int): Long {
    val target = grid[initRow][initCol]
    var result = 0L
    for (r in initRow + 1 until grid.size) {
        for (c in grid[r].indices) {
            if (grid[r][c] == target) {
                val diffRow = abs(initRow - r)
                val diffCol = abs(initCol - c)

                val nr = r + diffRow
                val nc = if (c <= initCol) {
                    c - diffCol
                } else {
                    c + diffCol
                }
                if (grid.isInBounds(Point2D(nr, nc)) && seen.add(Point2D(nr, nc))) {
                    result++
                }

                val fr = initRow - diffRow
                val fc = if (c <= initCol) {
                    initCol + diffCol
                } else {
                    initCol - diffCol
                }
                if (grid.isInBounds(Point2D(fr, fc)) && seen.add(Point2D(fr, fc))) {
                    result++
                }
            }
        }
    }
    return result
}

internal fun isCollinear(x1: Int, y1: Int, x2: Int, y2: Int, x3: Int, y3: Int): Boolean =
    (x2 - x1) * (y3 - y1) == (y2 - y1) * (x3 - x1)
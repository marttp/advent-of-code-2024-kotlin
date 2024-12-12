import helper.point.Point2D
import helper.util.println
import helper.util.readInput

internal const val START = 0
internal const val TOP = 9
internal const val MARKED = 100

fun main() {

    fun part1(input: List<String>): Int {
        val grid = input.map { line -> line.toCharArray().map { it.digitToInt() }.toMutableList() }
        return grid.findTrailhead()
            .sumOf { countReachTop(grid, it, 0, mutableSetOf()) }
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { line -> line.toCharArray().map { it.digitToInt() }.toMutableList() }
        return grid.findTrailhead()
            .sumOf { countReachTop(grid, it, 0) }
    }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

internal fun List<MutableList<Int>>.findTrailhead(): MutableList<Point2D> {
    val trailhead = mutableListOf<Point2D>()
    for (i in indices) {
        for (j in this[i].indices) {
            if (this[i][j] == START) {
                trailhead.add(Point2D(i, j))
            }
        }
    }
    return trailhead
}

internal fun isValid(grid: List<List<Int>>, position: Point2D): Boolean =
    grid.isInBounds(point = position) && grid[position.row][position.column] != MARKED

internal fun countReachTop(grid: List<MutableList<Int>>, position: Point2D, expected: Int): Int {
    if (!isValid(grid, position)) {
        return 0
    }
    val current = grid[position]
    if (current != expected) {
        return 0
    }
    if (current == TOP) {
        return 1
    }
    var count = 0
    grid[position] = MARKED
    for (next in position.getCardinalNeighbors()) {
        count += countReachTop(grid, next, expected + 1)
    }
    grid[position] = current
    return count
}


internal fun countReachTop(
    grid: List<MutableList<Int>>,
    position: Point2D,
    expected: Int,
    visited: MutableSet<Point2D>
): Int {
    if (!isValid(grid, position)) {
        return 0
    }
    val current = grid[position]
    if (current != expected) {
        return 0
    }
    if (current == TOP) {
        return if (visited.add(position)) 1 else 0
    }
    var count = 0
    grid[position] = MARKED
    for (next in position.getCardinalNeighbors()) {
        count += countReachTop(grid, next, expected + 1, visited)
    }
    grid[position] = current
    return count
}

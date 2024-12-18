import helper.point.Point2D
import helper.util.println
import helper.util.readInput

fun main() {

    fun part1(input: List<String>): Int {
        val ramRun = RamRun(70, 70, input.take(1024))
        return ramRun.shortestPath(Point2D(0, 0), Point2D(70, 70))
    }

    fun part2(input: List<String>): String {
        var bytes = 1
        while (bytes < input.size) {
            val ramRun = RamRun(70, 70, input.take(bytes))
            val result = ramRun.shortestPath(Point2D(0, 0), Point2D(70, 70))
            if (result == 0) {
                return input[bytes - 1]
            }
            bytes++
        }
        return "0,0"
    }

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}

class RamRun(x: Int, y: Int, bytes: List<String>) {

    private val grid = Array(y + 1) { CharArray(x + 1) { EMPTY } }

    init {
        bytes.forEach {
            val (c, r) = it.split(",").take(2).map { it.toInt() }
            grid[r][c] = OBSTACLE
        }
    }

    fun shortestPath(start: Point2D, end: Point2D): Int {
        val visited = mutableSetOf<Point2D>()
        val queue = ArrayDeque<Pair<Point2D, Int>>()
        queue.add(Pair(start, 0))

        while (queue.isNotEmpty()) {
            val (coord, distance) = queue.removeFirst()
            if (coord == end) {
                return distance
            }
            for (dir in coord.getCardinalNeighbors()) {
                if (dir.row in grid.indices && dir.column in grid[0].indices &&
                    grid[dir.row][dir.column] == EMPTY && visited.add(dir)
                ) {
                    queue.add(Pair(dir, distance + 1))
                }
            }
        }

        return 0
    }

    companion object {
        const val EMPTY = '.'
        const val OBSTACLE = '#'
    }
}
import Day06.EMPTY
import Day06.MARKED
import Day06.OBSTACLE
import Day06.STARTER
import helper.point.Point2D
import helper.point.Point2D.Companion.EAST
import helper.point.Point2D.Companion.NORTH
import helper.point.Point2D.Companion.SOUTH
import helper.point.Point2D.Companion.WEST
import helper.util.digitMap
import helper.util.println
import helper.util.readInput
import java.util.LinkedList


fun main() {

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toCharArray() }.toTypedArray()
        var currentPoint = grid.findStartingPoint()
        var currentDirection = NORTH
        grid[currentPoint.row][currentPoint.column] = MARKED

        while (grid.isInBounds(
                Point2D(
                    currentPoint.row + currentDirection.row,
                    currentPoint.column + currentDirection.column
                )
            )
        ) {
            val next = Point2D(currentPoint.row + currentDirection.row, currentPoint.column + currentDirection.column)
            // See on operator fun in Extensions
            when (grid[next]) {
                OBSTACLE -> {
                    currentDirection = turnRight(currentDirection)
                }
                else -> {
                    grid[next] = MARKED
                    currentPoint = next
                }
            }
        }

        return grid.countVisited()
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toCharArray() }.toTypedArray()
        val currentPoint = grid.findStartingPoint()
        var validNewObstacleCount = 0
        for (r in grid.indices) {
            for (c in grid[r].indices) {
                if (grid[r][c] == EMPTY) {
                    if (tryPathWithObstacle(grid, currentPoint, Point2D(r, c))) {
                        validNewObstacleCount++
                    }
                }
            }
        }
        return validNewObstacleCount
    }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

object Day06 {
    const val OBSTACLE = '#'
    const val MARKED = 'X'
    const val EMPTY = '.'
    const val STARTER = '^'
}

internal fun Array<CharArray>.findStartingPoint(): Point2D {
    for (r in this.indices) {
        for (c in this[0].indices) {
            if (this[r][c] == STARTER) {
                return Point2D(r, c)
            }
        }
    }
    return Point2D(-1, -1)
}

internal fun Array<CharArray>.countVisited(): Int {
    var count = 0
    for (r in this.indices) {
        for (c in this[0].indices) {
            if (this[r][c] == MARKED) {
                count++
            }
        }
    }
    return count
}

internal fun turnRight(dir: Point2D) = when (dir) {
    NORTH -> EAST
    EAST -> SOUTH
    SOUTH -> WEST
    else -> NORTH
}

internal fun tryPathWithObstacle(grid: Array<CharArray>, start: Point2D, obstaclePosition: Point2D): Boolean {
    if (obstaclePosition == start) {
        return false
    }

    grid[obstaclePosition] = OBSTACLE
    var currentState = State(start, NORTH)
    val visited: HashSet<State> = HashSet()
    visited.add(currentState)

    while (true) {
        val nextPosition: Point2D = currentState.position + currentState.direction
        if (!grid.isInBounds(nextPosition)) {
            grid[obstaclePosition] = EMPTY
            return false
        }

        currentState = if (grid[nextPosition] == OBSTACLE) {
            State(currentState.position, turnRight(currentState.direction))
        } else {
            State(nextPosition, currentState.direction)
        }

        if (!visited.add(currentState)) {
            grid[obstaclePosition] = EMPTY
            return true
        }
    }
}

internal data class State(val position: Point2D, val direction: Point2D)

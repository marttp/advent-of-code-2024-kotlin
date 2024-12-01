package helper.point

import pm
import kotlin.math.abs

data class Point2D(val row: Int, val column: Int) {

    fun getCardinalNeighbors(): List<Point2D> = FOUR_DIRECTIONS.map { this + it }

    fun distance(other: Point2D): Int =
        abs(row - other.row) + abs(column - other.column)

    operator fun minus(other: Point2D): Point2D =
        Point2D(row - other.row, column - other.column)

    operator fun plus(other: Point2D): Point2D =
        Point2D(row + other.row, column + other.column)

    operator fun times(other: Int): Point2D =
        Point2D(row * other, column * other)

    operator fun rem(other: Point2D) = Point2D(row pm other.row, column pm other.column)

    companion object {
        val NORTH = Point2D(-1, 0)
        val EAST = Point2D(0, 1)
        val SOUTH = Point2D(1, 0)
        val WEST = Point2D(0, -1)

        val NORTH_WEST = NORTH + WEST
        val NORTH_EAST = NORTH + EAST
        val SOUTH_WEST = SOUTH + WEST
        val SOUTH_EAST = SOUTH + EAST

        val FOUR_DIRECTIONS = setOf(NORTH, EAST, SOUTH, WEST)

        val EIGHT_DIRECTIONS = setOf(
            NORTH, EAST, SOUTH, WEST,
            NORTH_WEST, NORTH_EAST, SOUTH_WEST, SOUTH_EAST
        )
    }

}

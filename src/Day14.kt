import helper.util.println
import helper.util.readInput
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {

    fun part1(input: List<String>): Long = solution(input, 103, 101, 100)

    fun part2(input: List<String>) {
        val (robotPosition, robotVelocity, _, _) = Day14Information(input, 103, 101)
        moveRobotsForDuration(
            seconds = 10000,
            robotPosition = robotPosition,
            robotVelocity = robotVelocity,
            tall = 103,
            wide = 101,
            snapshot = true
        )
    }

    val input = readInput("Day14")
    part1(input).println()
    part2(input)
}

fun solution(input: List<String>, tall: Long, wide: Long, seconds: Long): Long {
    val (robotPosition, robotVelocity, centerY, centerX) = Day14Information(input, tall, wide)
    val robotStateOnThatTime = moveRobotsForDuration(
        seconds = seconds,
        robotPosition = robotPosition,
        robotVelocity = robotVelocity,
        wide = wide,
        tall = tall
    )
    return countQuadrantSkipCenter(robotStateOnThatTime, centerX, centerY)
}

private fun countQuadrantSkipCenter(
    robotStateOnThatTime: MutableMap<Int, Pair<Int, Int>>,
    centerX: Int,
    centerY: Int
): Long {
    var q1Count = 0L
    var q2Count = 0L
    var q3Count = 0L
    var q4Count = 0L
    for ((px, py) in robotStateOnThatTime.values) {
        if (px != centerX && py != centerY) {
            when {
                px < centerX && py < centerY -> q2Count++
                px < centerX && py > centerY -> q3Count++
                px > centerX && py < centerY -> q1Count++
                else -> q4Count++
            }
        }
    }
    println("Q1: $q1Count, Q2: $q2Count, Q3: $q3Count, Q4: $q4Count")
    return q1Count * q2Count * q3Count * q4Count
}

private fun moveRobotsForDuration(
    seconds: Long,
    robotPosition: MutableMap<Int, Pair<Int, Int>>,
    robotVelocity: MutableMap<Int, Pair<Long, Long>>,
    wide: Long,
    tall: Long,
    snapshot: Boolean = false
): MutableMap<Int, Pair<Int, Int>> {
    val currentState = robotPosition.toMutableMap()
    var current = 0L
    while (current++ < seconds) {
        val newRobotPosition = mutableMapOf<Int, Pair<Int, Int>>()
        for ((id, pos) in currentState) {
            val (px, py) = pos
            val (vx, vy) = robotVelocity[id]!!
            var nx = px.toLong() + vx
            if (nx < 0) {
                val diff = 0L - nx
                nx = wide - diff
            } else if (nx >= wide) {
                nx = nx % wide
            }
            var ny = py.toLong() + vy
            if (ny < 0) {
                val diff = 0L - ny
                ny = tall - diff
            } else if (ny >= tall) {
                ny = ny % tall
            }
            newRobotPosition[id] = Pair(nx.toInt(), ny.toInt())
        }
        currentState.clear()
        currentState.putAll(newRobotPosition)
        if (snapshot) {
            writeSnapshot(currentState, wide, tall, current)
        }
    }
    return currentState
}

internal data class Day14Information(
    var robotPosition: MutableMap<Int, Pair<Int, Int>>,
    var robotVelocity: MutableMap<Int, Pair<Long, Long>>,
    val centerY: Int,
    val centerX: Int
) {

    constructor(input: List<String>, tall: Long, wide: Long) : this(
        mutableMapOf<Int, Pair<Int, Int>>(),
        mutableMapOf<Int, Pair<Long, Long>>(),
        (tall / 2).toInt(),
        (wide / 2).toInt()
    ) {
        val robotPosition = mutableMapOf<Int, Pair<Int, Int>>()
        val robotVelocity = mutableMapOf<Int, Pair<Long, Long>>()
        input.forEachIndexed { id, line ->
            val lineSplit = line.split(" ")
            val position = lineSplit.first()
                .removePrefix("p=")
                .split(",")
            val (px, py) = position.map { it.toLong() }
            robotPosition[id] = Pair(px.toInt(), py.toInt())

            val velocity = lineSplit.last()
                .removePrefix("v=")
                .split(",")
            val (vx, vy) = velocity.map { it.toLong() }
            robotVelocity[id] = Pair(vx, vy)
        }
        this.robotPosition = robotPosition
        this.robotVelocity = robotVelocity
    }
}

internal fun writeSnapshot(robotPosition: Map<Int, Pair<Int, Int>>, wide: Long, tall: Long, seconds: Long) {
    val grid = Array(tall.toInt()) { IntArray(wide.toInt()) }

    for ((_, pos) in robotPosition) {
        val (px, py) = pos
        grid[py][px]++
    }

    val img = BufferedImage(wide.toInt(), tall.toInt(), BufferedImage.TYPE_INT_RGB)

    for (y in 0 until tall.toInt()) {
        for (x in 0 until wide.toInt()) {
            val color = if (grid[y][x] == 0) Color.BLACK else Color.GREEN
            img.setRGB(x, y, color.rgb)
        }
    }

    val filename = "images/frame_%04d.png".format(seconds)
    ImageIO.write(img, "png", File(filename))
}
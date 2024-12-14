import helper.util.println
import helper.util.readInput

fun main() {

    fun part1(input: List<String>): Long {
        val machines = extractMachine(input)
        var coins = 0L

        for (machine in machines) {
            val (ax, ay) = machine.buttonA
            val (bx, by) = machine.buttonB
            val (px, py) = machine.prize

            val timeB = ((py * ax) - (px * ay)).toDouble() / ((by * ax - bx * ay)).toDouble()
            val timeA = (px.toDouble() - (bx.toDouble() * timeB)) / ax.toDouble()

            if (timeA in 0.0..100.0 &&
                timeB in 0.0..100.0 &&
                timeA % 1.0 == 0.0 &&
                timeB % 1.0 == 0.0
            ) {
                coins += (timeA * 3.0 + timeB).toLong()
            }
        }
        return coins
    }

    fun part2(input: List<String>): Long {
        val machines = extractMachine(input)
        var coins = 0L

        for (machine in machines) {
            val (ax, ay) = machine.buttonA
            val (bx, by) = machine.buttonB
            val (tmpX, tmpY) = machine.prize
            val (px, py) = Coordinate(tmpX + 10000000000000, tmpY + 10000000000000)

            val timeB = ((py * ax) - (px * ay)).toDouble() / ((by * ax - bx * ay)).toDouble()
            val timeA = (px.toDouble() - (bx.toDouble() * timeB)) / ax.toDouble()

            if (timeA % 1.0 == 0.0 && timeB % 1.0 == 0.0) {
                coins += (timeA * 3.0 + timeB).toLong()
            }
        }
        return coins
    }

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}

internal data class Coordinate(val x: Long, val y: Long)

internal data class Machine(val buttonA: Coordinate, val buttonB: Coordinate, val prize: Coordinate)

internal fun extractMachine(input: List<String>): List<Machine> {
    val machines = mutableListOf<Machine>()
    val chunkGroup = input.chunked(4)
        .map { it.slice(0 until 3) }

    for (group in chunkGroup) {
        val aParts = group[0].split(": ").last().split(", ")
        val ax = aParts[0].split("+").last().toLong()
        val ay = aParts[1].split("+").last().toLong()

        val bParts = group[1].split(": ").last().split(", ")
        val bx = bParts[0].split("+").last().toLong()
        val by = bParts[1].split("+").last().toLong()

        val pParts = group[2].split(": ").last().split(", ")
        val px = pParts[0].split("=").last().toLong()
        val py = pParts[1].split("=").last().toLong()

        val machine = Machine(
            Coordinate(ax, ay),
            Coordinate(bx, by),
            Coordinate(px, py)
        )
        machines.add(machine)
    }
    return machines
}
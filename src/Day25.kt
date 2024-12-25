import helper.util.println
import helper.util.readInput

fun main() {

    fun buildKeyPair(input: List<String>): List<KeyPair> {
        val keyPairs = buildList {
            var currentGrid = mutableListOf<List<Char>>()

            for (line in input) {
                if (line.isEmpty()) {
                    add(KeyPair.from(currentGrid))
                    currentGrid = mutableListOf()
                } else {
                    currentGrid.add(line.toList())
                }
            }
            if (currentGrid.isNotEmpty()) {
                add(KeyPair.from(currentGrid))
            }
        }
        return keyPairs
    }

    fun part1(input: List<String>): Int {
        val keyPairs = buildKeyPair(input)

        val (locks, keys) = keyPairs.partition { it.category == "Lock" }

        return keys.sumOf { key ->
            locks.count { lock ->
                key.value.zip(lock.availableSlots)
                    .all { (kSlot, lSlot) -> kSlot <= lSlot }
            }
        }
    }

    val input = readInput("Day25")
    part1(input).println()
}

data class KeyPair(
    val category: String,
    val value: List<Int>,
    val availableSlots: List<Int>
) {
    companion object {
        fun from(grid: List<List<Char>>): KeyPair {
            val category = if (isKey(grid)) "Key" else "Lock"
            val maxRow = grid.size - 2
            val value = mutableListOf<Int>()
            val availableSlots = mutableListOf<Int>()

            for (col in grid[0].indices) {
                var amount = 0
                for (row in 1..maxRow) {
                    if (grid[row][col] == '#') amount++
                }
                value.add(amount)
                if (category == "Lock") {
                    availableSlots.add(maxRow - amount)
                }
            }

            return KeyPair(category, value, availableSlots)
        }

        private fun isKey(grid: List<List<Char>>) =
            grid.first().all { it == '.' }
    }
}
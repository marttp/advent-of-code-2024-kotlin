import helper.util.println
import helper.util.readInput

fun main() {

    fun solution(input: List<String>): Pair<Long, Long> {
        val width = input[0].length + 2
        val grid = mutableListOf<Char>()

        // Add Padding all
        grid.addAll(CharArray(width) { '0' }.toList())
        input.forEach { line ->
            grid.add('0')
            grid.addAll(line.toList())
            grid.add('0')
        }
        grid.addAll(CharArray(width) { '0' }.toList())

        val visited = BooleanArray(grid.size) { false }
        val queue = ArrayDeque<Int>()
        var ans1 = 0L
        var ans2 = 0L

        for (index in grid.indices) {
            if (grid[index] == '0' || visited[index]) {
                continue
            }

            var area = 0
            var perimeter = 0
            var sides = 0
            queue.addLast(index)
            visited[index] = true

            while (queue.isNotEmpty()) {
                val curIndex = queue.removeFirst()
                area++

                val neighbors = listOf(
                    curIndex - width,
                    curIndex + 1,
                    curIndex + width,
                    curIndex - 1
                )

                neighbors.forEachIndexed { i, neighbor ->
                    if (grid[neighbor] == grid[curIndex]) {
                        if (!visited[neighbor]) {
                            visited[neighbor] = true
                            queue.addLast(neighbor)
                        }
                    } else {
                        perimeter++
                    }

                    val nextNeighbor = neighbors[(i + 1) % neighbors.size]
                    if (grid[neighbor] != grid[curIndex] && grid[nextNeighbor] != grid[curIndex]) {
                        sides++
                    } else if (grid[neighbor] == grid[curIndex] &&
                        grid[nextNeighbor] == grid[curIndex] &&
                        grid[neighbor + nextNeighbor - curIndex] != grid[curIndex]
                    ) {
                        sides++
                    }
                }
            }

            ans1 += area.toLong() * perimeter
            ans2 += area.toLong() * sides
        }

        return Pair(ans1, ans2)
    }

    val input = readInput("Day12")
    solution(input).println()
}
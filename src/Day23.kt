import helper.util.println
import helper.util.readInput

fun main() {

    fun buildGraph(input: List<String>): HashMap<String, HashSet<String>> {
        val graph = hashMapOf<String, HashSet<String>>()
        for (line in input) {
            val pairs = line.split("-")
            graph.putIfAbsent(pairs.first(), hashSetOf())
            graph.putIfAbsent(pairs.last(), hashSetOf())
            graph[pairs.first()]!!.add(pairs.last())
            graph[pairs.last()]!!.add(pairs.first())
        }
        return graph
    }

    fun part1(input: List<String>): Int {
        val graph = buildGraph(input)
        val triangles = hashSetOf<MutableList<String>>()
        for (n1 in graph.keys) {
            for (n2 in graph[n1]!!) {
                if (n2 == n1) {
                    continue
                }
                for (n3 in graph[n2]!!) {
                    if (n3 == n2) {
                        continue
                    }
                    if (graph[n1]!!.contains(n3)) {
                        val nodes = mutableListOf(n1, n2, n3)
                        nodes.sort()
                        triangles.add(nodes)
                    }
                }
            }
        }
        return triangles.count { it.any { it.startsWith('t') } }
    }

    fun part2(input: List<String>): String {
        val graph = buildGraph(input)
        val maxClique = hashSetOf<String>()

        for (start in graph.keys) {
            val clique = hashSetOf(start)
            val candidates = graph[start]!!.toMutableSet()

            while (candidates.isNotEmpty()) {
                val next = candidates.find { candidate ->
                    clique.all { member -> graph[candidate]!!.contains(member) }
                }

                if (next != null) {
                    clique.add(next)
                    candidates.remove(next)
                } else {
                    break
                }
            }

            if (clique.size > maxClique.size) {
                maxClique.clear()
                maxClique.addAll(clique)
            }
        }

        return maxClique.sorted().joinToString(",")
    }

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}
import helper.util.println
import helper.util.readInput
import java.util.LinkedList

fun main() {

    fun part1(input: List<String>): Int {
        val safelyUpdate = extractRulesUpdates(input)
        val updateRules = safelyUpdate.updates.map {
            it.split(",").map { it.toInt() }
        }
        return updateRules.filter { checkValidUpdate(it, safelyUpdate.ruleMap) }
            .sumOf { it.findMiddleElement() }
    }

    fun part2(input: List<String>): Int {
        val safelyUpdate = extractRulesUpdates(input)
        val updateRules = safelyUpdate.updates.map {
            it.split(",").map { it.toInt() }
        }
        return updateRules.filter { !checkValidUpdate(it, safelyUpdate.ruleMap) }
            .map { it.topologicalSortBfs(safelyUpdate.ruleMap) }
            .sumOf { it.findMiddleElement() }
    }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

internal fun extractRulesUpdates(input: List<String>): SafelyUpdate {
    val rules = mutableListOf<String>()
    val updates = mutableListOf<String>()
    var isReceivedUpdates = false
    for (line in input) {
        if (line.isBlank()) {
            isReceivedUpdates = true
        } else if (isReceivedUpdates) {
            updates.add(line)
        } else {
            rules.add(line)
        }
    }
    val ruleMap = mutableMapOf<Int, MutableSet<Int>>()
    for (rule in rules) {
        val orderRule = rule.split("|").map { it.toInt() }
        ruleMap.putIfAbsent(orderRule[0], mutableSetOf())
        ruleMap[orderRule[0]]!!.add(orderRule[1])
    }
    return SafelyUpdate(rules, updates, ruleMap)
}

internal fun checkValidUpdate(update: List<Int>, ruleMap: Map<Int, Set<Int>>): Boolean {
    for (i in update.indices) {
        ruleMap[update[i]]?.let {
            for (j in 0 until i) {
                if (it.contains(update[j])) {
                    return false
                }
            }
        }
    }
    return true
}

internal fun List<Int>.topologicalSortBfs(ruleMap: Map<Int, Set<Int>>): List<Int> {
    val inDegree = mutableMapOf<Int, Int>()
    for (n in this) {
        inDegree[n] = 0
    }

    val set = this.toSet()
    for (n in this) {
        ruleMap[n]?.let {
            it.filter { set.contains(it) }
                .forEach { inDegree[it] = inDegree[it]!! + 1 }
        }
    }

    val queue: LinkedList<Int> = LinkedList()
    for (entry in inDegree.entries) {
        if (entry.value == 0) {
            queue.add(entry.key)
        }
    }
    val result = mutableListOf<Int>()
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        result.add(current)
        ruleMap[current]?.let { ruleSet ->
            for (r in ruleSet) {
                if (inDegree.containsKey(r)) {
                    inDegree[r] = inDegree[r]!! - 1
                    if (inDegree[r]!! == 0) {
                        queue.add(r)
                    }
                }
            }
        }
    }
    return result
}

internal fun List<Int>.findMiddleElement() = this[this.size / 2]

internal data class SafelyUpdate(
    val rules: List<String>,
    val updates: List<String>,
    val ruleMap: Map<Int, Set<Int>>
)
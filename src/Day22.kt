import helper.util.println
import helper.util.readInput
import kotlinx.coroutines.*
import kotlin.math.floor
import kotlin.math.roundToLong

fun main() {
    val input = readInput("Day22")
    runBlocking {
        part1(input).println()
        part2(input).println()
    }
}

suspend fun part1(input: List<String>): Long =
    input.map { it.toLong() }
        .pmap { produceSecret(it, 2000) }
        .sum()

suspend fun part2(input: List<String>): Long {
    val initialSecrets = input.map { it.toLong() }
    val allPriceSequences = initialSecrets.pmap { generatePriceSequence(it) }
    val allChanges = allPriceSequences.pmap { prices ->
        prices.windowed(2).map { (a, b) -> b.toInt() - a.toInt() }
    }

    return (-9..9).asSequence().flatMap { a ->
        (-9..9).map { b -> a to b }
    }.flatMap { (a, b) ->
        (-9..9).map { c -> Triple(a, b, c) }
    }.flatMap { (a, b, c) ->
        (-9..9).map { d -> listOf(a, b, c, d) }
    }.pmap { sequence ->
        calculateTotalBananas(allChanges, allPriceSequences, sequence)
    }.maxOrNull() ?: 0
}

fun produceSecret(secretInput: Long, target: Int): Long {
    var secret = secretInput
    repeat(target) {
        secret = produceNextSecret(secret)
    }
    return secret
}

suspend fun <A, B> Sequence<A>.pmap(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    block: suspend (A) -> B
): List<B> = coroutineScope {
    map { async(dispatcher) { block(it) } }.toList().awaitAll()
}

suspend fun <A, B> List<A>.pmap(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    block: suspend (A) -> B
): List<B> = coroutineScope {
    map { async(dispatcher) { block(it) } }.awaitAll()
}

fun generatePriceSequence(initialSecret: Long): List<Long> {
    val prices = mutableListOf(initialSecret % 10)
    var secret = initialSecret

    repeat(2000) {
        secret = produceNextSecret(secret)
        prices.add(secret % 10)
    }
    return prices
}

fun calculateTotalBananas(
    allChanges: List<List<Int>>,
    allPriceSequences: List<List<Long>>,
    targetSequence: List<Int>
): Long = allChanges.zip(allPriceSequences) { changes, prices ->
    changes.indices.asSequence()
        .take(changes.size - targetSequence.size + 1)
        .firstOrNull { i ->
            changes.subList(i, i + targetSequence.size) == targetSequence
        }?.let { i ->
            prices[i + targetSequence.size]
        } ?: 0
}.sum()

fun produceNextSecret(secret: Long): Long {
    var result = prune(mix(secret * 64, secret))
    result = prune(mix(floor(result.toDouble() / 32.0).toLong(), result))
    result = prune(mix(result * 2048, result))
    return result
}

fun mix(value: Long, secret: Long): Long = value xor secret

fun prune(secret: Long): Long = secret % 16777216
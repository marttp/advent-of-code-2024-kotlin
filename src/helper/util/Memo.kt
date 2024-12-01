package helper.util

class Memo<T, R>(private val fn: Memo<T, R>.(T) -> R) {
    private val cache = mutableMapOf<T, R>()

    operator fun invoke(param: T): R {
        return cache.getOrPut(param) { fn(param) }
    }
}
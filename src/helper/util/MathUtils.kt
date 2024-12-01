package helper.util

object MathUtils {
    fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

    fun lcm(a: Long, b: Long): Long = (a * b) / gcd(a, b)

}
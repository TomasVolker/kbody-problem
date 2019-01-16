package tomasvolker.kbodyproblem.math

import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector3
import kotlin.random.Random

val javaRandom = java.util.Random()

// Not reproducible
fun Random.nextGaussian() = javaRandom.nextGaussian()

fun Random.nextGaussianVector(
    stdX: Double = 1.0,
    stdY: Double = 1.0,
    stdZ: Double = 1.0
) = Vector3(
    stdX * nextGaussian(),
    stdY * nextGaussian(),
    stdZ * nextGaussian()
)

fun Random.nextDirection() =
    Vector3(nextGaussian(), nextGaussian(), nextGaussian()).normalized

fun Random.nextColor() =
    ColorRGBa(nextDouble(), nextDouble(), nextDouble())

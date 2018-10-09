package tomasvolker.vector

import java.lang.Math.cbrt
import kotlin.math.*

fun Double.squared() = this * this

fun sphereVolume(radius: Double) = 4.0 / 3.0 * PI * radius.pow(3)

fun sphereRadius(volume: Double) = cbrt(volume * 3.0 / (4.0 * PI))

data class Vector3(
        val x: Double,
        val y: Double,
        val z: Double
) {

    fun norm(): Double = sqrt(x * x + y * y + z * z)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = elementWise { -it }

    operator fun times(other: Double): Vector3 = elementWise { it * other }

    operator fun div(other: Double): Vector3 = elementWise { it / other }

    operator fun minus(other: Vector3): Vector3 = elementWise(other) { t, o ->  t - o}

    operator fun plus(other: Vector3): Vector3 = elementWise(other) { t, o ->  t + o}

    infix fun inner(other: Vector3): Double =
            this.x * other.x + this.y * other.y + this.z * other.z

    infix fun cross(other: Vector3): Vector3 =
            Vector3(
                    x = this.y * other.z - this.z * other.y,
                    y = this.z * other.x - this.x * other.z,
                    z = this.x * other.y - this.y * other.x
            )

    inline fun elementWise(function: (Double) -> Double) =
            Vector3(function(x), function(y), function(z))

    inline fun elementWise(other: Vector3, function: (Double, Double) -> Double) =
            Vector3(function(this.x, other.x), function(this.y, other.y), function(this.z, other.z))

    override fun toString() = "[%.4f, %.4f, %.4f]".format(x, y, z)

}

fun Vector3.rotateXY(angle: Double) =
        Vector3(this.x * cos(angle) - this.y * sin(angle), this.x * sin(angle) + this.y * cos(angle), this.z)

operator fun Double.times(vector2: Vector3) = vector2 * this

fun Vector3.normSquared() = x * x + y * y + z * z

infix fun Vector3.longerThan(value: Number): Boolean =
        this.normSquared() > value.toDouble().squared()

infix fun Vector3.shorterThan(value: Number): Boolean =
        this.normSquared() < value.toDouble().squared()

fun Vector3.normalized() = this / this.norm()

object v3 {

    operator fun get(x: Double, y: Double, z: Double): Vector3 =
            Vector3(x, y, z)

}

val V3_ZERO = v3[0.0, 0.0, 0.0]
val ORIGIN = V3_ZERO
val VERSOR_X = v3[1.0, 0.0, 0.0]
val VERSOR_Y = v3[0.0, 1.0, 0.0]
val VERSOR_Z = v3[0.0, 0.0, 1.0]

public inline fun <T> Iterable<T>.sumByVector3(selector: (T) -> Vector3): Vector3 {
    var sum: Vector3 = V3_ZERO
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

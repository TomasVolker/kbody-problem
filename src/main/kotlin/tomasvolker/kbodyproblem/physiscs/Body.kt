package tomasvolker.kbodyproblem.physiscs

import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector3
import org.openrndr.math.times
import tomasvolker.kbodyproblem.math.*
import kotlin.random.Random

class Body(
    var position: Vector3 = Vector3.ZERO,
    var velocity: Vector3 = Vector3.ZERO,
    var acceleration: Vector3 = Vector3.ZERO,
    var mass: Double = 1.0,
    var radius: Double = 1.0,
    var color: ColorRGBa = Random.nextColor()
) {

    val speed: Double
            get() = velocity.norm()

    val momentum: Vector3 get() = velocity * mass

    var force: Vector3
        get() = acceleration * mass
        set(value) {
            acceleration = value / mass
        }

    var diameter: Double
        get() = 2 * radius
        set(value) { radius = value / 2 }

    val volume: Double
        get() = sphereVolume(radius)

    val density: Double
        get() = mass / volume

    val kineticEnergy: Double get() = 0.5 * mass * velocity.normSquared()

    fun angularMomentum(reference: Vector3 = Vector3.ZERO): Vector3 =
           (position - reference) cross  momentum

    fun applyForce(force: Vector3) {
        this.force += force
    }

    fun step(time: Double) {
        velocity += time * acceleration
        position += time * velocity
        acceleration = Vector3.ZERO
    }

}

fun distanceBetween(body1: Body, body2: Body): Double =
    (body2.position - body1.position).norm()

fun relativeVelocity(body: Body, reference: Body): Vector3 =
    (body.velocity - reference.velocity)

fun areColliding(body1: Body, body2: Body): Boolean =
    (body2.position - body1.position).shorterThan(body1.radius + body2.radius)


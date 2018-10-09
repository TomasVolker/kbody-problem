package tomasvolker.physiscs.interactions

import tomasvolker.vector.normSquared
import tomasvolker.vector.normalized
import tomasvolker.physiscs.Body

val G: Double = 10.0

class GravitationalPull(
    body1: Body,
    body2: Body,
    val g: Double = G
): BodyInteraction(body1, body2) {

    override fun applyForces() {

        val delta = deltaPosition
        val force = delta.normalized() * g * body1.mass * body2.mass / delta.normSquared()

        body1.applyForce(force)
        body2.applyForce(-force)

    }

    override val potentialEnergy: Double
        get() = - g * body1.mass * body2.mass / distance

}
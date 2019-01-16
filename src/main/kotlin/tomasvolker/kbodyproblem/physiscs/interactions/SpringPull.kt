package tomasvolker.kbodyproblem.physiscs.interactions

import tomasvolker.kbodyproblem.math.normalized
import tomasvolker.kbodyproblem.math.squared
import tomasvolker.kbodyproblem.physiscs.Body

class SpringPull(
    body1: Body,
    body2: Body,
    val l0: Double,
    val k: Double = 1e-2
): BodyInteraction(body1, body2) {

    override fun applyForces() {

        val delta = deltaPosition
        val force = (delta - delta.normalized() * l0) * k

        body1.applyForce(force)
        body2.applyForce(-force)

    }

    override val potentialEnergy: Double
        get() = 0.5 * k * (distance - l0).squared()

}
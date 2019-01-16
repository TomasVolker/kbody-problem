package tomasvolker.kbodyproblem.physiscs.interactions

import org.openrndr.math.Vector3
import tomasvolker.kbodyproblem.math.norm
import tomasvolker.kbodyproblem.physiscs.Body

abstract class BodyInteraction(
    val body1: Body,
    val body2: Body
) {

    fun affects(body: Body) = body == body1 || body == body2

    val deltaPosition: Vector3
        get() =
        body2.position - body1.position

    val distance: Double get() =
        deltaPosition.norm()

    abstract fun applyForces()

    abstract val potentialEnergy: Double

}
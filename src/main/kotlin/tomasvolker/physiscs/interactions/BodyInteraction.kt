package tomasvolker.physiscs.interactions

import tomasvolker.vector.Vector3
import tomasvolker.physiscs.Body

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
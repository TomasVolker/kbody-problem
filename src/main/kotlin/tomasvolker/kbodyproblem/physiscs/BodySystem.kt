package tomasvolker.kbodyproblem.physiscs

import org.openrndr.math.Vector3
import tomasvolker.kbodyproblem.forAllPairs
import tomasvolker.kbodyproblem.physiscs.interactions.BodyInteraction
import tomasvolker.kbodyproblem.physiscs.interactions.GravitationalPull
import tomasvolker.kbodyproblem.math.*

class BodySystem(
    bodies: Iterable<Body> = emptyList(),
    interactions: Iterable<BodyInteraction> = emptyList()
) {

    private val mutableBodyList = bodies.distinct().toMutableList()
    private val mutableInteractionList = interactions.distinct().toMutableList()

    val bodyList: List<Body>
        get() = mutableBodyList

    val interactionList: List<BodyInteraction>
        get() = mutableInteractionList

    fun addBody(body: Body) {
        mutableBodyList.add(body)
    }

    fun addInteraction(interaction: BodyInteraction) {
        mutableInteractionList.add(interaction)
    }

    fun removeInteraction(interaction: BodyInteraction) {
        mutableInteractionList.remove(interaction)
    }

    fun step(time: Double): Boolean {

        bodyList.forEach { it.step(time) }

        val collision = processCollisions()

        interactionList.forEach { it.applyForces() }

        return collision
    }

    private fun processCollisions(): Boolean {

        var collision = false

        while (true) {

            val bodies = searchCollision()?.toList() ?: return collision
            collision = true

            mutableBodyList.removeAll(bodies)
            mutableInteractionList.removeAll {
                it.affects(bodies[0]) || it.affects(bodies[1])
            }

            val newBody = Body(
                position = bodies.centerOfMassPosition(),
                velocity = bodies.centerOfMassVelocity(),
                mass = bodies.totalMass(),
                radius = sphereRadius(volume = bodies.sumByDouble { it.volume })
            )

            bodyList.forEach {
                addInteraction(GravitationalPull(newBody, it))
            }

            addBody(newBody)
        }

    }

    private fun searchCollision(): Pair<Body, Body>? {

        bodyList.forAllPairs { body1, body2 ->

            if (areColliding(body1, body2)) {
                return body1 to body2
            }

        }

        return null
    }

    fun totalMass() = bodyList.totalMass()

    fun centerOfMassPosition() =
        bodyList.centerOfMassPosition()

    fun centerOfMassVelocity() = bodyList.centerOfMassVelocity()

    fun momentum() =
        bodyList.momentum()

    fun angularMomentum(reference: Vector3 = Vector3.ZERO) =
        bodyList.angularMomentum(reference)

    fun totalEnergy() = kineticEnergy() + potentialEnergy()

    fun kineticEnergy() =
        bodyList.kineticEnergy()

    fun potentialEnergy() =
        interactionList.sumByDouble { it.potentialEnergy }

}

inline fun bodySystem(init: BodySystem.()->Unit) = BodySystem().apply(init)

inline fun BodySystem.body(init: Body.()->Unit) = addBody(Body().apply(init))

fun BodySystem.interaction(interaction: BodyInteraction) = addInteraction(interaction)

fun Iterable<Body>.totalMass() = sumByDouble { it.mass }

fun Iterable<Body>.centerOfMassPosition() =
        sumByVector3 { it.position * it.mass } / totalMass()

fun Iterable<Body>.centerOfMassVelocity() =
        momentum() / totalMass()

fun Iterable<Body>.momentum() =
        sumByVector3 { it.momentum }

fun Iterable<Body>.angularMomentum(reference: Vector3 = ORIGIN) =
        sumByVector3 { it.angularMomentum(reference) }

fun Iterable<Body>.kineticEnergy() =
        sumByDouble { it.kineticEnergy }

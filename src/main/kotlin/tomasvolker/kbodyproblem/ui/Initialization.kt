package tomasvolker.kbodyproblem.ui

import org.openrndr.math.Vector3
import org.openrndr.math.times
import tomasvolker.kbodyproblem.forAllPairs
import tomasvolker.kbodyproblem.math.*
import tomasvolker.kbodyproblem.physiscs.body
import tomasvolker.kbodyproblem.physiscs.bodySystem
import tomasvolker.kbodyproblem.physiscs.interaction
import tomasvolker.kbodyproblem.physiscs.interactions.GravitationalPull
import kotlin.math.sqrt
import kotlin.random.Random

fun Random.nextPosition(): Vector3 {

    var x: Vector3

    do {
        x = nextGaussianVector(40.0, 12.0, 40.0)
    } while (x shorterThan  12.0)

    return x
}

fun buildBodySystem() = bodySystem {

    val bodyCount = 100
    val smallBodySpeed = 120.0
    val diameterFactor = 0.1

    repeat(bodyCount) {

        body {
            position = Random.nextPosition()

            mass = 10.0

            velocity = smallBodySpeed * position.normalized().rotate(Vector3.UNIT_Z, 90.0) / sqrt(position.norm())

            radius = diameterFactor * Math.cbrt(mass)
        }

    }

    val bigBodyDistanceFromCenter = 5.0
    val bigBodySpeed = 20.0

    body {
        position = bigBodyDistanceFromCenter * VERSOR_X
        velocity = bigBodySpeed * VERSOR_Z
        mass = 1200.0
        radius = diameterFactor * Math.cbrt(mass)
    }

    body {
        position = -bigBodyDistanceFromCenter * VERSOR_X
        velocity = -bigBodySpeed * VERSOR_Z
        mass = 1200.0
        radius = diameterFactor * Math.cbrt(mass)
    }

    bodyList.forAllPairs { body1, body2 ->
        interaction(GravitationalPull(body1, body2))
    }

}



package tomasvolker

import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.paint.Color
import tomasvolker.physiscs.*
import tomasvolker.physiscs.interactions.BodyInteraction
import tomasvolker.physiscs.interactions.GravitationalPull
import tomasvolker.render.BodyDrawer
import tomasvolker.vector.*
import tornadofx.App
import tornadofx.launch
import java.lang.Math.cbrt
import java.util.*
import kotlin.math.PI
import kotlin.math.sqrt

fun main(args: Array<String>) {
    launch<DemoApp>(args)
}

class DemoApp : App(DemoView::class)

class DemoView: CanvasView<BodySystem>() {

    override val canvasWidth: Double get() = 1600.0

    override val canvasHeight: Double get() = 1000.0

    private val centerPosition = v3[canvasWidth / 2, canvasHeight / 2, 50.0]

    private var restart = false
    private var drawSpeed = false
    private var drawAccelerations = false

    private val drawer = BodyDrawer()

    override val delayMillis: Long
        get() = 10

    override fun initialState(): BodySystem = buildBodySystem()

    override fun initialize() {

        root.scene.setOnKeyPressed(::onKeyPressed)

    }

    fun onKeyPressed(event: KeyEvent) {

        when(event.code) {
            KeyCode.SPACE -> restart = true
            KeyCode.V -> drawSpeed = !drawSpeed
            KeyCode.A -> drawAccelerations = !drawAccelerations
        }

    }

    override fun update(state: BodySystem): BodySystem {
        return if (restart) {
            restart = false
             buildBodySystem()
        } else {
            state.apply { step(1e-1) }
        }
    }

    override fun draw(graphicsContext: GraphicsContext, state: BodySystem) {

        with(graphicsContext) {

            fill = Color.BLACK
            stroke = Color.BLACK
            lineWidth = 1.0

            drawer.drawSpeed = drawSpeed
            drawer.drawAccelerations = drawAccelerations

            for (body in state.bodyList) {

                val position = body.position

                if (isVisible(position.x, position.y, body.radius)) {

                    save()

                    translate(
                            canvasWidth / 2 + position.x,
                            canvasHeight / 2 + position.y
                    )

                    drawer.draw(body, graphicsContext)

                    restore()
                }

            }

            fillText("Center of Mass Position: ${state.centerOfMassPosition()}", 10.0, 40.0)
            fillText("Center of Mass Velocity: ${state.centerOfMassVelocity()}", 10.0, 60.0)
            fillText("Energy: ${state.totalEnergy()}", 10.0, 80.0)
            fillText("Momentum: ${state.momentum()}", 10.0, 100.0)
            fillText("Angular Momentum: ${state.angularMomentum(centerPosition)}", 10.0, 120.0)
            fillText("Body count: ${state.bodyList.size}", 10.0, 140.0)

        }

    }

    private fun isVisible(x: Double, y: Double, radius: Double) =
            (canvasWidth  / 2 + x) in (-radius)..(canvasWidth+radius) &&
            (canvasHeight / 2 + y) in (-radius)..(canvasHeight+radius)

}

fun Random.nextDouble(lower: Double, upper: Double) =
        lower + (upper - lower) * nextDouble()

fun Random.nextVector(std: Double) =
        v3[
        std * nextGaussian(),
        std * nextGaussian(),
        0.01 * std * nextGaussian()
        ]

fun buildBodySystem(): BodySystem = bodySystem {

    val bodyCount = 200
    val smallBodySpeed = 400.0
    val diameterFactor = 1.0

    val random = Random()

    fun nextPosition(): Vector3 {

        var x: Vector3

        do {
            x = random.nextVector(200.0)
        } while (x shorterThan  120.0)

        return x
    }

    repeat(bodyCount) {

        body {
            position = nextPosition()

            mass = 10.0

            velocity = - smallBodySpeed * position.normalized().rotateXY(PI /2) / sqrt(position.norm())

            radius = diameterFactor * cbrt(mass)
        }

    }

    val bigBodyDistanceFromCenter = 50.0
    val bigBodySpeed = 22.0

    body {
        position = bigBodyDistanceFromCenter * VERSOR_X
        velocity = bigBodySpeed * VERSOR_Y
        mass = 10000.0
        radius = diameterFactor * cbrt(mass)
    }

    body {
        position = -bigBodyDistanceFromCenter * VERSOR_X
        velocity = -bigBodySpeed * VERSOR_Y
        mass = 10000.0
        radius = diameterFactor * cbrt(mass)
    }

    bodyList.forAllPairs { body1, body2 ->
        interaction(GravitationalPull(body1, body2))
    }

}



package tomasvolker

import javafx.animation.Timeline
import javafx.scene.canvas.GraphicsContext
import javafx.util.Duration
import tornadofx.*


abstract class CanvasView<S : Any> : View() {

    abstract val canvasWidth: Double
    abstract val canvasHeight: Double

    private lateinit var graphicsContext: GraphicsContext

    open val delayMillis: Long get() = 50

    private lateinit var state: S

    override val root = stackpane {

        canvas(canvasWidth, canvasHeight) {
            graphicsContext = graphicsContext2D
        }

    }

    open fun initialize() {}

    abstract fun initialState() : S

    override fun onDock() {

        state = initialState()

        initialize()

        timeline {

            keyframe(Duration.millis(delayMillis.toDouble())) {

                setOnFinished {

                    state = update(state)
                    clearCanvas()
                    draw(graphicsContext, state)

                }

            }

            cycleCount = Timeline.INDEFINITE

        }

    }

    private fun clearCanvas() {
        graphicsContext.clearRect(
                0.0,
                0.0,
                canvasWidth,
                canvasHeight
        )
    }

    abstract fun update(state: S): S

    abstract fun draw(graphicsContext: GraphicsContext, state: S)

}
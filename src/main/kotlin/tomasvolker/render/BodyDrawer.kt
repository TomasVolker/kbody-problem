package tomasvolker.render

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import tomasvolker.physiscs.Body

interface ObjectDrawer<T: Any> {
    fun draw(element: T, graphicsContext: GraphicsContext)
}

class BodyDrawer: ObjectDrawer<Body> {

    val velocityLineFactor = 1.0
    val accelerationLineFactor = 10000.0

    var drawSpeed: Boolean = false
    var drawAccelerations: Boolean = false

    override fun draw(element: Body, graphicsContext: GraphicsContext) {

        with(graphicsContext) {
            save()

            val diameter = element.diameter
            val velocity = element.velocity
            val acceleration = element.acceleration


            fillOval(
                    -diameter / 2,
                    -diameter / 2,
                    diameter,
                    diameter
            )

            if (drawSpeed) {
                stroke = Color.BLUE

                strokeLine(
                        0.0,
                        0.0,
                        velocityLineFactor * velocity.x,
                        velocityLineFactor * velocity.y
                )
            }

            if (drawAccelerations) {
                stroke = Color.RED

                strokeLine(
                        0.0,
                        0.0,
                        accelerationLineFactor * acceleration.x,
                        accelerationLineFactor * acceleration.y
                )
            }

            restore()
        }

    }

}
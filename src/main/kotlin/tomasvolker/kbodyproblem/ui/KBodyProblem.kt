package tomasvolker.kbodyproblem.ui

import org.openrndr.ExtensionStage
import org.openrndr.Program
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.configuration
import org.openrndr.dnky.*
import org.openrndr.draw.Drawer
import org.openrndr.draw.FontImageMap
import org.openrndr.draw.isolated
import org.openrndr.extensions.Debug3D
import org.openrndr.extras.meshgenerators.sphereMesh
import org.openrndr.ffmpeg.ScreenRecorder
import org.openrndr.math.Matrix44
import org.openrndr.math.Vector3
import org.openrndr.math.transforms.translate
import tomasvolker.kbodyproblem.physiscs.BodySystem
import tomasvolker.kbodyproblem.resourceUrl

fun main() = application(
    configuration = configuration {
        title = "K-Body Problem"
        width = 1200
        height = 800
        windowResizable = true
    },
    program =  KBodyProblem(
        bodySystem = buildBodySystem()
    )
)

class KBodyProblem(
    val bodySystem: BodySystem
): Program() {

    private lateinit var scene: Scene
    private lateinit var font: FontImageMap

    override fun setup() {

        font = FontImageMap.fromUrl(
            fontUrl = resourceUrl<KBodyProblem>("IBMPlexMono-Bold.ttf"),
            size = 16.0
        )

        //extend(ScreenRecorder())

        extend(
            Debug3D(
                eye = Vector3(0.0, 100.0, 0.0),
                lookAt = Vector3.ZERO
            )
        )

        scene = scene {
            populate(bodySystem)
        }

        extend(DNKY()) {
            scene = this@KBodyProblem.scene
        }

        extend { update() }

        extend(ExtensionStage.AFTER_DRAW) {
            printMagnitudes(drawer)
        }

    }

    fun update() {

        if(bodySystem.step(0.01)) {
            scene.root.children.clear()
            scene.updateFunctions.clear()
            scene.populate(bodySystem)
        }

    }

    fun printMagnitudes(drawer: Drawer) {
        drawer.isolated {
            view = Matrix44.IDENTITY
            ortho()

            fontMap = font
            fill = ColorRGBa.WHITE

            translate(16.0, 16.0)

            printVariables(
                mapOf(
                    "Total mass" to bodySystem.totalMass(),
                    "Center of Mass position" to bodySystem.centerOfMassPosition(),
                    "Center of Mass velocity" to bodySystem.centerOfMassVelocity(),
                    "Momentum" to bodySystem.momentum(),
                    "Angular Momentum" to bodySystem.angularMomentum(),
                    "Potential Energy" to bodySystem.potentialEnergy(),
                    "Kinetic Energy" to bodySystem.kineticEnergy(),
                    "Total Energy" to bodySystem.totalEnergy()
                )
            )

        }
    }

    fun Drawer.printVariables(values: Map<String, Any>) {

        values.forEach { name, value ->

            val string = when(value) {
                is Double -> "%g".format(value)
                is Vector3 -> "[%g, %g, %g]".format(value.x, value.y, value.z)
                else -> toString()
            }

            text("%s: %s".format(name, string))
            translate(0.0, 16.0)
        }


    }

    private fun Scene.populate(bodySystem: BodySystem) {

        node {

            directionalLight {
                direction = org.openrndr.math.Vector3(-1.0, -1.0, -1.0)
                color = ColorRGBa.WHITE.shade(0.6)
            }

        }

        bodySystem.bodyList.forEach { body ->

            node {

                update {
                    transform = translate(body.position.x, body.position.y, body.position.z)
                }

                mesh {
                    geometry = geometry(sphereMesh(16, 16, body.radius))

                    basicMaterial {
                        val color = body.color
                        diffuse = color
                        emissive = color.shade(0.2)
                        specular = ColorRGBa.WHITE
                    }

                }
            }

        }

    }

}




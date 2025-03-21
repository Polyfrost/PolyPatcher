package club.sk1er.patcher.render

import club.sk1er.patcher.config.PatcherConfig
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.universal.UMatrixStack
import org.polyfrost.oneconfig.api.event.v1.events.HudRenderEvent
import org.polyfrost.oneconfig.api.event.v1.invoke.impl.Subscribe
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.concurrent.CompletableFuture

object ScreenshotPreview {

    private var currentWindow: Window? = null

     @Subscribe
     fun renderScreenshot(event: HudRenderEvent) {
         this.currentWindow?.draw(UMatrixStack.Compat.get())
     }

    fun newCapture(image: BufferedImage) {
        this.currentWindow = Window(ElementaVersion.V2)
        this.instantiateComponents(image)
    }

    private fun instantiateComponents(image: BufferedImage) {
        val imageComponent = UIImage(CompletableFuture.completedFuture(image)).constrain {
            x = 0.pixels()
            y = 0.pixels()
            width = RelativeConstraint(1.0F)
            height = RelativeConstraint(1.0F)
        } childOf currentWindow!!

        val style = PatcherConfig.previewAnimationStyle
        val scale = PatcherConfig.previewScale / 5
        val strategy = Animations.OUT_QUINT
        val time = 1.5F
        when (style) {
            0 -> {
                imageComponent.animate {
                    setWidthAnimation(strategy, time, RelativeConstraint(scale))
                    setHeightAnimation(strategy, time, RelativeConstraint(scale))
                    setXAnimation(strategy, time, 10.pixels(true))
                    setYAnimation(strategy, time, 10.pixels(true))
                    onComplete { slideOutAnimation(imageComponent) }
                }
            }

            1 -> {
                imageComponent.constrain {
                    width = RelativeConstraint(scale)
                    height = RelativeConstraint(scale)
                    x = RelativeConstraint(1.0F)
                    y = 10.pixels(true)
                }.animate {
                    setXAnimation(strategy, time, 10.pixels(true))
                    onComplete { slideOutAnimation(imageComponent) }
                }
            }

            2 -> {
                imageComponent.constrain {
                    width = RelativeConstraint(scale)
                    height = RelativeConstraint(scale)
                    x = 10.pixels(true)
                    y = 10.pixels(true)
                }.animate {
                    onComplete { fakeAnimation(imageComponent) }
                }
            }
        }
    }

    private fun fakeAnimation(container: UIComponent) {
        container.animate {
            setColorAnimation(
                Animations.IN_OUT_CIRCULAR,
                1.0F,
                Color.WHITE.toConstraint(),
                PatcherConfig.previewTime.toFloat()
            ).onComplete { currentWindow = null }
        }
    }

    private fun slideOutAnimation(container: UIComponent) {
        container.animate {
            setXAnimation(
                Animations.IN_OUT_CIRCULAR,
                1.0F,
                RelativeConstraint(2.0F),
                PatcherConfig.previewTime.toFloat()
            ).onComplete { currentWindow = null }
        }
    }
}
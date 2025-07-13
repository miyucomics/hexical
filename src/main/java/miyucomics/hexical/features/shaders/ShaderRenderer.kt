package miyucomics.hexical.features.shaders

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.PostEffectProcessor
import net.minecraft.util.Identifier
import java.io.IOException

object ShaderRenderer {
    private var activeShader: PostEffectProcessor? = null
    private var lastShader: PostEffectProcessor? = null
    private var lastWidth = 0
    private var lastHeight = 0

    @JvmStatic
    fun render(deltaTick: Float) {
        if (activeShader == null)
            return

        if (lastShader != activeShader) {
            lastShader = activeShader
            lastWidth = 0
            lastHeight = 0
        }

        updateEffectSize(activeShader!!)
        activeShader!!.render(deltaTick)
        MinecraftClient.getInstance().framebuffer.beginWrite(false)
    }

    fun setEffect(location: Identifier?) {
        if (location == null) {
            activeShader = null
            return
        }
        try {
            val client = MinecraftClient.getInstance()
            activeShader = PostEffectProcessor(client.textureManager, client.resourceManager, client.framebuffer, location)
        }  catch (ioexception: IOException) {
            println(ioexception)
        }
    }

    private fun updateEffectSize(effect: PostEffectProcessor) {
        val client = MinecraftClient.getInstance()
        val width = client.window.width
        val height = client.window.height
        if (width != lastWidth || height != lastHeight) {
            lastWidth = width
            lastHeight = height
            effect.setupDimensions(width, height)
        }
    }
}
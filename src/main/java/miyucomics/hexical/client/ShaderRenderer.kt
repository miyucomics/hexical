package miyucomics.hexical.client

import com.google.gson.JsonSyntaxException
import miyucomics.hexical.HexicalMain
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.PostEffectProcessor
import net.minecraft.util.Identifier
import java.io.IOException

object ShaderRenderer {
    private val COLOR_VISION: Identifier = HexicalMain.id("shaders/post/color_vision.json");
    private var nightVisionShader: PostEffectProcessor? = null

    private var lastWidth = 0
    private var lastHeight = 0
    private var lastShader: PostEffectProcessor? = null

    @JvmStatic
    fun render(deltaTick: Float) {
        MinecraftClient.getInstance().player ?: return
        makeShaders()

        var activeShader: PostEffectProcessor? = null
        activeShader = nightVisionShader
        if (activeShader == null)
            return

        if (lastShader != activeShader) {
            lastShader = activeShader
            lastWidth = 0
            lastHeight = 0
        }

        updateShaderGroupSize(activeShader)
        activeShader.render(deltaTick)
        MinecraftClient.getInstance().framebuffer.beginWrite(false)
    }

    private fun createShaderGroup(location: Identifier): PostEffectProcessor? {
        try {
            val client = MinecraftClient.getInstance()
            return PostEffectProcessor(client.textureManager, client.resourceManager, client.framebuffer, location)
        }  catch (ioexception: IOException) {
            println("Failed to load shader: $location");
        } catch (jsonsyntaxexception: JsonSyntaxException) {
            println("Failed to parse shader: $location");
        }
        return null
    }

    private fun makeShaders() {
        if (nightVisionShader == null) {
            nightVisionShader = createShaderGroup(COLOR_VISION)
        }
    }

    private fun updateShaderGroupSize(shaderGroup: PostEffectProcessor?) {
        if (shaderGroup == null)
            return
        val client = MinecraftClient.getInstance()
        val width = client.window.width
        val height = client.window.height
        if (width != lastWidth || height != lastHeight) {
            lastWidth = width
            lastHeight = height
            shaderGroup.setupDimensions(width, height)
        }
    }
}
package miyucomics.hexical.features.shaders

import miyucomics.hexical.inits.InitHook
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.util.Identifier

object ClientShaderReceiver : InitHook() {
    override fun init() {
        ClientPlayConnectionEvents.DISCONNECT.register { _, _ -> ShaderRenderer.setEffect(null) }

        ClientPlayNetworking.registerGlobalReceiver(ServerShaderManager.SHADER_CHANNEL) { client, _, packet, _ ->
            val shader = packet.readString()
            if (shader == "null")
                client.execute { ShaderRenderer.setEffect(null) }
            else
                client.execute { ShaderRenderer.setEffect(Identifier(shader)) }
        }
    }
}
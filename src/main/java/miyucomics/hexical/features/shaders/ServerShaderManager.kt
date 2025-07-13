package miyucomics.hexical.features.shaders

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.player.getHexicalPlayerManager
import miyucomics.hexical.inits.Hook
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import kotlin.toString

object ServerShaderManager : Hook() {
    val SHADER_CHANNEL: Identifier = HexicalMain.id("shader")

    fun setShader(player: ServerPlayerEntity, shader: Identifier?) {
        ServerPlayNetworking.send(player, SHADER_CHANNEL, PacketByteBufs.create().also { it.writeString(shader.toString()) })
    }

    override fun registerCallbacks() {
        ServerPlayerEvents.AFTER_RESPAWN.register { old, new, alive ->
            if (!alive)
                ShaderRenderer.setEffect(null)
        }
    }
}
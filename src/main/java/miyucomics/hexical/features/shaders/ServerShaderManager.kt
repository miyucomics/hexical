package miyucomics.hexical.features.shaders

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.misc.InitHook
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

object ServerShaderManager : InitHook() {
    val SHADER_CHANNEL: Identifier = HexicalMain.id("shader")

    fun setShader(player: ServerPlayerEntity, shader: Identifier?) {
        ServerPlayNetworking.send(player, SHADER_CHANNEL, PacketByteBufs.create().also { it.writeString(shader.toString()) })
    }

    override fun init() {
        ServerPlayerEvents.AFTER_RESPAWN.register { _, player, alive ->
            if (!alive)
                setShader(player, null)
        }
    }
}
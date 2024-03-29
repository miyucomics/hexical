package miyucomics.hexical.registry

import at.petrak.hexcasting.api.spell.iota.BooleanIota
import at.petrak.hexcasting.api.spell.iota.Iota
import dev.architectury.networking.NetworkManager
import miyucomics.hexical.Hexical
import miyucomics.hexical.items.ConjuredStaffItem
import miyucomics.hexical.items.getConjuredStaff
import miyucomics.hexical.state.TelepathyData
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

object HexicalNetworking {
	@JvmStatic
	fun init() {
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, Hexical.START_TELEPATHY_PACKET) { _, context ->
			TelepathyData.active[context.player.uuid] = true
			TelepathyData.timer[context.player.uuid] = 0
		}
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, Hexical.STOP_TELEPATHY_PACKET) { _, context ->
			TelepathyData.active[context.player.uuid] = false
			TelepathyData.timer[context.player.uuid] = -1
		}
		NetworkManager.registerReceiver(NetworkManager.Side.C2S, Hexical.CAST_CONJURED_STAFF_PACKET) { buf, context ->
			val player = context.player
			val hand = getConjuredStaff(player) ?: return@registerReceiver
			val constructedStack: MutableList<Iota> = ArrayList()
			val staffRank = buf.readInt()
			for (i in 0 until staffRank)
				constructedStack.add(BooleanIota(buf.readBoolean()))
			ConjuredStaffItem.cast(context.player.world as ServerWorld, player as ServerPlayerEntity, hand, player.getStackInHand(hand), constructedStack)
		}
	}
}
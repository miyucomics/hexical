package miyucomics.hexical.utils

import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.interfaces.CastingContextMixinInterface
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

class CastingUtils {
	companion object {
		@Suppress("CAST_NEVER_SUCCEEDS")
		fun castInvisibly(world: ServerWorld, user: ServerPlayerEntity, hex: List<Iota>, archlamp: Boolean = false) {
			val context = CastingContext(user, user.activeHand, CastingContext.CastSource.PACKAGED_HEX)
			(context as CastingContextMixinInterface).setCastByLamp(true)
			if (archlamp)
				(context as CastingContextMixinInterface).setArchlamp(true)
			CastingHarness(context).executeIotas(hex, world)
		}

		fun doesPlayerHaveActiveArchLamp(player: ServerPlayerEntity): Boolean {
			for (stack in player.inventory.main)
				if (stack.item == HexicalItems.MASTER_LAMP_ITEM && stack.orCreateNbt.getBoolean("active"))
					return true
			return false
		}
	}
}
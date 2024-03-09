package miyucomics.hexical.utils

import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.casting.CastingHarness
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.math.HexPattern
import miyucomics.hexical.interfaces.CastingContextMixinInterface
import miyucomics.hexical.items.GrimoireItem
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text

class CastingUtils {
	companion object {
		@Suppress("CAST_NEVER_SUCCEEDS")
		fun castInvisibly(world: ServerWorld, user: ServerPlayerEntity, hex: List<Iota>, archlamp: Boolean = false) {
			val context = CastingContext(user, user.activeHand, CastingContext.CastSource.PACKAGED_HEX)
			(context as CastingContextMixinInterface).setCastByLamp(true)
			if (archlamp)
				(context as CastingContextMixinInterface).setArchLamp(true)
			CastingHarness(context).executeIotas(hex, world)
		}

		fun doesPlayerHaveActiveArchLamp(player: ServerPlayerEntity): Boolean {
			for (stack in player.inventory.main)
				if (stack.item == HexicalItems.ARCH_LAMP_ITEM && stack.orCreateNbt.getBoolean("active"))
					return true
			return false
		}

		fun grimoireLookup(player: ServerPlayerEntity, pattern: HexPattern): List<Iota>? {
			for (stack in player.inventory.main) {
				if (stack.item != HexicalItems.GRIMOIRE_ITEM)
					continue
				val value = GrimoireItem.getPatternInGrimoire(stack, pattern, player.getWorld())
				if (value != null)
					return value
			}
			for (stack in player.inventory.offHand) {
				if (stack.item != HexicalItems.GRIMOIRE_ITEM)
					continue
				val value = GrimoireItem.getPatternInGrimoire(stack, pattern, player.getWorld())
				if (value != null)
					return value
			}
			return null
		}
	}
}
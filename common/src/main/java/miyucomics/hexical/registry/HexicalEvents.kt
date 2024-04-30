package miyucomics.hexical.registry

import dev.architectury.event.EventResult
import dev.architectury.event.events.common.PlayerEvent
import dev.architectury.event.events.common.TickEvent
import miyucomics.hexical.state.TelepathyData
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents

object HexicalEvents {
	@JvmStatic
	fun init() {
		TickEvent.SERVER_POST.register(TickEvent.Server {
			for (key in TelepathyData.timer.keys)
				if (TelepathyData.active[key]!!)
					TelepathyData.timer[key] = TelepathyData.timer[key]!! + 1
		})
		PlayerEvent.ATTACK_ENTITY.register { player, world, target, hand, _ ->
			if (player.getStackInHand(hand).isOf(HexicalItems.LIGHTNING_ROD_STAFF)) {
				world.playSound(target.x, target.y, target.z, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.MASTER, 3f, 1f, true)
				val velocity = target.pos.subtract(player.pos).normalize().multiply(1.5)
				target.addVelocity(velocity.x, velocity.y, velocity.z)
			}
			EventResult.pass()
		}
	}
}
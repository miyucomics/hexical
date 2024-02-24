package miyucomics.hexical.registry

import dev.architectury.event.EventResult
import dev.architectury.event.events.common.PlayerEvent
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity

object HexicalEvents {
	@JvmStatic
	fun init() {
		PlayerEvent.DROP_ITEM.register { _: PlayerEntity, item: ItemEntity ->
			EventResult.pass()
		}
	}
}
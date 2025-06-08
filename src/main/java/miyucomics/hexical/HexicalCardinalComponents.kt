package miyucomics.hexical

import at.petrak.hexcasting.api.addldata.ADMediaHolder
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.fabric.cc.HexCardinalComponents
import at.petrak.hexcasting.fabric.cc.adimpl.CCMediaHolder
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer
import miyucomics.hexical.registry.HexicalItems

class HexicalCardinalComponents : EntityComponentInitializer, ItemComponentInitializer {
	override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
//		registry.registerFor(AnimatedScrollEntity::class.java, HexCardinalComponents.IOTA_HOLDER) { entity -> CCIotaHolder }
	}

	override fun registerItemComponentFactories(registry: ItemComponentFactoryRegistry) {
		registry.register(HexicalItems.HEX_GUMMY, HexCardinalComponents.MEDIA_HOLDER) { stack -> CCMediaHolder.Static({ MediaConstants.DUST_UNIT / 10 }, ADMediaHolder.AMETHYST_DUST_PRIORITY, stack) }
	}
}
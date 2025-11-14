package miyucomics.hexical

import at.petrak.hexcasting.api.addldata.ADMediaHolder
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.fabric.cc.HexCardinalComponents
import at.petrak.hexcasting.fabric.cc.adimpl.CCEntityIotaHolder
import at.petrak.hexcasting.fabric.cc.adimpl.CCMediaHolder
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer
import miyucomics.hexical.features.animated_scrolls.AnimatedScrollEntity
import miyucomics.hexical.features.animated_scrolls.AnimatedScrollReader
import miyucomics.hexical.features.specklikes.mesh.MeshChronicler
import miyucomics.hexical.features.specklikes.mesh.MeshEntity
import miyucomics.hexical.features.specklikes.speck.SpeckChronicler
import miyucomics.hexical.features.specklikes.speck.SpeckEntity
import miyucomics.hexical.inits.HexicalItems

class HexicalCardinalComponents : EntityComponentInitializer, ItemComponentInitializer {
	override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
		registry.registerFor(AnimatedScrollEntity::class.java, HexCardinalComponents.IOTA_HOLDER) { CCEntityIotaHolder.Wrapper(AnimatedScrollReader(it)) }
		registry.registerFor(SpeckEntity::class.java, HexCardinalComponents.IOTA_HOLDER, ::SpeckChronicler)
		registry.registerFor(MeshEntity::class.java, HexCardinalComponents.IOTA_HOLDER, ::MeshChronicler)
	}

	override fun registerItemComponentFactories(registry: ItemComponentFactoryRegistry) {
		registry.register(HexicalItems.HEX_GUMMY, HexCardinalComponents.MEDIA_HOLDER) { CCMediaHolder.Static({ MediaConstants.DUST_UNIT / 10 }, ADMediaHolder.AMETHYST_DUST_PRIORITY, it) }
	}
}
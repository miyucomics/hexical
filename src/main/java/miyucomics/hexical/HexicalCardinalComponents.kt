package miyucomics.hexical

import at.petrak.hexcasting.api.addldata.ADMediaHolder
import at.petrak.hexcasting.api.addldata.ItemDelegatingEntityIotaHolder
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.getList
import at.petrak.hexcasting.fabric.cc.HexCardinalComponents
import at.petrak.hexcasting.fabric.cc.adimpl.CCEntityIotaHolder
import at.petrak.hexcasting.fabric.cc.adimpl.CCMediaHolder
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer
import miyucomics.hexical.features.animated_scrolls.AnimatedScrollEntity
import miyucomics.hexical.inits.HexicalItems
import net.minecraft.nbt.NbtElement

class HexicalCardinalComponents : EntityComponentInitializer, ItemComponentInitializer {
	override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
		registry.registerFor(AnimatedScrollEntity::class.java, HexCardinalComponents.IOTA_HOLDER) { CCEntityIotaHolder.Wrapper(AnimatedScrollReader(it)) }
	}

	override fun registerItemComponentFactories(registry: ItemComponentFactoryRegistry) {
		registry.register(HexicalItems.HEX_GUMMY, HexCardinalComponents.MEDIA_HOLDER) { stack -> CCMediaHolder.Static({ MediaConstants.DUST_UNIT / 10 }, ADMediaHolder.AMETHYST_DUST_PRIORITY, stack) }
	}
}

class AnimatedScrollReader(scrollEntity: AnimatedScrollEntity) : ItemDelegatingEntityIotaHolder({ scrollEntity.scroll.copy() }, { stack ->
	scrollEntity.scroll = stack
	scrollEntity.patterns = stack.getList("patterns", NbtElement.COMPOUND_TYPE.toInt())!!.map { it.asCompound }
	scrollEntity.updateRender()
})
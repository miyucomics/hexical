package miyucomics.hexical.init

import at.petrak.hexcasting.api.addldata.ADMediaHolder
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.fabric.cc.HexCardinalComponents
import at.petrak.hexcasting.fabric.cc.adimpl.CCMediaHolder
import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer
import miyucomics.hexical.HexicalMain
import miyucomics.hexical.feature.confection.HexburstIotaHolder
import miyucomics.hexical.feature.mute.MutedComponent
import miyucomics.hexical.feature.specklike.mesh.MeshChronicler
import miyucomics.hexical.feature.specklike.mesh.MeshEntity
import miyucomics.hexical.feature.specklike.speck.SpeckChronicler
import miyucomics.hexical.feature.specklike.speck.SpeckEntity
import miyucomics.hexical.feature.specklike.strand.StrandChronicler
import miyucomics.hexical.feature.specklike.strand.StrandEntity
import net.minecraft.entity.Entity

class HexicalCardinalComponents : EntityComponentInitializer, ItemComponentInitializer {
	override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
		registry.registerFor(SpeckEntity::class.java, HexCardinalComponents.IOTA_HOLDER, ::SpeckChronicler)
		registry.registerFor(StrandEntity::class.java, HexCardinalComponents.IOTA_HOLDER, ::StrandChronicler)
		registry.registerFor(MeshEntity::class.java, HexCardinalComponents.IOTA_HOLDER, ::MeshChronicler)
		registry.registerFor(Entity::class.java, MUTED_COMPONENT, ::MutedComponent)
	}

	@Suppress("UnstableApiUsage")
	override fun registerItemComponentFactories(registry: ItemComponentFactoryRegistry) {
		registry.register(HexicalItems.HEX_GUMMY, HexCardinalComponents.MEDIA_HOLDER) { CCMediaHolder.Static({ MediaConstants.DUST_UNIT / 10 }, ADMediaHolder.AMETHYST_DUST_PRIORITY, it) }
		registry.register(HexicalItems.HEXBURST_ITEM, HexCardinalComponents.IOTA_HOLDER, ::HexburstIotaHolder)
	}

	companion object {
		@JvmField
		val MUTED_COMPONENT: ComponentKey<MutedComponent> = ComponentRegistryV3.INSTANCE.getOrCreate(HexicalMain.id("muted"), MutedComponent::class.java)
	}
}
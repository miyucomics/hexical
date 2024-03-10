package miyucomics.hexical.registry

import dev.architectury.registry.registries.DeferredRegister
import miyucomics.hexical.Hexical
import miyucomics.hexical.entities.SpeckEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.registry.Registry

object HexicalEntities {
	private val ENTITIES: DeferredRegister<EntityType<*>> = DeferredRegister.create(Hexical.MOD_ID, Registry.ENTITY_TYPE_KEY)
	val SPECK_ENTITY: EntityType<SpeckEntity?> = EntityType.Builder.create<SpeckEntity?>(::SpeckEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(10).trackingTickInterval(Int.MAX_VALUE).build(Hexical.MOD_ID + ":speck")

	@JvmStatic
	fun init() {
		ENTITIES.register("speck") { SPECK_ENTITY }
		ENTITIES.register()
	}
}
package miyucomics.hexical.registry

import dev.architectury.registry.client.level.entity.EntityRendererRegistry
import dev.architectury.registry.registries.DeferredRegister
import miyucomics.hexical.Hexical
import miyucomics.hexical.entities.*
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.registry.Registry

object HexicalEntities {
	private val ENTITIES: DeferredRegister<EntityType<*>> = DeferredRegister.create(Hexical.MOD_ID, Registry.ENTITY_TYPE_KEY)
	val LIVING_SCROLL_ENTITY: EntityType<LivingScrollEntity?> = EntityType.Builder.create(::LivingScrollEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(10).trackingTickInterval(1).build(Hexical.MOD_ID + ":living_scroll")
	val SPECK_ENTITY: EntityType<SpeckEntity?> = EntityType.Builder.create(::SpeckEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(10).trackingTickInterval(1).build(Hexical.MOD_ID + ":speck")
	val STATUE_ENTITY: EntityType<StatueEntity?> = EntityType.Builder.create(::StatueEntity, SpawnGroup.MISC).setDimensions(0.6f, 1.95f).maxTrackingRange(10).build(Hexical.MOD_ID + ":statue")

	@JvmStatic
	fun init() {
		ENTITIES.register("living_scroll") { LIVING_SCROLL_ENTITY }
		ENTITIES.register("speck") { SPECK_ENTITY }
		ENTITIES.register("statue") { STATUE_ENTITY }
		ENTITIES.register()
	}

	@JvmStatic
	fun clientInit() {
		EntityRendererRegistry.register(HexicalEntities::LIVING_SCROLL_ENTITY) { ctx: EntityRendererFactory.Context? -> LivingScrollRenderer(ctx) }
		EntityRendererRegistry.register(HexicalEntities::SPECK_ENTITY) { ctx: EntityRendererFactory.Context? -> SpeckRenderer(ctx) }
		EntityRendererRegistry.register(HexicalEntities::STATUE_ENTITY) { ctx: EntityRendererFactory.Context? -> StatueRenderer(ctx) }
	}
}
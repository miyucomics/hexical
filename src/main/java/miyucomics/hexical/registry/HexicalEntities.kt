package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.entities.*
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.registry.Registry

object HexicalEntities {
	val MAGIC_MISSILE_ENTITY: EntityType<MagicMissileEntity?> = EntityType.Builder.create(::MagicMissileEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build(HexicalMain.MOD_ID + ":magic_missile")
	val LIVING_SCROLL_ENTITY: EntityType<LivingScrollEntity?> = EntityType.Builder.create(::LivingScrollEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(10).trackingTickInterval(1).build(HexicalMain.MOD_ID + ":living_scroll")
	val FLECK_ENTITY: EntityType<FleckEntity?> = EntityType.Builder.create(::FleckEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(10).trackingTickInterval(1).build(HexicalMain.MOD_ID + ":fleck")
	val SPECK_ENTITY: EntityType<SpeckEntity?> = EntityType.Builder.create(::SpeckEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(10).trackingTickInterval(1).build(HexicalMain.MOD_ID + ":speck")

	@JvmStatic
	fun init() {
		Registry.register(Registry.ENTITY_TYPE, HexicalMain.id("living_scroll"), LIVING_SCROLL_ENTITY)
		Registry.register(Registry.ENTITY_TYPE, HexicalMain.id("magic_missile"), MAGIC_MISSILE_ENTITY)
		Registry.register(Registry.ENTITY_TYPE, HexicalMain.id("fleck"), FLECK_ENTITY)
		Registry.register(Registry.ENTITY_TYPE, HexicalMain.id("speck"), SPECK_ENTITY)
	}

	@JvmStatic
	fun clientInit() {
		EntityRendererRegistry.register(LIVING_SCROLL_ENTITY) { ctx: EntityRendererFactory.Context? -> LivingScrollRenderer(ctx) }
		EntityRendererRegistry.register(MAGIC_MISSILE_ENTITY) { ctx: EntityRendererFactory.Context? -> MagicMissileRenderer(ctx) }
		EntityRendererRegistry.register(FLECK_ENTITY) { ctx: EntityRendererFactory.Context? -> FleckRenderer(ctx) }
		EntityRendererRegistry.register(SPECK_ENTITY) { ctx: EntityRendererFactory.Context? -> SpeckRenderer(ctx) }
	}
}
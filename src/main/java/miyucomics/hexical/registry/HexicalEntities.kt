package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.entities.*
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.registry.Registry

object HexicalEntities {
	val MAGIC_MISSILE_ENTITY: EntityType<MagicMissileEntity> = EntityType.Builder.create(::MagicMissileEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build(HexicalMain.MOD_ID + ":magic_missile")
	val LIVING_SCROLL_ENTITY: EntityType<LivingScrollEntity> = EntityType.Builder.create(::LivingScrollEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(10).trackingTickInterval(1).build(HexicalMain.MOD_ID + ":living_scroll")
	val MESH_ENTITY: EntityType<MeshEntity> = EntityType.Builder.create(::MeshEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(10).trackingTickInterval(1).build(HexicalMain.MOD_ID + ":mesh")
	val SPECK_ENTITY: EntityType<SpeckEntity> = EntityType.Builder.create(::SpeckEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(10).trackingTickInterval(1).build(HexicalMain.MOD_ID + ":speck")
	val SPIKE_ENTITY: EntityType<SpikeEntity> = EntityType.Builder.create(::SpikeEntity, SpawnGroup.MISC).setDimensions(1f, 1f).maxTrackingRange(10).trackingTickInterval(1).build(HexicalMain.MOD_ID + ":spike")

	@JvmStatic
	fun init() {
		Registry.register(Registry.ENTITY_TYPE, HexicalMain.id("living_scroll"), LIVING_SCROLL_ENTITY)
		Registry.register(Registry.ENTITY_TYPE, HexicalMain.id("magic_missile"), MAGIC_MISSILE_ENTITY)
		Registry.register(Registry.ENTITY_TYPE, HexicalMain.id("mesh"), MESH_ENTITY)
		Registry.register(Registry.ENTITY_TYPE, HexicalMain.id("speck"), SPECK_ENTITY)
		Registry.register(Registry.ENTITY_TYPE, HexicalMain.id("spike"), SPIKE_ENTITY)
	}

	@JvmStatic
	fun clientInit() {
		EntityRendererRegistry.register(LIVING_SCROLL_ENTITY) { ctx: EntityRendererFactory.Context -> LivingScrollRenderer(ctx) }
		EntityRendererRegistry.register(MAGIC_MISSILE_ENTITY) { ctx: EntityRendererFactory.Context -> MagicMissileRenderer(ctx) }
		EntityRendererRegistry.register(MESH_ENTITY) { ctx: EntityRendererFactory.Context -> MeshRenderer(ctx) }
		EntityRendererRegistry.register(SPECK_ENTITY) { ctx: EntityRendererFactory.Context -> SpeckRenderer(ctx) }
		EntityRendererRegistry.register(SPIKE_ENTITY) { ctx: EntityRendererFactory.Context -> SpikeRenderer(ctx) }
	}
}
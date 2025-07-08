package miyucomics.hexical.registry

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.entities.*
import miyucomics.hexical.entities.specklikes.MeshEntity
import miyucomics.hexical.entities.specklikes.MeshRenderer
import miyucomics.hexical.entities.specklikes.SpeckEntity
import miyucomics.hexical.entities.specklikes.SpeckRenderer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object HexicalEntities {
	val ANIMATED_SCROLL_ENTITY: EntityType<AnimatedScrollEntity> = EntityType.Builder.create(::AnimatedScrollEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(10).trackingTickInterval(1).build(HexicalMain.MOD_ID + ":animated_scroll")
	val MAGIC_MISSILE_ENTITY: EntityType<MagicMissileEntity> = EntityType.Builder.create(::MagicMissileEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build(HexicalMain.MOD_ID + ":magic_missile")
	val SPIKE_ENTITY: EntityType<SpikeEntity> = EntityType.Builder.create(::SpikeEntity, SpawnGroup.MISC).setDimensions(1f, 1f).maxTrackingRange(10).trackingTickInterval(1).build(HexicalMain.MOD_ID + ":spike")
	val SPECK_ENTITY: EntityType<SpeckEntity> = EntityType.Builder.create(::SpeckEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(32).trackingTickInterval(1).build(HexicalMain.MOD_ID + ":speck")
	val MESH_ENTITY: EntityType<MeshEntity> = EntityType.Builder.create(::MeshEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(32).trackingTickInterval(1).build(HexicalMain.MOD_ID + ":mesh")

	fun init() {
		Registry.register(Registries.ENTITY_TYPE, HexicalMain.id("animated_scroll"), ANIMATED_SCROLL_ENTITY)
		Registry.register(Registries.ENTITY_TYPE, HexicalMain.id("magic_missile"), MAGIC_MISSILE_ENTITY)
		Registry.register(Registries.ENTITY_TYPE, HexicalMain.id("spike"), SPIKE_ENTITY)
		Registry.register(Registries.ENTITY_TYPE, HexicalMain.id("speck"), SPECK_ENTITY)
		Registry.register(Registries.ENTITY_TYPE, HexicalMain.id("mesh"), MESH_ENTITY)
	}

	fun clientInit() {
		EntityRendererRegistry.register(ANIMATED_SCROLL_ENTITY) { ctx -> AnimatedScrollRenderer(ctx) }
		EntityRendererRegistry.register(MAGIC_MISSILE_ENTITY) { ctx -> MagicMissileRenderer(ctx) }
		EntityRendererRegistry.register(SPIKE_ENTITY) { ctx -> SpikeRenderer(ctx) }
		EntityRendererRegistry.register(SPECK_ENTITY) { ctx -> SpeckRenderer(ctx) }
		EntityRendererRegistry.register(MESH_ENTITY) { ctx -> MeshRenderer(ctx) }
	}
}
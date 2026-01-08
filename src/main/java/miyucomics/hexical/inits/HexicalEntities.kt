package miyucomics.hexical.inits

import miyucomics.hexical.HexicalMain
import miyucomics.hexical.features.animated_scrolls.AnimatedScrollEntity
import miyucomics.hexical.features.magic_missile.MagicMissileEntity
import miyucomics.hexical.features.specklikes.mesh.MeshEntity
import miyucomics.hexical.features.specklikes.speck.SpeckEntity
import miyucomics.hexical.features.specklikes.strand.StrandEntity
import miyucomics.hexical.features.spike.SpikeEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object HexicalEntities {
	val ANIMATED_SCROLL_ENTITY: EntityType<AnimatedScrollEntity> = EntityType.Builder.create(::AnimatedScrollEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(10).trackingTickInterval(1).build(HexicalMain.MOD_ID + ":animated_scroll")
	val MAGIC_MISSILE_ENTITY: EntityType<MagicMissileEntity> = EntityType.Builder.create(::MagicMissileEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build(HexicalMain.MOD_ID + ":magic_missile")
	val SPIKE_ENTITY: EntityType<SpikeEntity> = EntityType.Builder.create(::SpikeEntity, SpawnGroup.MISC).setDimensions(1f, 1f).maxTrackingRange(10).trackingTickInterval(1).build(HexicalMain.MOD_ID + ":spike")
	val SPECK_ENTITY: EntityType<SpeckEntity> = EntityType.Builder.create(::SpeckEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(32).trackingTickInterval(1).build(HexicalMain.MOD_ID + ":speck")
	val STRAND_ENTITY: EntityType<StrandEntity> = EntityType.Builder.create(::StrandEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(32).trackingTickInterval(1).build(HexicalMain.MOD_ID + ":strand")
	val MESH_ENTITY: EntityType<MeshEntity> = EntityType.Builder.create(::MeshEntity, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(32).trackingTickInterval(1).build(HexicalMain.MOD_ID + ":mesh")

	fun init() {
		Registry.register(Registries.ENTITY_TYPE, HexicalMain.id("animated_scroll"), ANIMATED_SCROLL_ENTITY)
		Registry.register(Registries.ENTITY_TYPE, HexicalMain.id("magic_missile"), MAGIC_MISSILE_ENTITY)
		Registry.register(Registries.ENTITY_TYPE, HexicalMain.id("spike"), SPIKE_ENTITY)
		Registry.register(Registries.ENTITY_TYPE, HexicalMain.id("speck"), SPECK_ENTITY)
		Registry.register(Registries.ENTITY_TYPE, HexicalMain.id("strand"), STRAND_ENTITY)
		Registry.register(Registries.ENTITY_TYPE, HexicalMain.id("mesh"), MESH_ENTITY)
	}
}
package miyucomics.hexical.init

import miyucomics.hexical.feature.magic_missile.MagicMissileRenderer
import miyucomics.hexical.feature.specklike.MeshRenderer
import miyucomics.hexical.feature.specklike.SpeckRenderer
import miyucomics.hexical.feature.specklike.StrandRenderer
import miyucomics.hexical.feature.spike.SpikeRenderer
import miyucomics.hexical.init.HexicalEntities.MAGIC_MISSILE_ENTITY
import miyucomics.hexical.init.HexicalEntities.MESH_ENTITY
import miyucomics.hexical.init.HexicalEntities.SPECK_ENTITY
import miyucomics.hexical.init.HexicalEntities.SPIKE_ENTITY
import miyucomics.hexical.init.HexicalEntities.STRAND_ENTITY
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry

object HexicalEntitiesClient {
	fun clientInit() {
		EntityRendererRegistry.register(MAGIC_MISSILE_ENTITY, ::MagicMissileRenderer)
		EntityRendererRegistry.register(SPIKE_ENTITY, ::SpikeRenderer)
		EntityRendererRegistry.register(SPECK_ENTITY, ::SpeckRenderer)
		EntityRendererRegistry.register(STRAND_ENTITY, ::StrandRenderer)
		EntityRendererRegistry.register(MESH_ENTITY, ::MeshRenderer)
	}
}
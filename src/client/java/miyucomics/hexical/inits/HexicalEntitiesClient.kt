package miyucomics.hexical.inits

import miyucomics.hexical.features.animated_scrolls.AnimatedScrollRenderer
import miyucomics.hexical.features.magic_missile.MagicMissileRenderer
import miyucomics.hexical.features.specklikes.MeshRenderer
import miyucomics.hexical.features.specklikes.SpeckRenderer
import miyucomics.hexical.features.specklikes.StrandRenderer
import miyucomics.hexical.features.spike.SpikeRenderer
import miyucomics.hexical.inits.HexicalEntities.ANIMATED_SCROLL_ENTITY
import miyucomics.hexical.inits.HexicalEntities.MAGIC_MISSILE_ENTITY
import miyucomics.hexical.inits.HexicalEntities.MESH_ENTITY
import miyucomics.hexical.inits.HexicalEntities.SPECK_ENTITY
import miyucomics.hexical.inits.HexicalEntities.SPIKE_ENTITY
import miyucomics.hexical.inits.HexicalEntities.STRAND_ENTITY
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry

object HexicalEntitiesClient {
	fun clientInit() {
		EntityRendererRegistry.register(ANIMATED_SCROLL_ENTITY, ::AnimatedScrollRenderer)
		EntityRendererRegistry.register(MAGIC_MISSILE_ENTITY, ::MagicMissileRenderer)
		EntityRendererRegistry.register(SPIKE_ENTITY, ::SpikeRenderer)
		EntityRendererRegistry.register(SPECK_ENTITY, ::SpeckRenderer)
		EntityRendererRegistry.register(STRAND_ENTITY, ::StrandRenderer)
		EntityRendererRegistry.register(MESH_ENTITY, ::MeshRenderer)
	}
}
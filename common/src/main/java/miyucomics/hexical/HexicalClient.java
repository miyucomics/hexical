package miyucomics.hexical;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import miyucomics.hexical.entities.SpeckEntityRenderer;
import miyucomics.hexical.registry.HexicalEntities;

public class HexicalClient {
	public static void init() {
		EntityRendererRegistry.register(HexicalEntities.INSTANCE::getSPECK_ENTITY, SpeckEntityRenderer::new);
	}
}
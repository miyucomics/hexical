package miyucomics.hexical.fabric;

import miyucomics.hexical.entities.StatueEntity;
import miyucomics.hexical.registry.HexicalEntities;
import net.fabricmc.api.ModInitializer;
import miyucomics.hexical.Hexical;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class HexicalFabric implements ModInitializer {
	@Override public void onInitialize() {
		Hexical.init();
	}
}
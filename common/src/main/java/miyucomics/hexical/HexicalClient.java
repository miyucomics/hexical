package miyucomics.hexical;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import miyucomics.hexical.entities.SpeckEntityRenderer;
import miyucomics.hexical.registry.HexicalEntities;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class HexicalClient {
	private static final KeyBinding TELEPATHY_KEYBIND = new KeyBinding("key.hexical.telepathy", GLFW.GLFW_KEY_G, "key.categories.hexical");

	public static void init() {
		ClientTickEvent.CLIENT_POST.register(client -> {
			if (client.player == null)
				return;
			while (TELEPATHY_KEYBIND.wasPressed())
				client.player.sendMessage(Text.literal("using mind powers!!"), false);
		});
		EntityRendererRegistry.register(HexicalEntities.INSTANCE::getSPECK_ENTITY, SpeckEntityRenderer::new);
	}
}
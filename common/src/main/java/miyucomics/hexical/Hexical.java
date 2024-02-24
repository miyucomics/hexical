package miyucomics.hexical;

import at.petrak.hexcasting.api.spell.iota.BooleanIota;
import at.petrak.hexcasting.api.spell.iota.Iota;
import dev.architectury.networking.NetworkManager;
import miyucomics.hexical.items.ConjuredStaffItem;
import miyucomics.hexical.registry.HexicalBlocks;
import miyucomics.hexical.registry.HexicalEvents;
import miyucomics.hexical.registry.HexicalItems;
import miyucomics.hexical.registry.HexicalPatterns;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Hexical {
	public static final String MOD_ID = "hexical";
	public static final Identifier CAST_CONJURED_STAFF_PACKET = new Identifier(MOD_ID, "cast_conjured_staff");

	public static void init() {
		HexicalAbstractions.initPlatformSpecific();
		HexicalBlocks.init();
		HexicalItems.init();
		HexicalPatterns.init();
		HexicalEvents.init();

		NetworkManager.registerReceiver(NetworkManager.Side.C2S, CAST_CONJURED_STAFF_PACKET, (buf, context) -> {
			PlayerEntity player = context.getPlayer();
			ItemStack itemInHand = player.getMainHandStack();
			if (itemInHand.getItem() instanceof ConjuredStaffItem) {
				List<Iota> stack = new ArrayList<>();
				for (int i = 0; i < buf.readInt(); i++)
					stack.add(new BooleanIota(buf.readBoolean()));
				((ConjuredStaffItem) itemInHand.getItem()).cast(context.getPlayer().world, player, itemInHand, stack);
			}
		});
	}

	public static Identifier id(String string) {
		return new Identifier(MOD_ID, string);
	}
}
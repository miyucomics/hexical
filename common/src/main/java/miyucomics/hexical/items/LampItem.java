package miyucomics.hexical.items;

import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex;
import miyucomics.hexical.registry.HexicalItemRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.List;

public class LampItem extends ItemPackagedHex {
	public LampItem() {
		super(new Item.Settings());
	}

	@Override public TypedActionResult<ItemStack> use (World world, PlayerEntity player, Hand usedHand) {
		var stack = player.getStackInHand(usedHand);
		if (!hasHex(stack)) return TypedActionResult.fail(stack);
		player.setCurrentHand(usedHand);
		return TypedActionResult.success(stack);
	}

	@Override public UseAction getUseAction (ItemStack pStack) {
		return UseAction.BOW;
	}

	@Override public void usageTick (World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (world.isClient) return;
		if (getMedia(stack) == 0) return;

		List<Iota> hex = getHex(stack, (ServerWorld) world);
		if (hex == null) return;
		CastingHarness harness = new CastingHarness(new CastingContext((ServerPlayerEntity) user, user.getActiveHand(), CastingContext.CastSource.PACKAGED_HEX));
		harness.executeIotas(hex, (ServerWorld) world);
	}

	@Override public int getMaxUseTime (ItemStack stack) {
		return 72000;
	}

	@Override public boolean breakAfterDepletion () { return false; }
	@Override public boolean canDrawMediaFromInventory (ItemStack stack) { return false; }
	public static boolean isUsingLamp(CastingContext context) {
		return context.getSource().equals(CastingContext.CastSource.PACKAGED_HEX) && context.getCaster().getActiveItem().getItem().equals(HexicalItemRegistry.LAMP_ITEM);
	}
}
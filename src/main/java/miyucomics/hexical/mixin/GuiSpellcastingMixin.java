package miyucomics.hexical.mixin;

import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import at.petrak.hexcasting.common.lib.HexAttributes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import miyucomics.hexical.registry.HexicalBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GuiSpellcasting.class)
public class GuiSpellcastingMixin {
	@Unique
	long lastCounter = System.currentTimeMillis();
	@Unique
	double zoomFactor = 0;

	@WrapOperation(method = "hexSize", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
	double thinkyCarpet(ClientPlayerEntity player, EntityAttribute entityAttribute, Operation<Double> original) {
		long deltaTime = System.currentTimeMillis() - lastCounter;
		lastCounter = System.currentTimeMillis();

		if (player.getBlockStateAtPos().isOf(HexicalBlocks.CASTING_CARPET)) {
			zoomFactor += (double) deltaTime / 500;
		} else {
			zoomFactor -= (double) deltaTime / 500;
		}
		zoomFactor = MathHelper.clamp(zoomFactor, 0, 0.35);

		double returnValue = original.call(player, entityAttribute);
		returnValue += zoomFactor;
		return returnValue;
	}
}
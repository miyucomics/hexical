package miyucomics.hexical.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import miyucomics.hexical.features.periwinkle.SnifferEntityMinterface;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.SnifferEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnifferEntity.class)
public abstract class SnifferEntityMixin implements SnifferEntityMinterface {
	@Shadow public abstract SnifferEntity startState(SnifferEntity.State state);
	@Shadow public abstract Brain<SnifferEntity> getBrain();
	@Unique private ItemStack customItem = null;
	@Unique private boolean isDiggingCustom = false;

	@Override
	public boolean isDiggingCustom() {
		return isDiggingCustom;
	}

	@Override
	public void produceItem(@NotNull ItemStack stack) {
		this.customItem = stack;
		this.isDiggingCustom = true;
		getBrain().forget(MemoryModuleType.SNIFF_COOLDOWN);
		getBrain().forget(MemoryModuleType.DIG_COOLDOWN);
		getBrain().forget(MemoryModuleType.WALK_TARGET);
		getBrain().forget(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
		getBrain().forget(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);

		getBrain().remember(MemoryModuleType.SNIFFER_DIGGING, true);

		getBrain().stopAllTasks((ServerWorld) ((SnifferEntity) (Object) this).getWorld(), ((SnifferEntity) (Object) this));
		getBrain().resetPossibleActivities();
		startState(SnifferEntity.State.DIGGING);
	}

	@Inject(method = "canTryToDig", at = @At("HEAD"), cancellable = true)
	public void youWantToDig(CallbackInfoReturnable<Boolean> cir) {
		if (isDiggingCustom)
			cir.setReturnValue(true);
	}

	@Inject(method = "canDig", at = @At("HEAD"), cancellable = true)
	public void youCanDig(CallbackInfoReturnable<Boolean> cir) {
		if (isDiggingCustom)
			cir.setReturnValue(true);
	}

	@WrapOperation(method = "dropSeeds", at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/LootTable;generateLoot(Lnet/minecraft/loot/context/LootContextParameterSet;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"))
	public ObjectArrayList<ItemStack> alterDrops(LootTable instance, LootContextParameterSet lootContextParameterSet, Operation<ObjectArrayList<ItemStack>> original) {
		if (isDiggingCustom) {
			ObjectArrayList<ItemStack> newDrops = new ObjectArrayList<>();
			newDrops.add(customItem);
			customItem = null;
			isDiggingCustom = false;
			return newDrops;
		}
		return original.call(instance, lootContextParameterSet);
	}
}
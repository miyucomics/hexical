package miyucomics.hexical.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import miyucomics.hexical.features.player_state.fields.WristpocketField;
import miyucomics.hexical.features.player_state.fields.WristpocketFieldKt;
import miyucomics.hexical.interfaces.PlayerEntityMinterface;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Shadow public abstract void setHealth(float health);
	@Shadow public abstract boolean clearStatusEffects();
	@Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

	@WrapOperation(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;tryUseTotem(Lnet/minecraft/entity/damage/DamageSource;)Z"))
	private boolean undie(LivingEntity instance, DamageSource source, Operation<Boolean> original) {
		if (!(instance instanceof ServerPlayerEntity player))
			return original.call(instance, source);

		ItemStack wristpocket = WristpocketFieldKt.getWristpocket(player);
		if (wristpocket.isOf(Items.TOTEM_OF_UNDYING)) {
			WristpocketFieldKt.setWristpocket(player, ItemStack.EMPTY);
			player.incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
			Criteria.USED_TOTEM.trigger(player, wristpocket);

			setHealth(1.0f);
			clearStatusEffects();
			addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
			addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
			addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
			player.getWorld().sendEntityStatus((Entity) (Object) this, EntityStatuses.USE_TOTEM_OF_UNDYING);

			return true;
		}

		return original.call(instance, source);
	}
}
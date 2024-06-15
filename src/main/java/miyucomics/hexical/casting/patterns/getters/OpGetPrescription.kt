package miyucomics.hexical.casting.patterns.getters

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getEntity
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.ListIota
import at.petrak.hexcasting.api.spell.mishaps.MishapInvalidIota
import miyucomics.hexical.casting.iota.IdentifierIota
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.entity.projectile.ShulkerBulletEntity
import net.minecraft.entity.projectile.WitherSkullEntity
import net.minecraft.entity.projectile.thrown.PotionEntity
import net.minecraft.item.Items
import net.minecraft.potion.PotionUtil
import net.minecraft.util.registry.Registry

class OpGetPrescription : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		return listOf(ListIota(when (val entity = args.getEntity(0, argc)) {
			is ItemEntity -> {
				val stack = entity.stack
				if (!(stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION) || stack.item.isFood || stack.isOf(Items.TIPPED_ARROW)))
					throw MishapInvalidIota.of(args[0], 0, "potion_holding")
				val effects = mutableListOf<IdentifierIota>()
				if (stack.isOf(Items.POTION) || stack.isOf(Items.SPLASH_POTION) || stack.isOf(Items.LINGERING_POTION)) {
					for (effect in PotionUtil.getPotionEffects(stack))
						effects.add(IdentifierIota(Registry.STATUS_EFFECT.getId(effect.effectType)!!))
				} else if (stack.isOf(Items.TIPPED_ARROW)) {
					for (effect in PotionUtil.getPotion(stack).effects)
						effects.add(IdentifierIota(Registry.STATUS_EFFECT.getId(effect.effectType)!!))
					for (effect in PotionUtil.getCustomPotionEffects(stack))
						effects.add(IdentifierIota(Registry.STATUS_EFFECT.getId(effect.effectType)!!))
				} else {
					for (statusEffect in stack.item.foodComponent!!.statusEffects)
						effects.add(IdentifierIota(Registry.STATUS_EFFECT.getId(statusEffect.first.effectType)!!))
				}
				effects
			}
			is ArrowEntity -> {
				val output = mutableListOf<IdentifierIota>()
				for (instance in entity.potion.effects)
					output.add(IdentifierIota(Registry.STATUS_EFFECT.getId(instance.effectType)!!))
				for (instance in entity.effects)
					output.add(IdentifierIota(Registry.STATUS_EFFECT.getId(instance.effectType)!!))
				output
			}
			is PotionEntity -> {
				val effects = mutableListOf<IdentifierIota>()
				for (effect in PotionUtil.getPotionEffects(entity.stack))
					effects.add(IdentifierIota(Registry.STATUS_EFFECT.getId(effect.effectType)!!))
				effects
			}
			is ShulkerBulletEntity -> listOf(IdentifierIota(Registry.STATUS_EFFECT.getId(StatusEffects.LEVITATION)!!))
			is WitherSkullEntity -> listOf(IdentifierIota(Registry.STATUS_EFFECT.getId(StatusEffects.WITHER)!!))
			else -> listOf()
		}))
	}
}
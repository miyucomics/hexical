package miyucomics.hexical.casting.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import net.minecraft.item.AirBlockItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d

class OpWristpocket : SpellAction {
	override val argc = 0
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		return Triple(Spell(ctx.otherHand), MediaConstants.DUST_UNIT * 3, listOf())
	}

	private data class Spell(val hand: Hand) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			print(ctx.caster.getStackInHand(hand))
			val minterface = (ctx.caster as PlayerEntityMinterface)
			if (minterface.wristpocketItem() == ItemStack.EMPTY || minterface.wristpocketItem().item == Items.AIR) {
				(ctx.caster as PlayerEntityMinterface).stashWristpocket(ctx.caster.getStackInHand(hand))
				ctx.caster.setStackInHand(hand, ItemStack.EMPTY)
			} else {
				ctx.caster.setStackInHand(hand, (ctx.caster as PlayerEntityMinterface).wristpocketItem())
				minterface.stashWristpocket(ItemStack.EMPTY)
			}
		}
	}
}
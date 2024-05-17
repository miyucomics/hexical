package miyucomics.hexical.casting.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import miyucomics.hexical.interfaces.PlayerEntityMinterface
import miyucomics.hexical.state.PersistentStateHandler
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
			val item = PersistentStateHandler.wristpocketItem(ctx.caster)
			if (item == ItemStack.EMPTY || item.item == Items.AIR) {
				PersistentStateHandler.stashWristpocket(ctx.caster, ctx.caster.getStackInHand(hand))
				ctx.caster.setStackInHand(hand, ItemStack.EMPTY)
			} else {
				ctx.caster.setStackInHand(hand, item)
				PersistentStateHandler.stashWristpocket(ctx.caster, ItemStack.EMPTY)
			}
		}
	}
}
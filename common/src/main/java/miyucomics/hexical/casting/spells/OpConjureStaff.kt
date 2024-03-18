package miyucomics.hexical.casting.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.ktxt.tellWitnessesThatIWasMurdered
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.registry.HexicalItems
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Vec3d

class OpConjureStaff : SpellAction {
	override val argc = 4
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		val position = args.getVec3(0, argc)
		val battery = args.getInt(1, argc)
		val rank = args.getPositiveInt(2, argc)
		val instructions = args.getList(3, argc).toList()
		return Triple(Spell(position, battery, rank, instructions), battery + MediaConstants.CRYSTAL_UNIT + MediaConstants.SHARD_UNIT * rank, listOf(ParticleSpray.burst(position, 1.0)))
	}

	private data class Spell(val position: Vec3d, val battery: Int, val rank: Int, val instructions: List<Iota>) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val stack = ItemStack(HexicalItems.CONJURED_STAFF_ITEM, 1)
			stack.orCreateNbt.putInt("rank", rank)
			val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(stack)
			hexHolder?.writeHex(instructions, battery)
			ctx.world.spawnEntity(ItemEntity(ctx.world, position.x, position.y, position.z, stack))
		}
	}
}
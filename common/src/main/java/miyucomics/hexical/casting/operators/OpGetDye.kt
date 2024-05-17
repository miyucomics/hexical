package miyucomics.hexical.casting.operators

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.getBlockPos
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.iota.NullIota
import miyucomics.hexical.iota.DyeIota
import net.minecraft.util.DyeColor
import net.minecraft.util.registry.Registry

class OpGetDye : ConstMediaAction {
	private val dyes = mapOf(
		"white" to DyeColor.WHITE,
		"orange" to DyeColor.ORANGE,
		"magenta" to DyeColor.MAGENTA,
		"light_blue" to DyeColor.LIGHT_BLUE,
		"yellow" to DyeColor.YELLOW,
		"lime" to DyeColor.LIME,
		"pink" to DyeColor.PINK,
		"gray" to DyeColor.GRAY,
		"light_gray" to DyeColor.LIGHT_GRAY,
		"cyan" to DyeColor.CYAN,
		"purple" to DyeColor.PURPLE,
		"blue" to DyeColor.BLUE,
		"brown" to DyeColor.BROWN,
		"green" to DyeColor.GREEN,
		"red" to DyeColor.RED,
		"black" to DyeColor.BLACK
	)

	override val argc = 1
	override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
		val position = args.getBlockPos(0, argc)
		ctx.assertVecInRange(position)
		val name = Registry.BLOCK.getId(ctx.world.getBlockState(position).block).path
		for (dye in dyes.keys)
			if (name.startsWith(dye))
				return listOf(DyeIota(dyes[dye]!!))
		return listOf(NullIota())
	}
}
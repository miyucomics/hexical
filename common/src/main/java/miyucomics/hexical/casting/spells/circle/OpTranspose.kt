package miyucomics.hexical.casting.spells.circle

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.*
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.MishapNoSpellCircle
import at.petrak.hexcasting.xplat.IXplatAbstractions
import miyucomics.hexical.casting.mishaps.OutsideCircleMishap
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i

class OpTranspose : SpellAction {
	override val argc = 4
	override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
		if (ctx.spellCircle == null)
			throw MishapNoSpellCircle()
		for (i in 0..3)
			if (!ctx.spellCircle!!.aabb.contains(args.getVec3(i, argc))) {
				ctx.world.setBlockState(BlockPos(args.getVec3(i, argc)), Blocks.WHITE_CONCRETE.defaultState)
				ctx.caster.sendMessage(Text.literal(args.getVec3(i, argc).toString()))
			}
//				throw OutsideCircleMishap()
		val box1 = Box(args.getBlockPos(0, argc), args.getBlockPos(1, argc))
		val box2 = Box(args.getBlockPos(2, argc), args.getBlockPos(3, argc))
		if (box1.intersects(box2))
			throw OutsideCircleMishap() // change this mishap to overlapping domains
		if (box1.xLength != box2.xLength || box1.yLength != box2.yLength || box1.zLength != box2.zLength)
			throw OutsideCircleMishap() // change this mishap to invalid size domains
		return Triple(Spell(box1, box2), (box1.xLength * box1.yLength * box1.zLength * MediaConstants.DUST_UNIT + 3 * MediaConstants.CRYSTAL_UNIT).toInt(), listOf())
	}

	private data class Spell(val fromBox: Box, val toBox: Box) : RenderedSpell {
		override fun cast(ctx: CastingContext) {
			val fromLowerCorner = BlockPos(fromBox.minX, fromBox.minY, fromBox.minZ)
			val toLowerCorner = BlockPos(toBox.minX, toBox.minY, toBox.minZ)
			val size = Vec3i(fromBox.xLength.toInt(), fromBox.yLength.toInt(), fromBox.zLength.toInt())
			for (i in 0 until size.x) {
				for (j in 0 until size.y) {
					for (k in 0 until size.z) {
						val offset = Vec3i(i, j, k)
						val fromPoint = fromLowerCorner.add(offset)
						val toPoint = toLowerCorner.add(offset)
						if (ctx.world.getBlockEntity(fromPoint) != null)
							return
						if (ctx.world.getBlockEntity(toPoint) != null)
							return

						val fromState = ctx.world.getBlockState(fromPoint)
						val toState = ctx.world.getBlockState(toPoint)
						val canBreak = IXplatAbstractions.INSTANCE.isBreakingAllowed(ctx.world, fromPoint, fromState, ctx.caster) && IXplatAbstractions.INSTANCE.isBreakingAllowed(ctx.world, toPoint, toState, ctx.caster)
						if (!canBreak)
							continue
						if (fromState.block.hardness == -1f)
							continue
						if (toState.block.hardness == -1f)
							return

						ctx.world.setBlockState(fromPoint, toState)
						ctx.world.setBlockState(toPoint, fromState)
						ctx.world.markDirty(fromPoint)
						ctx.world.markDirty(toPoint)
					}
				}
			}
		}
	}
}
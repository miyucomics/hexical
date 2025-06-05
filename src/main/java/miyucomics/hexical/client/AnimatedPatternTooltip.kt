package miyucomics.hexical.client

import at.petrak.hexcasting.api.casting.math.HexPattern
import net.minecraft.client.item.TooltipData

data class AnimatedPatternTooltip(val color: Int, val pattern: HexPattern, val ancient: Boolean) : TooltipData
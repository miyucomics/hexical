package miyucomics.hexical.features.animated_scrolls

import at.petrak.hexcasting.api.casting.math.HexPattern
import net.minecraft.client.item.TooltipData

data class AnimatedPatternTooltip(val color: Int, val pattern: HexPattern, val state: Int, val glowing: Boolean) : TooltipData
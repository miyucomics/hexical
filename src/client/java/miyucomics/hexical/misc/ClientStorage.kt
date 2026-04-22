package miyucomics.hexical.misc

import net.minecraft.util.math.Vec3d

object ClientStorage {
	@JvmField
	var ticks: Int = 0
	var lesserSentinels: MutableList<Vec3d> = mutableListOf()
}
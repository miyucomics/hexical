package miyucomics.hexical.misc

import miyucomics.hexical.feature.media_log.MediaLogField
import net.minecraft.util.math.Vec3d

object ClientStorage {
	@JvmField
	var ticks: Int = 0
	var lesserSentinels: MutableList<Vec3d> = mutableListOf()
	var mediaLog: MediaLogField = MediaLogField()
}
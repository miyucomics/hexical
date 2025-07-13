package miyucomics.hexical.misc

import miyucomics.hexical.features.player.fields.MediaLogField
import net.minecraft.util.math.Vec3d

object ClientStorage {
	@JvmField
	var ticks: Int = 0
	var mediaLog: MediaLogField = MediaLogField()
	var lesserSentinels: MutableList<Vec3d> = mutableListOf()

	var fadingInLog = false
	var fadingInLogStart = 0
	var fadingInLogTweener = 0
}
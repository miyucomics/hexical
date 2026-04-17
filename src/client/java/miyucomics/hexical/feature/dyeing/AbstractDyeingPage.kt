package miyucomics.hexical.feature.dyeing

import com.google.gson.annotations.SerializedName
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import org.joml.Vector2i
import vazkii.patchouli.client.book.BookContentsBuilder
import vazkii.patchouli.client.book.BookEntry
import vazkii.patchouli.client.book.page.abstr.PageWithText
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

abstract class AbstractDyeingPage<T> : PageWithText() {
	var uncolored: String? = null
	var white: String? = null
	var orange: String? = null
	var magenta: String? = null
	@SerializedName("light_blue") var lightBlue: String? = null
	var yellow: String? = null
	var lime: String? = null
	var pink: String? = null
	var gray: String? = null
	@SerializedName("light_gray") var lightGray: String? = null
	var cyan: String? = null
	var purple: String? = null
	var blue: String? = null
	var brown: String? = null
	var green: String? = null
	var red: String? = null
	var black: String? = null

	@Transient private val renders: MutableList<Pair<T, Vector2i>> = mutableListOf()
	@Transient private var minY = 0
	@Transient private var maxY = 0

	abstract fun parseProvided(name: String): T
	abstract fun renderCustom(graphics: DrawContext, thing: T, x: Int, y: Int, mouseX: Int, mouseY: Int, tickDelta: Float)

	override fun build(level: World, entry: BookEntry, builder: BookContentsBuilder, pageNum: Int) {
		super.build(level, entry, builder, pageNum)
		minY = 0
		maxY = 0
		renders.clear()

		//val validOptions = listOfNotNull(uncolored, white, orange, magenta, lightBlue, yellow, lime, pink, gray, lightGray, cyan, purple, blue, brown, green, red, black).map(::parseProvided)
		val hues = listOfNotNull(red, orange, brown, yellow, lime, green, cyan, lightBlue, blue, purple, magenta, pink).map(::parseProvided)
		val numberOfHues = hues.size
		val radius = max(10f, numberOfHues * 18 / MathHelper.TAU)
		hues.forEachIndexed { index, thing ->
			val angle = index.toDouble() * MathHelper.TAU / numberOfHues
			val x = (cos(angle) * radius).toInt() + 68
			val y = (sin(angle) * radius).toInt()
			renders.add(thing to Vector2i(x, y))
			minY = min(minY, y)
			maxY = max(maxY, y)
		}

		if (uncolored != null)
			renders.add(parseProvided(uncolored!!) to Vector2i(68, (maxY + minY) / 2))

		val shades = listOfNotNull(black, gray, lightGray, white).map(::parseProvided)
		val numberOfShades = shades.size
		shades.forEachIndexed { index, thing ->
			val y = (index.toFloat() / (numberOfShades - 1)) * (maxY - minY) + minY
			renders.add(thing to Vector2i(13, y.toInt()))
		}
	}

	override fun render(graphics: DrawContext, mouseX: Int, mouseY: Int, pticks: Float) {
		super.render(graphics, mouseX, mouseY, pticks)
		for ((thing, pos) in this.renders)
			renderCustom(graphics, thing, pos.x, pos.y - minY + 5, mouseX, mouseY, pticks)
	}

	override fun getTextHeight() = this.maxY - this.minY + 18
}
package miyucomics.hexical.features.patchouli

import com.google.gson.annotations.SerializedName
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import org.joml.Vector2i
import vazkii.patchouli.client.book.BookContentsBuilder
import vazkii.patchouli.client.book.BookEntry
import vazkii.patchouli.client.book.gui.GuiBook
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
	abstract fun renderCustom(graphics: DrawContext, thing: T, x: Int, y: Int, mouseX: Int, mouseY: Int)

	override fun build(level: World, entry: BookEntry, builder: BookContentsBuilder, pageNum: Int) {
		super.build(level, entry, builder, pageNum)
		minY = 0
		maxY = 0
		renders.clear()

		val validOptions = listOfNotNull(uncolored, white, orange, magenta, lightBlue, yellow, lime, pink, gray, lightGray, cyan, purple, blue, brown, green, red, black).map(::parseProvided)
		val numberOfOptions = validOptions.size
		val radius = max(10f, numberOfOptions * 18 / MathHelper.TAU)
		validOptions.forEachIndexed { index, thing ->
			val angle = index.toDouble() * MathHelper.TAU / numberOfOptions
			val x = (cos(angle) * radius).toInt()
			val y = (sin(angle) * radius).toInt()
			renders.add(thing to Vector2i(x, y))
			minY = min(minY, y)
			maxY = max(maxY, y)
		}
	}

	override fun render(graphics: DrawContext, mouseX: Int, mouseY: Int, pticks: Float) {
		super.render(graphics, mouseX, mouseY, pticks)
		for (pair in this.renders)
			renderCustom(graphics, pair.first, pair.second.x + GuiBook.PAGE_WIDTH / 2, pair.second.y - minY + 5, mouseX, mouseY)
	}

	override fun getTextHeight() = this.maxY - this.minY + 20
}
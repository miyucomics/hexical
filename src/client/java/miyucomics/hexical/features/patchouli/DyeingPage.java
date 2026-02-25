package miyucomics.hexical.features.patchouli;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.joml.Vector2i;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageWithText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class DyeingPage extends PageWithText {
	IVariable uncolored, white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black;
	private transient List<ItemStack> validOptions;
	private final transient List<Pair<ItemStack, Vector2i>> renders = new ArrayList<>();
	private transient int spiralMinY = 0;
	private transient int spiralMaxY = 0;

	private static final double GOLDEN_RATIO = (1 + Math.sqrt(5)) / 2;
	private static final double GOLDEN_ANGLE = MathHelper.TAU / (GOLDEN_RATIO * GOLDEN_RATIO);
	private static final double SCALE = 12.0;

	@Override
	public void build(World level, BookEntry entry, BookContentsBuilder builder, int pageNum) {
		super.build(level, entry, builder, pageNum);
		List<ItemStack> validOptions = Stream.of(uncolored, white, orange, magenta, light_blue, yellow, lime, pink, gray, light_gray, cyan, purple, blue, brown, green, red, black)
			.filter(Objects::nonNull)
			.map(var -> var.as(ItemStack.class))
			.toList();

		int index = 0;
		spiralMinY = 0;
		spiralMaxY = 0;
		int numberOfOptions = validOptions.size();
		float radius = Math.max(10, numberOfOptions * 18 / MathHelper.TAU);

		renders.clear();
		for (ItemStack stack : validOptions) {
			double angle = (double) index * MathHelper.TAU / numberOfOptions;
			int x = (int) (Math.cos(angle) * radius);
			int y = (int) (Math.sin(angle) * radius);
			renders.add(new Pair<>(stack, new Vector2i(x - 8, y - 8)));
			spiralMinY = Math.min(spiralMinY, y);
			spiralMaxY = Math.max(spiralMaxY, y);
			index++;
		}
	}

	@Override
	public void render(DrawContext graphics, int mouseX, int mouseY, float pticks) {
		MatrixStack matrices = graphics.getMatrices();
		for (Pair<ItemStack, Vector2i> pair : this.renders)
			parent.renderItemStack(graphics, pair.getRight().x + GuiBook.PAGE_WIDTH / 2, pair.getRight().y - spiralMinY + 5, mouseX, mouseY, pair.getLeft());
		super.render(graphics, mouseX, mouseY, pticks);
	}

	@Override
	public int getTextHeight() {
		return spiralMaxY - spiralMinY + 20;
	}
}
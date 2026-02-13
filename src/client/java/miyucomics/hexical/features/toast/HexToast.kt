package miyucomics.hexical.features.toast

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.toast.Toast
import net.minecraft.client.toast.ToastManager
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class HexToast(val title: Text, val description: Text, val stack: ItemStack) : Toast {
	private var newlyCreated = true
	private var startTime: Long = 0

	override fun draw(graphics: DrawContext, manager: ToastManager, time: Long): Toast.Visibility {
		if (this.newlyCreated) {
			this.newlyCreated = false
			this.startTime = time
		}

		graphics.drawTexture(Toast.TEXTURE, 0, 0, 0, 32, width, height)
		graphics.drawText(manager.getClient().textRenderer, title, 30, 7, -11534256, false)
		graphics.drawText(manager.getClient().textRenderer, description, 30, 18, -16777216, false)
		graphics.drawItemWithoutEntity(stack, 8, 8)
		return if ((time - this.startTime).toDouble() >= 5000.0 * manager.notificationDisplayTimeMultiplier) Toast.Visibility.HIDE else Toast.Visibility.SHOW
	}
}
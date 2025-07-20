package miyucomics.hexical.mixin;

import miyucomics.hexical.features.curios.curios.HandbellCurioItemModel;
import miyucomics.hexical.features.curios.curios.FluteCurioItemModel;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ModelLoader.class)
public abstract class ModelLoaderMixin {
	@Shadow protected abstract void addModel(ModelIdentifier modelIdentifier);

	@Inject(method = "<init>", at = @At("TAIL"))
	private void injectModel(BlockColors blockColors, Profiler profiler, Map map, Map map2, CallbackInfo ci) {
		addModel(FluteCurioItemModel.heldFluteModel);
		addModel(HandbellCurioItemModel.heldHandbellModel);
	}
}
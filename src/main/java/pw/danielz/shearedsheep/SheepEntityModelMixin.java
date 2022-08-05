package pw.danielz.shearedsheep;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.math.Matrix4f;

import java.nio.FloatBuffer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepEntityModel.class)
public class SheepEntityModelMixin<T extends SheepEntity>
		extends QuadrupedEntityModel<T> {
	protected SheepEntityModelMixin(ModelPart root, boolean headScaled, float childHeadYOffset, float childHeadZOffset,
			float invertedChildHeadScale, float invertedChildBodyScale, int childBodyYOffset) {
		super(root, headScaled, childHeadYOffset, childHeadZOffset, invertedChildHeadScale, invertedChildBodyScale,
				childBodyYOffset);
	}

	private boolean isSheared = false;

	@Inject(at = @At("HEAD"), method = "animateModel(Lnet/minecraft/entity/passive/SheepEntity;FFF)V")
	private void animateModelInject(SheepEntity sheepEntity, float limbAngle, float limbDistance, float tickDelta, CallbackInfo ci) {
		isSheared = sheepEntity.isSheared();
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green,
			float blue, float alpha) {
		matrices.push();
		if (isSheared) {
			Matrix4f mat = new Matrix4f();
			float shear_amount = -0.5f;
			mat.readRowMajor(FloatBuffer.wrap(new float[] { 1f, shear_amount, 0f, -shear_amount, 0f, 1f, 0f, 0f, 0f, 0f,
					1f, 0f, 0f, 0f, 0f, 1f }));
			matrices.multiplyPositionMatrix(mat);
		}
		super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		matrices.pop();
	}
}

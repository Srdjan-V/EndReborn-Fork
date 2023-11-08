package endreborn.client.entity.render;

import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import endreborn.Reference;
import endreborn.common.entity.EntityEndGuard;

public class RenderEGuard extends RenderLiving<EntityEndGuard> {

    public static final ResourceLocation TEXTURES = new ResourceLocation(
            Reference.MODID + ":textures/entity/endguard.png");

    public static final Factory FACTORY = new Factory();

    public RenderEGuard(RenderManager manager) {
        super(manager, new ModelBlaze(), 0.5F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEndGuard entity) {
        return TEXTURES;
    }

    @Override
    protected void applyRotations(EntityEndGuard entityLiving, float p_77043_2_, float rotationYaw,
                                  float partialTicks) {
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }

    public static class Factory implements IRenderFactory<EntityEndGuard> {

        @Override
        public Render<? super EntityEndGuard> createRenderFor(RenderManager manager) {
            return new RenderEGuard(manager);
        }
    }
}

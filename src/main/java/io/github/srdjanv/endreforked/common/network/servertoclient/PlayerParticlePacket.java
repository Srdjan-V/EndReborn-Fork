package io.github.srdjanv.endreforked.common.network.servertoclient;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.jetbrains.annotations.Nullable;

import io.github.srdjanv.endreforked.client.ParticleHandler;

public class PlayerParticlePacket extends ParticlePacket {

    public PlayerParticlePacket() {}

    public PlayerParticlePacket(boolean remove, String id, int renderIterations,
                                @Nullable EnumParticleTypes particleType) {
        super(remove, id, renderIterations, particleType);
    }

    public static PlayerParticlePacket newParticle(String id, int renderIterations, EnumParticleTypes particleType) {
        return new PlayerParticlePacket(id, renderIterations, particleType);
    }

    public PlayerParticlePacket(String id, int renderIterations, EnumParticleTypes particleType) {
        this(false, id, renderIterations, particleType);
    }

    public static PlayerParticlePacket remove(String id) {
        return new PlayerParticlePacket(id);
    }

    public PlayerParticlePacket(String id) {
        this(true, id, 0, null);
    }

    public enum Handler implements IMessageHandler<PlayerParticlePacket, IMessage> {

        INSTANCE;

        @Override
        public IMessage onMessage(PlayerParticlePacket message, MessageContext ctx) {
            if (ctx.side.isServer()) return null;

            if (message.remove()) {
                ParticleHandler.removeFromRender(message.id());
            } else {
                final var particle = message.particleType();
                ParticleHandler.addToRender(
                        new ParticleHandler.RenderInfo(message.id(), message.renderIterations()) {

                            @Override
                            public void render() {
                                int color = 0x5900b3;
                                double xSpeed = (double) (color >> 16 & 255) / 255.0D;
                                double ySpeed = (double) (color >> 8 & 255) / 255.0D;
                                double zSpeed = (double) (color >> 0 & 255) / 255.0D;

                                var mc = Minecraft.getMinecraft();
                                var player = mc.player;
                                mc.world.spawnParticle(particle,
                                        player.posX + (player.getRNG().nextDouble() - 0.5D) * (double) player.width,
                                        player.posY + player.getRNG().nextDouble() * (double) player.height,
                                        player.posZ + (player.getRNG().nextDouble() - 0.5D) * (double) player.width,
                                        xSpeed, ySpeed, zSpeed);
                            }
                        });
            }

            return null;
        }
    }
}

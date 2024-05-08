package io.github.srdjanv.endreforked.common.network.servertoclient;

import io.github.srdjanv.endreforked.client.ParticleHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TpParticlePacket implements IMessage {
    private double posX;
    private double posY;
    private double posZ;

    private double newPosX;
    private double newPosY;
    private double newPosZ;

    private double width;
    private double height;

    public TpParticlePacket(boolean livingOldPos, EntityLivingBase livingBase,  double posX, double posY, double posZ) {
        if (livingOldPos) {
            this.posX = livingBase.posX;
            this.posY = livingBase.posY;
            this.posZ = livingBase.posZ;

            this.newPosX = posX;
            this.newPosY = posY;
            this.newPosZ = posZ;
        } else {
            this.newPosX = livingBase.posX;
            this.newPosY = livingBase.posY;
            this.newPosZ = livingBase.posZ;

            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
        }

        this.width = livingBase.width;
        this.height = livingBase.height;
    }

    public TpParticlePacket(double posX, double posY, double posZ, double newPosX, double newPosY, double newPosZ, double width, double height) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.newPosX = newPosX;
        this.newPosY = newPosY;
        this.newPosZ = newPosZ;
        this.width = width;
        this.height = height;
    }

    public TpParticlePacket() {
    }


    @Override public void fromBytes(ByteBuf buf) {
        posX = buf.readDouble();
        posY = buf.readDouble();
        posZ = buf.readDouble();

        newPosX = buf.readDouble();
        newPosY = buf.readDouble();
        newPosZ = buf.readDouble();

        width = buf.readDouble();
        height = buf.readDouble();
    }

    @Override public void toBytes(ByteBuf buf) {
        buf.writeDouble(posX);
        buf.writeDouble(posY);
        buf.writeDouble(posZ);

        buf.writeDouble(newPosX);
        buf.writeDouble(newPosY);
        buf.writeDouble(newPosZ);

        buf.writeDouble(width);
        buf.writeDouble(height);
    }

    public enum Handler implements IMessageHandler<TpParticlePacket, IMessage> {
        INSTANCE;

        @Override public IMessage onMessage(TpParticlePacket message, MessageContext ctx) {
            if (ctx.side.isServer()) return null;
            ParticleHandler.addToRender(
                    new ParticleHandler.RenderInfo("TPParticlePacket" + Minecraft.getSystemTime(), 1) {
                        @Override public void render() {
                            var mc = Minecraft.getMinecraft();
                            var random = mc.world.rand;
                            for (int j = 0; j < 128; ++j) {
                                double d6 = (double) j / 127.0D;
                                float f = (random.nextFloat() - 0.5F) * 0.2F;
                                float f1 = (random.nextFloat() - 0.5F) * 0.2F;
                                float f2 = (random.nextFloat() - 0.5F) * 0.2F;
                                double d3 = message.posX + (message.newPosX - message.posX) * d6 + (random.nextDouble() - 0.5D) * message.width * 2.0D;
                                double d4 = message.posY + (message.newPosY - message.posY) * d6 + random.nextDouble() * message.height;
                                double d5 = message.posZ + (message.newPosZ - message.posZ) * d6 + (random.nextDouble() - 0.5D) * message.width * 2.0D;
                                mc.world.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, f, f1, f2);
                            }
                        }
                    });

            return null;
        }
    }
}

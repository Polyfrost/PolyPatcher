package club.sk1er.patcher.mixins.performance.forge;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.lang.ref.WeakReference;
import java.util.List;

import static net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec.INBOUNDPACKETTRACKER;

@Mixin(value = FMLIndexedMessageToMessageCodec.class, remap = false)
public abstract class FMLIndexedMessageToMessageCodecMixin_FastUtil<A> extends MessageToMessageCodec<FMLProxyPacket, A> {
    //#if MC==10809
    @Shadow
    public abstract void encodeInto(ChannelHandlerContext ctx, A msg, ByteBuf target) throws Exception;
    @Shadow
    protected abstract void testMessageValidity(FMLProxyPacket msg);
    @Shadow
    public abstract void decodeInto(ChannelHandlerContext ctx, ByteBuf source, A msg);
    @Unique
    private final Byte2ObjectMap<Class<? extends A>> patcher$discriminators = new Byte2ObjectOpenHashMap<>();
    @Unique
    private final Object2ByteMap<Class<? extends A>> patcher$types = new Object2ByteOpenHashMap<>();

    /**
     * @author Microcontrollers
     * @reason Use FastUtil
     */
    @Overwrite
    public FMLIndexedMessageToMessageCodec<A> addDiscriminator(int discriminator, Class<? extends A> type) {
        patcher$discriminators.put((byte)discriminator, type);
        patcher$types.put(type, (byte)discriminator);
        return (FMLIndexedMessageToMessageCodec) (Object) this;
    }

    /**
     * @author Microcontrollers
     * @reason Use FastUtil
     */
    @Overwrite
    @Override
    protected final void encode(ChannelHandlerContext ctx, A msg, List<Object> out) throws Exception {
        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        @SuppressWarnings("unchecked") // Stupid unnecessary cast I can't seem to kill
        Class<? extends A> clazz = (Class<? extends A>) msg.getClass();
        byte discriminator = patcher$types.get(clazz);
        buffer.writeByte(discriminator);
        this.encodeInto(ctx, msg, buffer);
        FMLProxyPacket proxy = new FMLProxyPacket(buffer/*.copy()*/, ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
        WeakReference<FMLProxyPacket> ref = ctx.attr(INBOUNDPACKETTRACKER).get().get();
        FMLProxyPacket old = ref == null ? null : ref.get();
        if (old != null) {
            proxy.setDispatcher(old.getDispatcher());
        }
        out.add(proxy);
    }

    /**
     * @author Microcontrollers
     * @reason Use FastUtil
     */
    @Overwrite
    @Override
    protected final void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception {
        this.testMessageValidity(msg);
        ByteBuf payload = msg.payload().duplicate();
        if (payload.readableBytes() < 1) {
            FMLLog.log(Level.ERROR, "The FMLIndexedCodec has received an empty buffer on channel %s, likely a result of a LAN server issue. Pipeline parts : %s", ctx.channel().attr(NetworkRegistry.FML_CHANNEL), ctx.pipeline().toString());
        }
        byte discriminator = payload.readByte();
        Class<? extends A> clazz = patcher$discriminators.get(discriminator);
        if(clazz == null) {
            throw new NullPointerException("Undefined message for discriminator " + discriminator + " in channel " + msg.channel());
        }
        A newMsg = clazz.newInstance();
        ctx.attr(INBOUNDPACKETTRACKER).get().set(new WeakReference<FMLProxyPacket>(msg));
        this.decodeInto(ctx, payload.slice(), newMsg);
        out.add(newMsg);
    }
    //#endif
}

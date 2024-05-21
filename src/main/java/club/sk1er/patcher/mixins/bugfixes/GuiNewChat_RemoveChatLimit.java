package club.sk1er.patcher.mixins.bugfixes;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GuiNewChat.class)
public class GuiNewChat_RemoveChatLimit {
    @ModifyConstant(method = "setChatLine", constant = @Constant(intValue = 100))
    private int patcher$removeChatLimit(int original) {
        if (PatcherConfig.removeChatMessageLimit) return Integer.MAX_VALUE;
        return original;
    }
}

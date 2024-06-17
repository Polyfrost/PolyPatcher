package club.sk1er.patcher.hooks;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

@SuppressWarnings("unused")
public class GuiIngameForgeHook {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static int fixHealthMargin(int original) {
        if (mc.thePlayer.isPotionActive(Potion.poison)) original -= 36;
        else if (mc.thePlayer.isPotionActive(Potion.wither)) original -= 108;
        return original;
    }
}

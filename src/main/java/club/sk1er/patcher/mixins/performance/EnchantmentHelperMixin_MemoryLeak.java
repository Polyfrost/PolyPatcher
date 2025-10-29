package club.sk1er.patcher.mixins.performance;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin_MemoryLeak {
    //#if MC==10809
    @Shadow
    @Final
    private static EnchantmentHelper.HurtIterator ENCHANTMENT_ITERATOR_HURT;

    @Shadow
    @Final
    private static EnchantmentHelper.DamageIterator ENCHANTMENT_ITERATOR_DAMAGE;

    @Shadow
    @Final
    private static EnchantmentHelper.ModifierDamage enchantmentModifierDamage;

    @Shadow
    @Final
    private static EnchantmentHelper.ModifierLiving enchantmentModifierLiving;

    @Inject(method = "getEnchantmentModifierDamage", at = @At("TAIL"))
    private static void patcher$fixMemoryLeak(ItemStack[] stack, DamageSource source, CallbackInfoReturnable<Integer> cir) {
        EnchantmentHelperMixin_MemoryLeak.enchantmentModifierDamage.source = null;
    }

    @Inject(method = "getModifierForCreature", at = @At("TAIL"))
    private static void patcher$fixMemoryLeak(ItemStack p_152377_0_, EnumCreatureAttribute p_152377_1_, CallbackInfoReturnable<Float> cir) {
        EnchantmentHelperMixin_MemoryLeak.enchantmentModifierLiving.entityLiving = null;
    }

    @Inject(method = "applyThornEnchantments", at = @At("TAIL"))
    private static void patcher$fixMemoryLeak(EntityLivingBase p_151384_0_, Entity p_151384_1_, CallbackInfo ci) {
        EnchantmentHelperMixin_MemoryLeak.ENCHANTMENT_ITERATOR_HURT.attacker = null;
        EnchantmentHelperMixin_MemoryLeak.ENCHANTMENT_ITERATOR_HURT.user = null;
    }

    @Inject(method = "applyArthropodEnchantments", at = @At("TAIL"))
    private static void patcher$fixMemoryLeak2(EntityLivingBase p_151385_0_, Entity p_151385_1_, CallbackInfo ci) {
        EnchantmentHelperMixin_MemoryLeak.ENCHANTMENT_ITERATOR_DAMAGE.target = null;
        EnchantmentHelperMixin_MemoryLeak.ENCHANTMENT_ITERATOR_DAMAGE.user = null;
    }
    //#endif
}

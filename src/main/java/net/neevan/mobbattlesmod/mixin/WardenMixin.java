package net.neevan.mobbattlesmod.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import net.neevan.mobbattlesmod.MobBattlesMod;
import net.neevan.mobbattlesmod.cataclysm.CataclysmUtil;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Warden.class)
public class WardenMixin extends Monster {
    protected WardenMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "canTargetEntity", at = @At("HEAD"),cancellable = true)
    public void canTargetEntityInject(Entity entity, CallbackInfoReturnable<Boolean> cir){
        if(!CataclysmUtil.isCataclysmBoss(entity)){
            cir.setReturnValue(false);
        }
    }
}

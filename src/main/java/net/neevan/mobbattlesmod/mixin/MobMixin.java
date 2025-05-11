package net.neevan.mobbattlesmod.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import net.neevan.mobbattlesmod.cataclysm.CataclysmUtil;
import net.neevan.mobbattlesmod.data.MobBattlesData;
import net.neevan.mobbattlesmod.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.neevan.mobbattlesmod.cataclysm.CataclysmUtil.isCataclysmBoss;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    public void registerGoalsInject(CallbackInfo ci){
        Mob mob = (Mob) (Object) this;
        if(!isCataclysmBoss(mob)){
            mob.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(mob, Mob.class, 0, false, false, CataclysmUtil::isCataclysmBoss));
        } else {
            mob.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(mob, Mob.class, 0, false, false, livingEntity -> !isCataclysmBoss(livingEntity)));
        }
    }

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    public void setTargetInject(LivingEntity target, CallbackInfo ci){

        Mob mob = (Mob) (Object) this;

        //if mob is NOT a boss & target is NOT a boss & mob is NOT special
        if(!CataclysmUtil.isCataclysmBoss(mob) && !CataclysmUtil.isCataclysmBoss(target) && !Util.isSpecialMob(mob)){
            ci.cancel();
        }

    }

    @Override
    public boolean canAttack(LivingEntity target) {

        Mob mob = (Mob) (Object) this;

        if(!isCataclysmBoss(mob)){
            return isCataclysmBoss(target);
        }
        return super.canAttack(target);
    }

    @Override
    public void tick() {

        Mob mob = (Mob) (Object) this;


        if(!CataclysmUtil.isCataclysmBoss(mob) &&
                mob.getServer() != null &&
                mob.getServer().getLevel(Level.OVERWORLD) != null &&
                mob.isAlive()){

            MobBattlesData data = MobBattlesData.get(mob.getServer().getLevel(Level.OVERWORLD));

            if(!data.bossExists() && !Util.isSpecialMob(mob)){

                return;
            }
        }

        super.tick();
    }


}

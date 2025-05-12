package net.neevan.mobbattlesmod.mixin;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;
import net.neevan.mobbattlesmod.cataclysm.CataclysmUtil;
import net.neevan.mobbattlesmod.data.MobBattlesData;
import net.neevan.mobbattlesmod.util.Util;
import org.spongepowered.asm.mixin.Mixin;

import java.util.logging.Level;

@Mixin(Witch.class)
public class WitchMixin implements RangedAttackMob {
    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {

        Witch witch = (Witch) (Object) this;

        if (!witch.isDrinkingPotion()) {
            Vec3 vec3 = target.getDeltaMovement();
            double d0 = target.getX() + vec3.x - witch.getX();
            double d1 = target.getEyeY() - 1.1F - witch.getY();
            double d2 = target.getZ() + vec3.z - witch.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            Holder<Potion> holder = Potions.HARMING;

            if (target instanceof Warden) {
                ServerLevel serverLevel = Util.getServerLevelFromMob(target);
                if(serverLevel != null){

                    MobBattlesData data = MobBattlesData.get(serverLevel);
                    if(data.bossExists() && data.getBossUUID() != null){

                        Entity maybeBoss = ((ServerLevel) target.level()).getEntity(data.getBossUUID());
                        if(!Util.tooClose(target, maybeBoss)){
                            if(!target.hasEffect(MobEffects.DAMAGE_RESISTANCE)){
                                holder = Potions.STRONG_TURTLE_MASTER;
                            }
                            if(!target.hasEffect(MobEffects.DAMAGE_BOOST)){
                                holder = Potions.STRONG_STRENGTH;
                            }
                            if(!target.hasEffect(MobEffects.REGENERATION)){
                                holder = Potions.STRONG_REGENERATION;
                            }
                        }
                    }
                }
            else {

                if (d3 >= 8.0 && !target.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                    holder = Potions.SLOWNESS;
                } else if (target.getHealth() >= 8.0F && !target.hasEffect(MobEffects.POISON)) {
                    holder = Potions.POISON;
                } else if (d3 <= 3.0 && !target.hasEffect(MobEffects.WEAKNESS) && RandomSource.create().nextFloat() < 0.25F) {
                    holder = Potions.WEAKNESS;
                }

                }

            ThrownPotion thrownpotion = new ThrownPotion(witch.level(), witch);
            thrownpotion.setItem(PotionContents.createItemStack(Items.SPLASH_POTION, holder));
            thrownpotion.setXRot(thrownpotion.getXRot() - -20.0F);
            thrownpotion.shoot(d0, d1 + d3 * 0.2, d2, 0.75F, 8.0F);
            if (!witch.isSilent()) {
                witch.level()
                        .playSound(
                                null,
                                witch.getX(),
                                witch.getY(),
                                witch.getZ(),
                                SoundEvents.WITCH_THROW,
                                witch.getSoundSource(),
                                1.0F,
                                0.8F + RandomSource.create().nextFloat() * 0.4F
                        );
            }

            witch.level().addFreshEntity(thrownpotion);
        }
    }
}
}

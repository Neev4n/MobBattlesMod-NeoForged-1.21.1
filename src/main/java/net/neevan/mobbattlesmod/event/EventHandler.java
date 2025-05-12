package net.neevan.mobbattlesmod.event;


import com.google.common.eventbus.Subscribe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import net.neevan.mobbattlesmod.MobBattlesMod;
import net.neevan.mobbattlesmod.cataclysm.CataclysmUtil;
import net.neevan.mobbattlesmod.data.MobBattlesData;
import net.neevan.mobbattlesmod.util.Util;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;

@EventBusSubscriber(modid = "mobbattlesmod")
public class EventHandler {

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event){

        Entity entity = event.getEntity();

        if((entity instanceof Mob boss) && event.getLevel() instanceof ServerLevel serverLevel){

            //cataclysm boss
            if(CataclysmUtil.isCataclysmBoss(boss)){

                MobBattlesData data = MobBattlesData.get(serverLevel);
                data.setBossExists(true);
                data.setBossUUID(boss.getUUID());

                MobBattlesMod.LOGGER.info("boss spanwed: " + boss.toString());

                for (Entity entityOther : serverLevel.getEntities().getAll()) {
                    if (entityOther instanceof Mob mob && !entityOther.is(boss)) {

                        if(mob instanceof Warden warden){
                            LivingEntity wardenTarget = warden.getTarget();

                            if(wardenTarget != null && !CataclysmUtil.isCataclysmBoss(wardenTarget)){
                                warden.getBrain().eraseMemory(MemoryModuleType.ROAR_TARGET);
                                warden.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, boss);
                                warden.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
                                for(int i = 0; i < 10; i++){
                                    warden.increaseAngerAt(boss);
                                }
                            }
                        } else {

                            mob.setTarget(boss);
                        }


                    }


                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityLeaveLevel(EntityLeaveLevelEvent event){
        Entity entity = event.getEntity();

        if(entity instanceof Mob boss && event.getLevel() instanceof ServerLevel serverLevel){

            //cataclysm boss
            if(CataclysmUtil.isCataclysmBoss(boss)){
                MobBattlesMod.LOGGER.info("boss left: " + boss.toString());

                MobBattlesData data = MobBattlesData.get(serverLevel);
                data.setBossExists(false);
                data.setBossUUID(null);

            }
        }

    }

}

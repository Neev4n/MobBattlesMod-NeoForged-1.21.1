package net.neevan.mobbattlesmod.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.neevan.mobbattlesmod.cataclysm.CataclysmUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Util {


    // List to store valid fighter mobs
    private static List<EntityType<?>> validEntityTypes = new ArrayList<>();
    private static List<EntityType<?>> specialEntityTypes = new ArrayList<>();

    // Static block to initialize valid entity types
    static {
        validEntityTypes.add(EntityType.WARDEN);
        validEntityTypes.add(EntityType.WITHER);
        validEntityTypes.add(EntityType.WITCH);
        validEntityTypes.add(EntityType.GUARDIAN);

        specialEntityTypes.add(EntityType.WITCH);
        // Add other fighter mobs here...
    }

    public static boolean isSpecialMob(Entity entity) {
        if (entity == null || (entity instanceof Mob)) {
            return false;  // Ensure mob is not null
        }

        // Check if the mob's EntityType is in the list of valid fighter mobs
        return specialEntityTypes.contains(entity.getType());
    }

    public static boolean tooClose(Entity mob, Entity target){
        if(mob == null || target == null){
            return false;
        }
        return mob.distanceTo(target) < 5;

    }

    @Nullable
    public static ServerLevel getServerLevelFromMob(LivingEntity living){
        if(living.getServer() != null){
            return living.getServer().getLevel(living.level().dimension());
        }

        return null;
    }

}

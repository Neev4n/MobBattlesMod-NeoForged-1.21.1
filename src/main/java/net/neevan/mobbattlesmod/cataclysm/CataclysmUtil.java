package net.neevan.mobbattlesmod.cataclysm;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ender_Guardian_Entity;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.The_Harbinger_Entity;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.The_Leviathan.The_Leviathan_Entity;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Ancient_Remnant.Ancient_Remnant_Entity;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.NewNetherite_Monstrosity.Netherite_Monstrosity_Entity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;



public class CataclysmUtil {


    public static boolean isCataclysmBoss(Entity entity){

        return ((entity instanceof Netherite_Monstrosity_Entity) ||
                (entity instanceof Ender_Guardian_Entity) ||
                (entity instanceof Ancient_Remnant_Entity) ||
                (entity instanceof Ignis_Entity) ||
                (entity instanceof The_Harbinger_Entity) ||
                (entity instanceof The_Leviathan_Entity));

    }



}

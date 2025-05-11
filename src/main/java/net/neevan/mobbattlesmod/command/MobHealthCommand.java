package net.neevan.mobbattlesmod.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Supplier;

public class MobHealthCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("mobhealth")
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            ServerLevel level = source.getLevel(); // Get the world from the CommandSourceStack

                            // Create a StringBuilder to collect health data
                            //StringBuilder healthInfo = new StringBuilder("Health of all entities:\n");
                            double totalHealth = 0;
                            // Loop through all entities using the getAll method
                            for (Entity entity : level.getEntities().getAll()) {
                                if (entity instanceof LivingEntity livingEntity) {
//                                    healthInfo.append(String.format("%s: %.2f/%s health\n",
//                                            entity.getName().getString(),
//                                            livingEntity.getHealth(),
//                                            livingEntity.getMaxHealth()));

                                    totalHealth += livingEntity.getHealth();
                                }
                            }

                            double finalTotalHealth = totalHealth;
                            context.getSource().sendSuccess(() ->
                                    Component.literal("Army health: " + finalTotalHealth), true
                            );

                            // Send the collected health info to the player's chat
//                            source.sendSuccess((Supplier<Component>) Component.literal(healthInfo.toString()), false);
                            return 1; // Command executed successfully
                        })
        );
    }
}
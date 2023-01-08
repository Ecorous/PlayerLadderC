package org.ecorous.pladderc.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class SitCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("playersit").then(argument("player", EntityArgumentType.player()).executes(context -> {
            PlayerEntity player = context.getSource().getPlayer();
            PlayerEntity playerX = EntityArgumentType.getPlayer(context, "player");
            if (player == playerX) return 0;
            if(player instanceof PlayerEntity && !context.getSource().getWorld().isClient)
            {
                if(playerX.distanceTo(player) <= 5)
                {
                    ServerPlayerEntity passenger = (ServerPlayerEntity) playerX;

                    while (passenger.getFirstPassenger() != null && passenger.getFirstPassenger() != player)
                    {
                        passenger = (ServerPlayerEntity) passenger.getFirstPassenger();
                    }

                    passenger.networkHandler.sendPacket(new EntityPassengersSetS2CPacket(player));

                    player.startRiding(passenger);

                    passenger.networkHandler.sendPacket(new EntityPassengersSetS2CPacket(passenger));
                }
            }
            return 1;
        })));
    }
}

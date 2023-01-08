package org.ecorous.pladderc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.ecorous.pladderc.commands.SitCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PLadder implements ModInitializer
{
	public static final Logger LOGGER = LoggerFactory.getLogger("playerladderc");
	@Override
	public void onInitialize()
	{
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
		{
			if(handler.player.hasVehicle() && handler.player.getVehicle() instanceof PlayerEntity) handler.player.dismountVehicle();
		});
		CommandRegistrationCallback.EVENT.register(((dispatcher, buildContext, environment) -> {
			SitCommand.register(dispatcher, environment);
		}));
		/*UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) ->
		{
			if(entity instanceof PlayerEntity && !world.isClient)
			{
				if(hand == Hand.MAIN_HAND && player.getMainHandStack() == ItemStack.EMPTY && entity.distanceTo(player) < 4)
				{
					ServerPlayerEntity passenger = (ServerPlayerEntity) entity;

					while (passenger.getFirstPassenger() != null && passenger.getFirstPassenger() != player)
					{
						passenger = (ServerPlayerEntity) passenger.getFirstPassenger();
					}

					passenger.networkHandler.sendPacket(new EntityPassengersSetS2CPacket(player));

					player.startRiding(passenger);

					passenger.networkHandler.sendPacket(new EntityPassengersSetS2CPacket(passenger));
				}
			}
			return ActionResult.PASS;
		});*/
	}
}

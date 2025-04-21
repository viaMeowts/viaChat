package com.viameowts.viachat;

import com.viameowts.viachat.command.ChatModeCommand;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

@Environment(EnvType.SERVER)
public class viaChatServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        viaChat.LOGGER.info("Initializing viaChat Server!");
        ChatHandler.register();

        CommandRegistrationCallback.EVENT.register(ChatModeCommand::register);
        viaChat.LOGGER.info("Registered viaChat commands.");
    }
}

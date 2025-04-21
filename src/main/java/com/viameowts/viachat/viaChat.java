package com.viameowts.viachat;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class viaChat implements ModInitializer {
	public static final String MOD_ID = "viachat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final double LOCAL_CHAT_RADIUS = 100.0;
    public static final double LOCAL_CHAT_RADIUS_SQUARED = LOCAL_CHAT_RADIUS * LOCAL_CHAT_RADIUS;

    public static final Map<UUID, Boolean> playerChatModePref = new HashMap<>();

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing viaChat!");
        Lang.initialize();
	}

    public static boolean getPlayerPrefersPrefixForGlobal(UUID playerUuid) {
        return playerChatModePref.getOrDefault(playerUuid, true);
    }
}

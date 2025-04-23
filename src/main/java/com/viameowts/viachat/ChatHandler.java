package com.viameowts.viachat;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class ChatHandler {

    public static void register() {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            onChatMessage(message, sender);
            return false;
        });
    }

    private static void onChatMessage(SignedMessage message, ServerPlayerEntity sender) {
        String rawMessage = message.getContent().getString();
        MinecraftServer server = sender.getServer();
        if (server == null) return;

        boolean hasPrefix = rawMessage.startsWith("!");
        boolean prefersPrefixForGlobal = viaChat.getPlayerPrefersPrefixForGlobal(sender.getUuid());

        boolean isEffectivelyGlobal;
        String messageContent;

        if (hasPrefix) {
            isEffectivelyGlobal = prefersPrefixForGlobal;
            messageContent = rawMessage.substring(1).trim();
        } else {
            isEffectivelyGlobal = !prefersPrefixForGlobal;
            messageContent = rawMessage;
        }

        if (messageContent.isEmpty()) return;

        Double squareDistance = isEffectivelyGlobal ? null : viaChat.LOCAL_CHAT_RADIUS_SQUARED;

        handleMessage(server, sender, messageContent, squareDistance);

        viaChat.LOGGER.info("<{}> {}", sender.getName().getString(), rawMessage);
    }

    private static void handleMessage(
            MinecraftServer server,
            ServerPlayerEntity sender,
            String messageContent,
            @Nullable Double squareDistance) {
        boolean isGlobal = squareDistance == null;
        MessageFormatting formatting = isGlobal ? viaChat.GLOBAL_FORMATTING : viaChat.LOCAL_FORMATTING;
        Text message = formatting.format(isGlobal ? "[G]" : "[L]", sender.getDisplayName().copy(), messageContent);

        if (isGlobal) {
            server.getPlayerManager().broadcast(message, false);
        } else {
            var players = sender.getWorld().getPlayers();
            var originPosition = sender.getPos();

            AtomicBoolean foundPlayers = new AtomicBoolean(false);

            sender.sendMessage(message);
            players.stream()
                    .filter(player -> player.squaredDistanceTo(originPosition) <= squareDistance)
                    .filter(player -> player != sender)
                    .forEach(player -> {
                        player.sendMessage(message, false);

                        // Ignore sneaking players
                        if (!player.isSneaking()) {
                            foundPlayers.set(true);
                        }
                    });

            if (!foundPlayers.get()) {
                sender.sendMessage(Lang.get("chat.no_one_heard"));
            }
        }
    }
}
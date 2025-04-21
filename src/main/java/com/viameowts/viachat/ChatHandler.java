package com.viameowts.viachat;

import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class ChatHandler {

    public static void register() {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            onChatMessage(message, sender, params);
            return false;
        });
    }

    private static void onChatMessage(SignedMessage message, ServerPlayerEntity sender, net.minecraft.network.message.MessageType.Parameters params) {
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

        if (isEffectivelyGlobal) {
            handleGlobalMessage(server, sender, messageContent);
        } else {
            handleLocalMessage(server, sender, messageContent);
        }

        viaChat.LOGGER.info("<{}> {}", sender.getName().getString(), rawMessage);
    }

    private static void handleGlobalMessage(MinecraftServer server, ServerPlayerEntity sender, String messageContent) {
        Formatting prefixColor = Formatting.YELLOW;
        Formatting nameColor = Formatting.YELLOW;
        Formatting msgColor = Formatting.WHITE;
        String prefix = "[G]";

        MutableText formattedMessage = Text.literal(prefix + " ").formatted(prefixColor)
                .append(sender.getDisplayName().copy().formatted(nameColor))
                .append(Text.literal(": ").formatted(prefixColor))
                .append(Text.literal(messageContent).formatted(msgColor));

        server.getPlayerManager().broadcast(formattedMessage, false);
    }

    private static void handleLocalMessage(MinecraftServer server, ServerPlayerEntity sender, String messageContent) {
        Formatting prefixColor = Formatting.GREEN;
        Formatting nameColor = Formatting.GREEN;
        Formatting msgColor = Formatting.GRAY;
        String prefix = "[L]";
        double radiusSquared = viaChat.LOCAL_CHAT_RADIUS_SQUARED;

        MutableText formattedMessage = Text.literal(prefix + " ").formatted(prefixColor)
                .append(sender.getDisplayName().copy().formatted(nameColor))
                .append(Text.literal(": ").formatted(prefixColor))
                .append(Text.literal(messageContent).formatted(msgColor));

        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        World senderWorld = sender.getWorld();

        for (ServerPlayerEntity recipient : players) {
            if (recipient == sender) {
                 recipient.sendMessage(formattedMessage, false);
                 continue;
            }
            if (recipient.getWorld() == senderWorld &&
                sender.squaredDistanceTo(recipient) <= radiusSquared) {
                recipient.sendMessage(formattedMessage, false);
            }
        }
    }
}
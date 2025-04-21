package com.viameowts.viachat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.viameowts.viachat.Lang;
import com.viameowts.viachat.viaChat;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChatModeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("viachat")
                .then(CommandManager.literal("local")
                        .then(CommandManager.literal("!")
                                .executes(ChatModeCommand::setModeLocal)
                        )
                )
                .then(CommandManager.literal("global")
                        .then(CommandManager.literal("!")
                                .executes(ChatModeCommand::setModeGlobal)
                        )
                )
                .then(CommandManager.literal("lang")
                        .then(CommandManager.argument("language", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    builder.suggest("en");
                                    builder.suggest("ru");
                                    return builder.buildFuture();
                                })
                                .executes(ChatModeCommand::setLanguage)
                        )
                        .executes(ChatModeCommand::showCurrentLanguage)
                )
                .executes(ChatModeCommand::showCurrentMode)
        );
    }

    private static int setModeLocal(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendError(Lang.get("command.error.player_only"));
            return 0;
        }

        viaChat.playerChatModePref.put(player.getUuid(), false);

        source.sendFeedback(() -> Lang.get("command.set.prefix_local"), false);

        return 1;
    }

    private static int setModeGlobal(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendError(Lang.get("command.error.player_only"));
            return 0;
        }

        viaChat.playerChatModePref.put(player.getUuid(), true);

        source.sendFeedback(() -> Lang.get("command.set.prefix_global"), false);

        return 1;
    }

     private static int showCurrentMode(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
         if (!(source.getEntity() instanceof ServerPlayerEntity player)) {
            source.sendError(Lang.get("command.error.player_only"));
            return 0;
        }
        boolean currentPref = viaChat.getPlayerPrefersPrefixForGlobal(player.getUuid());
        Text feedback;
         if (currentPref) {
            feedback = Lang.get("command.current.prefix_global");
        } else {
             feedback = Lang.get("command.current.prefix_local");
        }
        source.sendFeedback(() -> feedback, false);
        return 1;
    }

    private static int setLanguage(CommandContext<ServerCommandSource> context) {
        String langArg = StringArgumentType.getString(context, "language");
        ServerCommandSource source = context.getSource();

        if (Lang.setLang(langArg)) {
            Text feedback = Lang.getMutable("command.lang.set")
                                .append(Text.literal(langArg).formatted(Formatting.AQUA));
            source.sendFeedback(() -> feedback, true);
            return 1;
        } else {
            source.sendError(Lang.get("command.lang.invalid"));
            return 0;
        }
    }

    private static int showCurrentLanguage(CommandContext<ServerCommandSource> context) {
         ServerCommandSource source = context.getSource();
         Text feedback = Lang.getMutable("command.lang.current")
                             .append(Text.literal(Lang.getCurrentLang()).formatted(Formatting.AQUA));
         source.sendFeedback(() -> feedback, false);
         return 1;
    }
}

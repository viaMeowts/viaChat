package com.viameowts.viachat;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;

public class Lang {

    private static String currentLang = "en";

    private static final Map<String, Text> enMessages = new HashMap<>();
    private static final Map<String, Text> ruMessages = new HashMap<>();

    public static void initialize() {
        enMessages.put("command.error.player_only", Text.literal("This command can only be run by a player.").formatted(Formatting.RED));
        enMessages.put("command.set.prefix_local", Text.literal("Chat mode set: ").formatted(Formatting.GRAY)
                .append(Text.literal("Use '!' for Local chat").formatted(Formatting.GREEN))
                .append(Text.literal(".").formatted(Formatting.GRAY)));
        enMessages.put("command.set.prefix_global", Text.literal("Chat mode set: ").formatted(Formatting.GRAY)
                .append(Text.literal("Use '!' for Global chat").formatted(Formatting.YELLOW))
                .append(Text.literal(" (default).").formatted(Formatting.GRAY)));
        enMessages.put("command.current.prefix_local", Text.literal("Current mode: ").formatted(Formatting.GRAY)
                .append(Text.literal("'!' means Local chat").formatted(Formatting.GREEN)));
        enMessages.put("command.current.prefix_global", Text.literal("Current mode: ").formatted(Formatting.GRAY)
                .append(Text.literal("'!' means Global chat").formatted(Formatting.YELLOW)));
        enMessages.put("command.lang.set", Text.literal("Language set to: ").formatted(Formatting.GRAY));
        enMessages.put("command.lang.invalid", Text.literal("Invalid language. Use 'en' or 'ru'.").formatted(Formatting.RED));
        enMessages.put("command.lang.current", Text.literal("Current language: ").formatted(Formatting.GRAY));

        ruMessages.put("command.error.player_only", Text.literal("Эту команду может использовать только игрок.").formatted(Formatting.RED));
        ruMessages.put("command.set.prefix_local", Text.literal("Режим чата изменен: ").formatted(Formatting.GRAY)
                .append(Text.literal("Используйте '!' для Локального чата").formatted(Formatting.GREEN))
                .append(Text.literal(".").formatted(Formatting.GRAY)));
        ruMessages.put("command.set.prefix_global", Text.literal("Режим чата изменен: ").formatted(Formatting.GRAY)
                .append(Text.literal("Используйте '!' для Глобального чата").formatted(Formatting.YELLOW))
                .append(Text.literal(" (по умолчанию).").formatted(Formatting.GRAY)));
        ruMessages.put("command.current.prefix_local", Text.literal("Текущий режим: ").formatted(Formatting.GRAY)
                .append(Text.literal("'!' означает Локальный чат").formatted(Formatting.GREEN)));
        ruMessages.put("command.current.prefix_global", Text.literal("Текущий режим: ").formatted(Formatting.GRAY)
                .append(Text.literal("'!' означает Глобальный чат").formatted(Formatting.YELLOW)));
        ruMessages.put("command.lang.set", Text.literal("Язык изменен на: ").formatted(Formatting.GRAY));
        ruMessages.put("command.lang.invalid", Text.literal("Неверный язык. Используйте 'en' или 'ru'.").formatted(Formatting.RED));
        ruMessages.put("command.lang.current", Text.literal("Текущий язык: ").formatted(Formatting.GRAY));
    }

    public static Text get(String key) {
        Map<String, Text> messages = currentLang.equals("ru") ? ruMessages : enMessages;
        Text defaultText = enMessages.getOrDefault(key, Text.literal(key).formatted(Formatting.RED));
        return messages.getOrDefault(key, defaultText);
    }

    public static MutableText getMutable(String key) {
        return get(key).copy();
    }

    public static boolean setLang(String lang) {
        if (lang.equalsIgnoreCase("en") || lang.equalsIgnoreCase("ru")) {
            currentLang = lang.toLowerCase();
            viaChat.LOGGER.info("viaChat language set to: " + currentLang);
            return true;
        }
        return false;
    }

    public static String getCurrentLang() {
        return currentLang;
    }
}

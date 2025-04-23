package com.viameowts.viachat;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public record MessageFormatting(
        Formatting prefix,
        Formatting name,
        Formatting msg
) {
    public MutableText format(String prefix, MutableText name, String message) {
        return Text.literal(prefix + " ").formatted(this.prefix)
                .append(name.formatted(this.name))
                .append(Text.literal(": ").formatted(this.prefix))
                .append(Text.literal(message).formatted(this.msg));
    }
}

package dev.necro.necrocore.callbacks;

import lombok.Data;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
public class CanSkipCallback {
    private final CommandSender sender;
    private final boolean canSkip;

    @Nullable
    private final List<String> reason;
}

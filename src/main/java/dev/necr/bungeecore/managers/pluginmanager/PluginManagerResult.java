package dev.necr.bungeecore.managers.pluginmanager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SuppressWarnings("unused")
public class PluginManagerResult {

    private final String message;
    private final boolean positive;

    public String getMessage() {
        return message;
    }

    public boolean isPositive() {
        return positive;
    }
}

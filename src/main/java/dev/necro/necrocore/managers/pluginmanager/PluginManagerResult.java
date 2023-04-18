package dev.necro.necrocore.managers.pluginmanager;

@SuppressWarnings("unused")
public class PluginManagerResult {
    private final String message;
    private final boolean positive;

    public PluginManagerResult(String message, boolean positive) {
        this.message = message;
        this.positive = positive;
    }

    public String getMessage() {
        return message;
    }

    public boolean isPositive() {
        return positive;
    }
}

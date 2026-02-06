package net.astralechoes.lobby;

import lombok.Getter;
import net.astralechoes.lobby.controller.ConfigController;
import net.astralechoes.lobby.listener.PlayerListener;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class.
 *
 * @since 1.0.0
 */
@Getter
public final class LobbyPlugin extends JavaPlugin {

    private final ConfigController configController = new ConfigController(this);

    private MiniMessage miniMessage;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.miniMessage = MiniMessage.miniMessage();

        new PlayerListener(this).register();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

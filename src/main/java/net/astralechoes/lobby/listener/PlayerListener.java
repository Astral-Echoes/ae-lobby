package net.astralechoes.lobby.listener;

import lombok.RequiredArgsConstructor;
import net.astralechoes.lobby.LobbyPlugin;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.Optional;

/**
 * Listener class for Player events.
 *
 * @since 1.0.0
 */
@RequiredArgsConstructor
public final class PlayerListener implements Listener {

    private final LobbyPlugin plugin;

    // Registers the listener
    public void register() {
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        final Optional<Location> optional = this.plugin.getConfigController().getSpawnLocation();

        optional.ifPresent(event::setRespawnLocation);
    }

    @EventHandler
    public void onPlayerSpawn(final PlayerSpawnLocationEvent event) {
        final Optional<Location> optional = this.plugin.getConfigController().getSpawnLocation();

        optional.ifPresent(event::setSpawnLocation);
    }


}

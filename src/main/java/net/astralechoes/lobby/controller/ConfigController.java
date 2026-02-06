package net.astralechoes.lobby.controller;

import lombok.RequiredArgsConstructor;
import net.astralechoes.lobby.LobbyPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller class for config files.
 *
 * @since 1.0.0
 */
@RequiredArgsConstructor
public final class ConfigController {

    private final LobbyPlugin plugin;

    private CommentedConfigurationNode primaryNode;
    private CommentedConfigurationNode messagesNode;

    // Gets a HoconConfigurationLoader for the given file path
    private HoconConfigurationLoader getHoconConfigurationLoader(final Path path) {
        return HoconConfigurationLoader.builder()
                .path(path)
                .prettyPrinting(true)
                .build();
    }

    // Creates a CommentedConfigurationNode for the given file
    private CommentedConfigurationNode createNode(final String fileName) {
        final Path path = this.plugin.getDataPath().resolve(fileName);

        if (Files.notExists(path)) {
            this.plugin.getComponentLogger().info("Config file '{}' not found, creating it", fileName);
            this.plugin.saveResource(fileName, false);
        }

        final var loader = this.getHoconConfigurationLoader(path);

        try {
            return loader.load();
        } catch (final ConfigurateException e) {
            this.plugin.getComponentLogger().error("Failed to load config file '{}'", fileName, e);
            return null;
        }
    }

    // Gets the spawn location from the primary config
    public Optional<Location> getSpawnLocation() {
        final String spawnString = this.primaryNode.node("spawn-location").getString();

        // Check if spawn string exists
        if (spawnString != null && !(spawnString.isEmpty())) {
            final String[] args = spawnString.split(",");
            final Map<String, Object> locationMap = Map.of(
                    "world", args[0],
                    "x", args[1],
                    "y", args[2],
                    "z", args[3],
                    "yaw", args[4],
                    "pitch", args[5]
            );
            return Optional.of(Location.deserialize(locationMap));
        }
        return Optional.empty();
    }

    // Gets the command prefix from the messages config
    public @NotNull String getPrefix() {
        final String prefix = this.messagesNode.node("prefix").getString();
        return prefix != null ? prefix : "";
    }

    // Gets a message from the messages config
    public @NotNull Component getMessage(final String key, final TagResolver... resolvers) {
        final String message = this.messagesNode.node(key).getString();

        final List<TagResolver> tagResolvers = new ArrayList<>(List.of(resolvers));
        tagResolvers.add(Placeholder.parsed("prefix", this.getPrefix()));

        if (message != null) {
            return this.plugin.getMiniMessage().deserialize(message, tagResolvers.toArray(TagResolver[]::new))
                    .decorationIfAbsent(
                            TextDecoration.ITALIC,
                            TextDecoration.State.FALSE
                    );
        }

        return Component.text(key);
    }

}

package com.thebackrooms.levels;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.tag.Tag;

import javax.swing.text.html.HTML;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Level {

    public static final Tag<String> TAG_LEVEL = Tag.String("Level");
    public static final Tag<List<Integer>> TAG_VISITED_LEVELS = Tag.Integer("VisitedLevels").list().defaultValue(List.of());
    public static final Tag<Double> TAG_SANITY = Tag.Double("Sanity");

    private final String id;
    private final int idHashcode;
    private Component name;
    private InstanceContainer instance;

    public Level(String id) {
        this.id = id;
        this.idHashcode = id.hashCode();
    }

    protected void name(String translationKey) {
        name = Component.translatable(translationKey);
    }

    protected Pos getSpawnPos(Player player) {
        return new Pos(0, 2, 0);
    }

    protected double getSanityChange(Player player) {
        return 0;
    }

    public InstanceContainer getInstance() {
        return instance;
    }

    public void addPlayer(Player player) {
        player.setTag(TAG_LEVEL, id);
        List<Integer> visitedLevels = player.getTag(TAG_VISITED_LEVELS);
        boolean newlyDiscovered = false;
        if (!visitedLevels.contains(idHashcode)) {
            visitedLevels = new ArrayList<>(visitedLevels);
            visitedLevels.add(idHashcode);
            player.setTag(TAG_VISITED_LEVELS, visitedLevels);
            newlyDiscovered = true;
        }

        if (player.getInstance() != instance) player.setInstance(instance);
        player.teleport(getSpawnPos(player));
        onPlayerJoin(player, newlyDiscovered);
    }

    public void init() {
        instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkLoader(new AnvilLoader(id));
        onInit();
    }

    public CompletableFuture<Void> close() {
        onClose();
        return instance.saveChunksToStorage();
    }

    public void onInit() {
    }

    public void onClose() {
    }

    public void onPlayerJoin(Player player, boolean newlyDiscovered) {

    }
}

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
import java.util.HashMap;
import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Level {

    public static final Tag<String> TAG_LEVEL = Tag.String("Level");
    public static final Tag<List<Integer>> TAG_VISITED_LEVELS = Tag.Integer("VisitedLevels").list().defaultValue(List.of());
    public static final Tag<Double> TAG_SANITY = Tag.Double("Sanity");
    public static final HashMap<String, Level> LEVELS = new HashMap<>();

    private final String id;
    private final int idHashcode;
    private Component name;
    private InstanceContainer instance;

    public Level(String id) {
        this.id = id;
        this.idHashcode = id.hashCode();
        LEVELS.put(id, this);
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
        addPlayer(player, true);
    }

    public void addPlayer(Player player, boolean setInstance) {
        player.setTag(TAG_LEVEL, id);
        List<Integer> visitedLevels = player.getTag(TAG_VISITED_LEVELS);
        boolean newlyDiscovered = false;
        if (!visitedLevels.contains(idHashcode)) {
            visitedLevels = new ArrayList<>(visitedLevels);
            visitedLevels.add(idHashcode);
            player.setTag(TAG_VISITED_LEVELS, visitedLevels);
            newlyDiscovered = true;
        }

        try {
            if (player.getInstance() != instance)
                if (setInstance) player.setInstance(instance).get();
            player.teleport(getSpawnPos(player));
            onPlayerJoin(player, newlyDiscovered);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePlayer(Player player) {
        onPlayerLeave(player);
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

    public void onPlayerLeave(Player player) {
    }
}

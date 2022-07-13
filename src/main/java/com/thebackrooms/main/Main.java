package com.thebackrooms.main;


import com.thebackrooms.commands.WarpCommand;
import com.thebackrooms.levels.Level;
import com.thebackrooms.levels.Levels;
import com.thebackrooms.listener.PlayerDataListener;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.*;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.world.biomes.Biome;
import org.jglrxavpok.hephaistos.antlr.SNBTParser;
import org.jglrxavpok.hephaistos.nbt.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();

        MojangAuth.init();
        Levels.init();
        PlayerDataListener.init();

        MinecraftServer.getCommandManager().register(new WarpCommand());

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, event -> {
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
        });

        minecraftServer.start("0.0.0.0", 25565);
    }
}
package com.thebackrooms.listener;

import com.thebackrooms.levels.Level;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jglrxavpok.hephaistos.nbt.*;

import javax.swing.text.html.HTML;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlayerDataListener {

    public static final Tag<List<ItemStack>> TAG_INVENTORY = Tag.ItemStack("InventorySlots").list();

    public static void init() {
        new File("playerdata").mkdirs();

        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, event -> {
            Player player = event.getPlayer();
            player.setTag(TAG_INVENTORY, List.of(player.getInventory().getItemStacks()));

            try {
                File file = new File("./playerdata/" + player.getUuid() + ".dat");
                if (!file.exists()) file.createNewFile();
                NBTWriter writer = new NBTWriter(new File("./playerdata/" + player.getUuid() + ".dat"));
                writer.writeRaw(NBT.Compound(root -> root.put("Data", player.tagHandler().asCompound())));
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, event -> {
            Player player = event.getPlayer();
            try {
                File file = new File("./playerdata/" + player.getUuid() + ".dat");
                if (!file.exists()) return;
                NBTReader reader = new NBTReader(file);
                player.tagHandler().updateContent((NBTCompoundLike) reader.read());
                for (int i = 0; i < player.getTag(TAG_INVENTORY).size(); i++)
                    player.getInventory().setItemStack(i, player.getTag(TAG_INVENTORY).get(i));
                Level.LEVELS.get(player.getTag(Level.TAG_LEVEL)).addPlayer(player);
                reader.close();
            } catch (IOException | NBTException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

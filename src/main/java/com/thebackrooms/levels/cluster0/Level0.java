package com.thebackrooms.levels.cluster0;

import com.thebackrooms.levels.Level;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;

public class Level0 extends Level {

    public Level0() {
        super("level_0");
        name("level.thebackrooms.level_0");
    }

    @Override
    protected Pos getSpawnPos(Player player) {
        return new Pos(0, 42, 0);
    }

    @Override
    public void onInit() {
        getInstance().setGenerator(unit ->
                unit.modifier().fillHeight(0, 40, Block.SANDSTONE));
    }
}

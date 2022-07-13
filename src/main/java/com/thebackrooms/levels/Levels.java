package com.thebackrooms.levels;

import com.thebackrooms.levels.cluster0.Level0;
import net.minestom.server.entity.Player;

import java.lang.reflect.Field;

public class Levels {

    public static final Level LEVEL_0 = new Level0();

    public static void init() {
        for (Field field : Levels.class.getFields()) {
            try {
                ((Level) field.get(null)).init();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void close() {
        for (Field field : Levels.class.getFields()) {
            try {
                ((Level) field.get(null)).close();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

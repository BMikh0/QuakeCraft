package ru.croxi.quakeCraft.utils;

import java.util.Random;

public class MathUtil {

    public static int getRandomInt(int bound) {
        return (int) Math.round(Math.random()*bound);
    }

}

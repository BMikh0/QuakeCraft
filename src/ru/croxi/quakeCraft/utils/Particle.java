package ru.croxi.quakeCraft.utils;

import net.minecraft.server.v1_8_R1.EnumParticle;
import net.minecraft.server.v1_8_R1.PacketPlayOutWorldParticles;
import org.bukkit.Location;

public class Particle {

    public static void display(EnumParticle type, Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        PacketSender.sendPacket(new PacketPlayOutWorldParticles(type, true, (float)location.getX(), (float)location.getY(),(float) location.getZ(), offsetX, offsetY, offsetZ, speed, amount));
    }

}

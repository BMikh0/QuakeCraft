package ru.croxi.quakeCraft.utils;


import net.minecraft.server.v1_8_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketSender {

    public static void sendPacket(Packet packet) {
        for(Player player: Bukkit.getOnlinePlayers()) sendPacket(packet, player);
    }

    public static void sendPacket(Packet packet, Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}

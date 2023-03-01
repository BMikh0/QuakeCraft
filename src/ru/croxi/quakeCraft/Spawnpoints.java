package ru.croxi.quakeCraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.croxi.quakeCraft.utils.MathUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Spawnpoints {

    private static File file;
    private static FileConfiguration yamlFile;
    private static Map<Integer, Location> locations;
    private static int biggestId;

    Spawnpoints() {
        file = new File(QuakeCraft.getInstance().getDataFolder() +File.separator +"spawnLocations.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        locations = new HashMap<>();
        initialize();
    }

    public boolean areEmpty() {
        yamlFile = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection cfgSection = yamlFile.getConfigurationSection("locations");

        return cfgSection == null || cfgSection.getKeys(false).isEmpty();
    }

    private void initialize() {
        if(!areEmpty()) {
            locations.clear();

            ConfigurationSection cfgSection = yamlFile.getConfigurationSection("locations");

            for(Map.Entry<String, Object> entry: cfgSection.getValues(false).entrySet()) {
                ConfigurationSection value = ((ConfigurationSection) entry.getValue());
                int id = Integer.valueOf(entry.getKey());

                locations.put(id, new Location(Bukkit.getWorld(value.getString("world")), value.getInt("x"), value.getInt("y"), value.getInt("z"), (float)value.getDouble("yaw"), (float) value.getDouble("pitch")));
                if(biggestId < id) biggestId = id;
            }
        }
    }

    public int create(Location location) {
        ConfigurationSection cfgSection = yamlFile.getConfigurationSection("locations");
        int id;

        for(Map.Entry<Integer, Location> entry: locations.entrySet()) {
            if(areEqual(entry.getValue(), location)) return -1;
        }

        for(id = 1; cfgSection != null && id<=cfgSection.getKeys(false).size(); id++) {
            if(cfgSection.get(String.valueOf(id)) == null) break;
        }

        String pathElement = "locations." +id +".";

        yamlFile.set(pathElement +"world", location.getWorld().getName());
        yamlFile.set(pathElement +"x", location.getBlockX());
        yamlFile.set(pathElement +"y", location.getBlockY());
        yamlFile.set(pathElement +"z", location.getBlockZ());
        yamlFile.set(pathElement + "yaw", location.getYaw());
        yamlFile.set(pathElement + "pitch", location.getPitch());

        locations.put(id, location);

        try {
            yamlFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }

    public boolean remove(int id) {
        if (yamlFile.get("locations." +id) != null) {
            yamlFile.set("locations." +id, null);

            locations.remove(id);

            try {
                yamlFile.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

        return false;
    }

    public Map<Integer, Location> getLocations() {
        return locations;
    }

    public Location getRandomSpawnpoint() {
        Location location;

        do location = locations.get(MathUtil.getRandomInt(biggestId - 1) +1);
        while(location == null);

        return location;
    }

    private static boolean areEqual(Location location1, Location location2) {
        if (location1 == location2) return true;

        if (location1 == null || location2 == null) return false;

        return location1.getWorld().equals(location2.getWorld()) &&
                location1.getBlockX() == location2.getBlockX() &&
                location1.getBlockY() == location2.getBlockY() &&
                location1.getBlockZ() == location2.getBlockZ();
    }

}

package fr.xamez.textholoapi.text;

import fr.xamez.textholoapi.hologram.HologramManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class TextHoloAPI {

    private static HologramManager hologramManager;
    private static JavaPlugin plugin;

    /**
     * Setup the API
     * @param javaPlugin Pass the main class of your plugin that extends {@link JavaPlugin}
     */
    public static void register(JavaPlugin javaPlugin) {
        plugin = javaPlugin;
        hologramManager = new HologramManager();
    }

    /**
     * Display a text to the player with holograms
     * @param player The player
     * @param text The text to display
     * @param duration Duration in tick between each letter
     * @param waitingTicks Waiting time in tick before the hologram disappears
     */
    public static void displayText(Player player, Location location, ArrayList<String> text, int duration, int waitingTicks){
        new TextRunnable(player, location, text, waitingTicks).runTaskTimerAsynchronously(plugin, 0L, duration);
    }

    public static HologramManager getHologramManager() {
        if (hologramManager == null){
            try {
                throw new Exception(
                        "You have to register the API correctly." +
                        "E.g. TextHoloAPI.register(this); on your main class"
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return hologramManager;
    }
}

package fr.xamez.textholoapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReflectionUtils {

    public static void sendPacket(Player p, Object packet){
        try {
            final Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
            final Object playerConnection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
            playerConnection.getClass().getMethod("sendPacket", getNmsClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getNmsClass(String className){
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Object.class;
    }

    public static String getVersion(){
        return Bukkit.getServer().getClass().getPackageName().split("\\.")[3];
    }

    public static boolean isAnOldVersion(){
        return getMinorVersion() < 13;
    }

    public static int getMinorVersion(){
        final String version = getVersion();
        final String subVer = version.split("_")[1];
        try {
            return Integer.parseInt(subVer);
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        return 16;
    }

}

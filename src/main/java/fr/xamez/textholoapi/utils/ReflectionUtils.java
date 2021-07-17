package fr.xamez.textholoapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReflectionUtils {

    public static void sendPacket(Player p, Object packet){
        try {
            Object playerConnection;
            final Object nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
            if (getMajorVersion() < 17) {
                playerConnection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
                playerConnection.getClass().getMethod("sendPacket", getNmsClass("Packet")).invoke(playerConnection, packet);
            } else {
                playerConnection = nmsPlayer.getClass().getField("b").get(nmsPlayer);
                playerConnection.getClass().getMethod("sendPacket", getNetworkClass("protocol", "Packet")).invoke(playerConnection, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getNmsClass(String className){
        return getClass("net.minecraft.server." + getVersion() + "." + className);
    }

    public static Class<?> getPacketClass(String className){
        return getClass("net.minecraft.network.protocol.game." + className);
    }

    public static Class<?> getNetworkClass(String subPackage, String className){
        return getClass("net.minecraft.network." + subPackage + "." + className);
    }

    public static Class<?> getMinecraftClass(String subPackage, String className){
        return getClass("net.minecraft." + subPackage + "." + className);
    }

    private static Class<?> getClass(String pathClass){
        try {
            return Class.forName(pathClass);
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return Object.class;
    }

    public static String getVersion(){
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public static boolean isAnOldVersion(){
        return getMajorVersion() < 13;
    }

    public static int getMajorVersion(){
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

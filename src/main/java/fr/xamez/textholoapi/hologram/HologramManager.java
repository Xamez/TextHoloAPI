package fr.xamez.textholoapi.hologram;

import fr.xamez.textholoapi.utils.ReflectionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HologramManager {

    public void createHologram(Player p, Hologram hologram){
        Location nextLoc = hologram.getBaseLocation().clone();
        for (String s : hologram.getLines()){
            spawnArmorStand(hologram, p, s, nextLoc);
            nextLoc.subtract(0, hologram.getSpaceBetween(), 0);
        }
    }

    public void deleteHologram(Player p, Hologram hologram){
        for (Object armorStand : hologram.getArmorStands()){
            try {
                final int id = (int) armorStand.getClass().getMethod("getId").invoke(armorStand);
                final Object packetPlayOutEntityDestroy = ReflectionUtils.getNmsClass("PacketPlayOutEntityDestroy").getConstructor(int[].class).newInstance(new int[]{id});
                ReflectionUtils.sendPacket(p, packetPlayOutEntityDestroy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void editLine(Player p, Hologram hologram, int lineNumber, String text){
        Location nextLoc = hologram.getBaseLocation().clone();
        nextLoc.subtract(0, hologram.getSpaceBetween()*lineNumber, 0);
        if (lineNumber < hologram.getArmorStands().size()) {
            final Object entity = hologram.getArmorStands().get(lineNumber);
            try {
                setCustomName(entity, text);
                updateEntityMetaData(p, entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addLine(Player p, Hologram hologram, String text) {
        Location nextLoc = hologram.getBaseLocation().clone();
        nextLoc.subtract(0, hologram.getSpaceBetween()*(hologram.getLines().size()-1), 0);
        spawnArmorStand(hologram, p, text, nextLoc);
    }

    public void spawnArmorStand(Hologram hologram, Player p, String text, Location location){
        try {
            final Object nmsWorldServer = location.getWorld().getClass().getMethod("getHandle").invoke(location.getWorld());
            final Class<?> nmsWorldClazz = ReflectionUtils.getNmsClass("World");
            final Object entityArmorStand = ReflectionUtils.getNmsClass("EntityArmorStand")
                    .getConstructor(nmsWorldClazz, double.class, double.class, double.class)
                    .newInstance(nmsWorldServer, location.getX(), location.getY(), location.getZ());
            setCustomName(entityArmorStand, text);
            entityArmorStand.getClass().getMethod("setInvisible", boolean.class).invoke(entityArmorStand, true);
            if (ReflectionUtils.getMinorVersion() < 9)
                entityArmorStand.getClass().getMethod("setGravity", boolean.class).invoke(entityArmorStand, true);
            else
                entityArmorStand.getClass().getMethod("setNoGravity", boolean.class).invoke(entityArmorStand, true);
            entityArmorStand.getClass().getMethod("setCustomNameVisible", boolean.class).invoke(entityArmorStand, true);
            if (ReflectionUtils.getMinorVersion() > 8)
                entityArmorStand.getClass().getMethod("setMarker", boolean.class).invoke(entityArmorStand, true);
            final Class<?> nmsEntityLivingClazz = ReflectionUtils.getNmsClass("EntityLiving");
            final Object packetPlayOutSpawnEntityLiving = ReflectionUtils.getNmsClass("PacketPlayOutSpawnEntityLiving")
                    .getConstructor(nmsEntityLivingClazz)
                    .newInstance(entityArmorStand);
            ReflectionUtils.sendPacket(p, packetPlayOutSpawnEntityLiving);
            updateEntityMetaData(p, entityArmorStand);
            hologram.getArmorStands().add(entityArmorStand);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateEntityMetaData(Player p, Object entity){
        try {
            final int id = (int) entity.getClass().getMethod("getId").invoke(entity);
            final Object dataWatcher = entity.getClass().getMethod("getDataWatcher").invoke(entity);
            final Class<?> dataWatcherClazz = ReflectionUtils.getNmsClass("DataWatcher");
            final Object packetPlayOutEntityMetadata = ReflectionUtils.getNmsClass("PacketPlayOutEntityMetadata")
                    .getConstructor(int.class, dataWatcherClazz, boolean.class)
                    .newInstance(id, dataWatcher, false);
            ReflectionUtils.sendPacket(p, packetPlayOutEntityMetadata);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setCustomName(Object entity, String customName){
        try {
            if (ReflectionUtils.isAnOldVersion()){
                entity.getClass().getMethod("setCustomName", String.class).invoke(entity, ChatColor.translateAlternateColorCodes('&', customName));
            } else {
                final Class<?> nmsChatBaseComponentClazz = ReflectionUtils.getNmsClass("IChatBaseComponent");
                final Object componentText = ReflectionUtils.getNmsClass("ChatComponentText").getConstructor(String.class).newInstance(ChatColor.translateAlternateColorCodes('&', customName));
                entity.getClass().getMethod("setCustomName", nmsChatBaseComponentClazz).invoke(entity, componentText);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}

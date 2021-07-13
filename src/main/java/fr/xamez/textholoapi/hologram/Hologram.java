package fr.xamez.textholoapi.hologram;

import fr.xamez.textholoapi.text.TextHoloAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hologram {

    private final HologramManager hologramManager = TextHoloAPI.getHologramManager();

    private final Player p;
    private final Location baseLocation;
    private final double spaceBetween;
    private final ArrayList<Object> armorStands;
    private final ArrayList<String> lines;

    public Hologram(Player p, Location baseLocation, double spaceBetween, ArrayList<String> lines) {
        this.p = p;
        this.baseLocation = baseLocation;
        this.spaceBetween = spaceBetween;
        this.lines = lines;
        this.armorStands = new ArrayList<>();
    }

    public Hologram(Player p, Location baseLocation){
        this(p, baseLocation, 0.25, new ArrayList<>(Collections.singletonList(" ")));
    }

    public Location getBaseLocation() {
        return baseLocation;
    }

    public double getSpaceBetween() {
        return spaceBetween;
    }

    public List<String> getLines() {
        if (lines.size() > 0)
            return lines;
        return Collections.singletonList("");
    }

    public void editLine(int lineNumber, String text){
        if (this.lines.size() > lineNumber) {
            this.lines.set(lineNumber, text);
            hologramManager.editLine(p, this, lineNumber, text);
        }
    }

    public void addLine(String text){
        this.lines.add(text);
        hologramManager.addLine(p, this, text);
    }

    public void create(){
        hologramManager.createHologram(p, this);
    }

    public ArrayList<Object> getArmorStands() {
        return armorStands;
    }
}

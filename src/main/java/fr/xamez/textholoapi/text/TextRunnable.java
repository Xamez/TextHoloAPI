package fr.xamez.textholoapi.text;

import fr.xamez.textholoapi.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TextRunnable extends BukkitRunnable {

    private final Player p;
    private final ArrayList<String> dialogue;
    private final Hologram hologram;
    private final int lineSize;
    private final String lastLine;
    private int line = 0;
    private int cursor = 0;
    private boolean wait = false;
    private final int waitingTicks;
    private final boolean hasCursor;

    public TextRunnable(Player p, Location location, ArrayList<String> dialogue, int waitingTicks, double spaceBetween, boolean hasCursor){
        this.p = p;
        this.dialogue = dialogue;
        this.lineSize = dialogue.size()-1;
        this.lastLine = dialogue.get(lineSize);
        this.hologram = new Hologram(p, location, spaceBetween, new ArrayList<>(Collections.singletonList(" ")));
        this.waitingTicks = waitingTicks;
        this.hasCursor = hasCursor;
        hologram.create();
    }

    @Override
    public void run() {
        if (!wait) {
            if (cursor > dialogue.get(line).length() && line < lineSize) {
                hologram.addLine(" ");
                cursor = 0;
                line++;
            }
            hologram.editLine(line, getDisplayedText());
            cursor++;
            if (cursor == dialogue.get(line).length() + 1 && line == lineSize) {
                cursor = 0;
                wait = true;
            }
        } else {
            if (cursor >= waitingTicks) {
                TextHoloAPI.getHologramManager().deleteHologram(p, hologram);
                this.cancel();
            }
            cursor++;
            if (hasCursor) {
                final String line = hologram.getLines().get(lineSize);
                if (cursor % 5 == 0)
                    hologram.editLine(lineSize, this.lastLine.equals(line) ? line + "§f|" : lastLine);
            }
        }
    }

    private String getDisplayedText(){
        final char[] chars = dialogue.get(line).toCharArray();
        return new String(Arrays.copyOfRange(chars, 0, cursor));
    }
}

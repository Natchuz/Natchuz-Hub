package com.natchuz.hub.paper.music;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;

/**
 * Plays a music to specific group of players
 *
 * @see Song
 */
public class MusicPlayer {
    private BukkitTask task;
    private int tick;
    private final LinkedList<Player> players = new LinkedList<>();
    private boolean isPlaying = false;

    private final Plugin plugin;

    public MusicPlayer(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Player song
     *
     * @param song song
     */
    public void play(Song song) {
        cancel();
        task = Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            tick = 0;
            isPlaying = true;
            while (tick < song.getLength() && isPlaying) {
                for (int i = 0; i < song.getLayers(); i++) {
                    if (song.getInstruments(tick)[i] == null)
                        continue;
                    for (Player p : players)
                        p.playSound(p.getLocation(), SOUNDS[song.getInstruments(tick)[i].ordinal()], song.getVolume(i), PITCHES[song.getNote(tick)[i]]);
                }
                tick++;
                try {
                    Thread.sleep(song.getTickSpacing());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isPlaying = false;
        });
    }

    /**
     * Stop playing current song
     */
    public void cancel() {
        isPlaying = false;
    }

    /**
     * Add a player to listening group
     *
     * @param player player to add
     */
    public void appendPlayer(Player player) {
        players.add(player);
    }

    /**
     * Remove player from listening group
     *
     * @param player a plyer to remove
     */
    public void removePlayer(Player player) {
        players.remove(player);
        if (players.size() == 0) {
            cancel();
        }
    }

    /**
     * How many players listen to music now
     */
    public int playerCount() {
        return players.size();
    }

    /**
     * If there is any song playing right now, or it is stopped.
     */
    public boolean isPlaying() {
        return isPlaying;
    }


    /**
     * Since player.playNote() requires Noteblock at specified location we have to play it via playSound()
     * Each Sound and Pitch corresponds to Instrument ordinal and Note
     * <p>
     * https://minecraft.gamepedia.com/Note_Block
     */
    private final float[] PITCHES = {0.50000F, 0.52973F, 0.56123F, 0.59461F, 0.62995F, 0.66741F, 0.70711F, 0.74916F, 0.79370F,
            0.84089F, 0.89091F, 0.94386F, 1.00000F, 1.05945F, 1.12245F, 1.18920F, 1.25993F, 1.33484F, 1.41420F, 1.49832F,
            1.58741F, 1.68180F, 1.78180F, 1.88775F, 2.00000F};

    private final Sound[] SOUNDS = {
            Sound.BLOCK_NOTE_BLOCK_HARP, Sound.BLOCK_NOTE_BLOCK_BASEDRUM,
            Sound.BLOCK_NOTE_BLOCK_SNARE, Sound.BLOCK_NOTE_BLOCK_HAT,
            Sound.BLOCK_NOTE_BLOCK_BASS, Sound.BLOCK_NOTE_BLOCK_FLUTE,
            Sound.BLOCK_NOTE_BLOCK_BELL, Sound.BLOCK_NOTE_BLOCK_GUITAR,
            Sound.BLOCK_NOTE_BLOCK_CHIME, Sound.BLOCK_NOTE_BLOCK_XYLOPHONE,
            Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, Sound.BLOCK_NOTE_BLOCK_COW_BELL,
            Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, Sound.BLOCK_NOTE_BLOCK_BIT,
            Sound.BLOCK_NOTE_BLOCK_BANJO, Sound.BLOCK_NOTE_BLOCK_PLING
    };
}

package com.natchuz.hub.paper.music;

import org.bukkit.Instrument;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Representation of the song in NBS format
 * See: https://hielkeminecraft.github.io/OpenNoteBlockStudio/nbs
 */
public class Song {
    private Instrument[][] instruments;
    private int[][] keys;
    private float[] volume;
    private int[] stereo;

    private short length;
    private short layers;
    private short tempo;

    public static Song fromDataFolder(File file) {
        try {
            Song song = new Song();
            song.parse(new DataInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Song fromResource(String name) {
        try {
            Song song = new Song();
            song.parse(new DataInputStream(Song.class.getResourceAsStream(name)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parse(DataInputStream input) throws IOException {
        //region header

        readShort(input);
        input.readByte(); // NBS version
        input.readByte(); // Vanilla Note Block Instruments count
        length = readShort(input); // Song length
        layers = readShort(input); // Layer count
        readString(input); // Song name
        readString(input); // Song author
        readString(input); // Song original author
        readString(input); // Song description
        tempo = readShort(input); // Song tempo
        input.readBoolean(); // Auto-saving
        input.readByte(); // Auto-saving duration
        input.readByte(); // Time signature
        readInt(input); // Minutes spent
        readInt(input); // Left clicks
        readInt(input); // Right clicks
        readInt(input); // Note blocks added
        readInt(input); // Block removed (IDK why but this wasn't included in specification, so I spent literally 5 hours on debugging)
        readString(input); // MIDI/Schematic file name

        //endregion

        instruments = new Instrument[length + 1][layers];
        keys = new int[length + 1][layers];
        volume = new float[layers];
        stereo = new int[layers];

        Arrays.fill(volume, 1.0f);

        //region note blocks

        short tick = -1;
        while (true) {
            short tickJump = readShort(input);
            if (tickJump == 0)
                break;
            tick += tickJump;

            short layer = -1;
            while (true) {
                short layerJump = readShort(input);
                if (layerJump == 0)
                    break;
                layer += layerJump;

                byte nbsInstrument = input.readByte();
                Instrument instrument;
                //translate to instrument
                switch (nbsInstrument) {
                    case (1):
                        instrument = Instrument.BASS_GUITAR;
                        break;
                    case (2):
                        instrument = Instrument.BASS_DRUM;
                        break;
                    case (3):
                        instrument = Instrument.SNARE_DRUM;
                        break;
                    case (4):
                        instrument = Instrument.STICKS;
                        break;
                    case (5):
                        instrument = Instrument.GUITAR;
                        break;
                    case (6):
                        instrument = Instrument.FLUTE;
                        break;
                    case (7):
                        instrument = Instrument.BELL;
                        break;
                    default:
                        instrument = Instrument.values()[nbsInstrument];
                        break;
                }
                instruments[tick][layer] = instrument;
                keys[tick][layer] = input.readByte() - 33;
            }
        }

        //endregion
        //region layers

        for (int i = 0; i < layers; i++) {
            readString(input);
            volume[i] = input.readByte() / 100;
            stereo[i] = input.readByte();
        }

        //endregion
    }

    private String readString(DataInputStream input) throws IOException {
        int length = readInt(input);
        StringBuilder builder = new StringBuilder(length);
        for (; length > 0; --length) {
            char c = (char) input.readByte();
            if (c == (char) 0x0D) {
                c = ' ';
            }
            builder.append(c);
        }
        return builder.toString();
    }

    private static short readShort(DataInputStream dataInputStream) throws IOException {
        int byte1 = dataInputStream.readUnsignedByte();
        int byte2 = dataInputStream.readUnsignedByte();
        return (short) (byte1 + (byte2 << 8));
    }

    private static int readInt(DataInputStream dataInputStream) throws IOException {
        int byte1 = dataInputStream.readUnsignedByte();
        int byte2 = dataInputStream.readUnsignedByte();
        int byte3 = dataInputStream.readUnsignedByte();
        int byte4 = dataInputStream.readUnsignedByte();
        return (byte1 + (byte2 << 8) + (byte3 << 16) + (byte4 << 24));
    }

    public Instrument[] getInstruments(int tick) {
        return instruments[tick];
    }

    public int[] getNote(int tick) {
        return keys[tick];
    }

    public float getVolume(int layer) {
        return volume[layer];
    }

    public long getTickSpacing() {
        return 1000 / (tempo / 100);
    }

    public int getStereo(int layer) {
        return stereo[layer];
    }

    public short getLength() {
        return length;
    }

    public short getLayers() {
        return layers;
    }

    public short getTempo() {
        return tempo;
    }
}
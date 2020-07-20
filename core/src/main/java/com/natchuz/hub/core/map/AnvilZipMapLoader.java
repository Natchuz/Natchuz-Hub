package com.natchuz.hub.core.map;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.natchuz.hub.paper.utils.EmptyChunkGenerator;
import com.natchuz.hub.utils.UUIDConverter;

/**
 * Loads map from zipped world in Anvil format
 */
public class AnvilZipMapLoader implements MapLoader {

    private final MapRepository mapRepository;

    /**
     * Map repository to load files from
     *
     * @param mapRepository
     */
    public AnvilZipMapLoader(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    @Override
    public LoadedMap load(String id) throws IOException {
        File worldContainer = Bukkit.getWorldContainer();
        UUID loadingUUID = UUID.randomUUID();

        Bukkit.getLogger().info("[AnvilZipMapLoader] Initializing map loading with UUID: "
                + loadingUUID.toString());

        InputStream mapStream = mapRepository.requestMap(id).orElseThrow(
                () -> new IllegalArgumentException("Map with this id (" + id + ") does not exist in repo!"));

        // create a directory for map
        File creatingWorldContainer = new File(worldContainer + "/" + UUIDConverter.toCondensed(loadingUUID));
        creatingWorldContainer.mkdirs();

        ZipInputStream zipStream = new ZipInputStream(mapStream);

        /*
            Unpack map archive
         */
        ZipEntry entry = zipStream.getNextEntry();
        while (entry != null) {
            File outputFile = new File(creatingWorldContainer + "/" + entry.getName());
            if (entry.isDirectory()) {
                outputFile.mkdirs();
            } else {
                byte[] buffer = new byte[4096];
                FileOutputStream fileStream = new FileOutputStream(outputFile);
                int len;
                while ((len = zipStream.read(buffer)) > 0) {
                    fileStream.write(buffer, 0, len);
                }
                fileStream.close();
            }
            entry = zipStream.getNextEntry();
        }

        mapStream.close();
        zipStream.close();

        String config = FileUtils.readFileToString(new File(creatingWorldContainer + "/config.json"),
                StandardCharsets.UTF_8);

        MapManifest mapManifest = MapManifest.fromString(
                FileUtils.readFileToString(new File(creatingWorldContainer + "/manifest.properties"),
                        StandardCharsets.UTF_8));

        // init world
        World world = Bukkit.createWorld(new WorldCreator(UUIDConverter.toCondensed(loadingUUID))
                .generator(new EmptyChunkGenerator()).generateStructures(false).seed(0));

        Bukkit.getLogger().info("[AnvilZipMapLoader] Successfully loaded a map! ");

        return new LoadedMap(world, config, mapManifest);
    }
}
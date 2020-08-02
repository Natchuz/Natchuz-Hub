package com.natchuz.hub.core.map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldArchetypes;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.natchuz.hub.utils.UUIDConverter;

/**
 * Loads map from zipped world in Anvil format
 */
public class AnvilZipMapLoader implements MapLoader {

    private final MapRepository mapRepository;
    private Logger logger;

    /**
     * Map repository to load files from
     *
     * @param mapRepository repo to load map from
     */
    public AnvilZipMapLoader(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
        logger = Sponge.getPluginManager().getPlugin("natchuz-hub-core").get().getLogger();
    }

    @Override
    public LoadedMap load(String id) throws IOException {
        UUID loadingUUID = UUID.randomUUID();

        logger.info("[AnvilZipMapLoader] Initializing map loading with UUID: "
                + loadingUUID.toString());

        InputStream mapStream = mapRepository.requestMap(id).orElseThrow(
                () -> new IllegalArgumentException("Map with this id (" + id + ") does not exist in repo!"));

        // create a directory for map
        File creatingWorldContainer = new File(Sponge.getGame().getGameDirectory().toAbsolutePath() + "/world/"
                + UUIDConverter.toCondensed(loadingUUID));
        creatingWorldContainer.mkdirs();
        ZipInputStream zipStream = new ZipInputStream(mapStream);

        // Unpack map archive
        ZipEntry entry = zipStream.getNextEntry();
        while (entry != null) {
            File outputFile = new File(creatingWorldContainer + "/" + entry.getName()).getAbsoluteFile();
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
        String worldName = UUIDConverter.toCondensed(loadingUUID);
        //Sponge.getServer().loadWorld("")
        //WorldProperties properties = Sponge.getServer().createWorldProperties(worldName, WorldArchetypes.THE_VOID);
        Sponge.getServer().loadWorld(worldName);
        /*World world = *///Sponge.getServer().loadWorld(properties);
                //.orElseThrow(() -> new RuntimeException("Map not found in folder"));
        logger.info("[AnvilZipMapLoader] Successfully loaded a map! ");

        //return new LoadedMap(Sponge.getServer().getWorld(worldName).get().getUniqueId(), config, mapManifest);
        return new LoadedMap(Sponge.getServer().getWorld("world").get().getUniqueId(), config, mapManifest);
    }
}
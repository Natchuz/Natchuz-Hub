package com.natchuz.hub.core.base.map;

import lombok.SneakyThrows;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.natchuz.hub.core.api.map.MapManifest;

/**
 * {@link MapRepository Map repositry} on disk that contains map files
 */
public class LocalMapRepository implements MapRepository {

    private final File repoLocation;
    private Logger logger;

    /**
     * @param repoLocation directory of repo
     */
    public LocalMapRepository(File repoLocation) {
        Validate.isTrue(repoLocation.isDirectory(), "Repo location has to be directory!");
        this.repoLocation = repoLocation;
        logger = Sponge.getPluginManager().getPlugin("natchuz-hub-core").get().getLogger();
    }

    @SneakyThrows
    @Override
    public Optional<InputStream> requestMap(String id) {
        assert repoLocation.listFiles() != null : "Critical error while reading local repo! " + this.toString();

        for (File file : Objects.requireNonNull(repoLocation.listFiles())) {
            if (file.getName().matches(".+\\.zip")) {
                ZipFile zipFile = new ZipFile(file);
                ZipEntry manifestEntry = zipFile.getEntry("manifest.properties");
                if (manifestEntry == null) {
                    logger.info("[LocalMapRepository] File "
                            + file.getCanonicalPath() + " does not contain manifest.properties! Skipping.");
                    continue;
                }
                MapManifest mapManifest = MapManifest.fromStream(zipFile.getInputStream(manifestEntry));
                if (mapManifest.getId().equals(id)) {
                    return Optional.of(new FileInputStream(file));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "LocalMapRepository{" + "repoLocation=" + repoLocation + '}';
    }
}

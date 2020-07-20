package com.natchuz.hub.utils.mojang;

import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestInstance;
import lombok.SneakyThrows;
import org.apache.http.HttpException;

import java.util.UUID;

/**
 * Wrapper around Electroid Mojang API that cache data, and does not limit requests per minute
 *
 * @see <a href="https://github.com/Electroid/mojang-api">https://github.com/Electroid/mojang-api</a>
 */
public class ElectroidMojangAPI implements MojangAPI {

    private final UnirestInstance instance;
    private final Gson gson;
    private final String apiUrl;

    /**
     * Creates a client using specified http client instance, and api endpoint
     *
     * @param apiUrl should be formatted for {@link String#format(String, Object...)} function
     */
    public ElectroidMojangAPI(UnirestInstance instance, String apiUrl) {
        this.instance = instance;
        this.apiUrl = apiUrl;
        this.gson = new Gson();
    }

    /**
     * Creates a client using specified http client
     */
    public ElectroidMojangAPI(UnirestInstance instance) {
        this(instance, "https://api.ashcon.app/mojang/v2/user/%s");
    }

    /**
     * Creates a client using default values
     */
    public ElectroidMojangAPI() {
        this(Unirest.primaryInstance());
    }

    /**
     * Returns user
     *
     * @param uuid uuid of user
     */
    public MojangUser getUser(UUID uuid) {
        return getUser(uuid.toString());
    }

    /**
     * Returns user
     *
     * @param id can be username, uuid, or even condensed uuid
     */
    @SneakyThrows
    public MojangUser getUser(String id) {
        HttpResponse<String> response = instance.get(String.format(getApiUrl(), id)).asString();

        if (response.getStatus() == 404) {
            return null;
        } else if (response.getStatus() == 200) {
            return gson.fromJson(response.getBody(), MojangUser.class);
        } else {
            throw new HttpException("Could not perform api call, or returned bad payload");
        }
    }

    @Override
    public String getUsername(UUID uuid) {
        return getUser(uuid).getUsername();
    }

    @Override
    public UUID getUUID(String username) {
        return getUser(username).getUUID();
    }

    public String getApiUrl() {
        return apiUrl;
    }
}

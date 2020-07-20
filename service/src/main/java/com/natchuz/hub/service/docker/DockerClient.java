package com.natchuz.hub.service.docker;

import java.util.Map;

public interface DockerClient {

    //region container methods

    /**
     * Creates a container
     *
     * @param image  image name
     * @param name   name
     * @param params map of environment variables
     * @param labels map of labels
     * @return id of created container
     */
    String createContainer(String image, String name, Map<String, String> params, Map<String, String> labels);

    /**
     * Starts a container
     *
     * @param id container id
     */
    void runContainer(String id);

    /**
     * Stops a container
     *
     * @param id      container id
     * @param timeout how long to wait until docker forces kill
     */
    void stopContainer(String id, int timeout);

    /**
     * Kill a container
     *
     * @param id container id
     */
    void killContainer(String id);

    /**
     * Remove container
     *
     * @param id container id
     */
    void removeContainer(String id);

    //endregion
    //region network methods

    /**
     * Joins container to network
     *
     * @param container container id
     * @param network   network id
     */
    void joinContainerToNetwork(String container, String network);

    //endregion
}

package com.natchuz.hub.utils;

import lombok.SneakyThrows;

import java.util.Properties;

/**
 * Loads information about jar
 *
 * @link https://github.com/nemerosa/versioning#versioning-info
 */
public class VersionInfo {

    private final String build;
    private final String branch;
    private final String base;
    private final String branchID;
    private final String branchType;
    private final String commit;
    private final String display;
    private final String full;
    private final String scm;
    private final String tag;
    private final String lastTag;
    private final boolean dirty;

    public VersionInfo(Class<?> clazz) {
        this(clazz, "/version.properties");
    }

    @SneakyThrows
    public VersionInfo(Class<?> clazz, String location) {
        Properties prop = new Properties();
        prop.load(clazz.getResourceAsStream(location));

        build = prop.getProperty("VERSION_BUILD");
        branch = prop.getProperty("VERSION_BRANCH");
        base = prop.getProperty("VERSION_BASE");
        branchID = prop.getProperty("VERSION_BRANCHID");
        branchType = prop.getProperty("VERSION_BRANCHTYPE");
        commit = prop.getProperty("VERSION_COMMIT");
        display = prop.getProperty("VERSION_DISPLAY");
        full = prop.getProperty("VERSION_FULL");
        scm = prop.getProperty("VERSION_SCM");
        tag = prop.getProperty("VERSION_TAG");
        lastTag = prop.getProperty("VERSION_LAST_TAG");
        dirty = prop.getProperty("VERSION_DIRTY").equals("true");
    }

    //region getters

    public String getBuild() {
        return build;
    }

    public String getBranch() {
        return branch;
    }

    public String getBase() {
        return base;
    }

    public String getBranchID() {
        return branchID;
    }

    public String getBranchType() {
        return branchType;
    }

    public String getCommit() {
        return commit;
    }

    public String getDisplay() {
        return display;
    }

    public String getFull() {
        return full;
    }

    public String getScm() {
        return scm;
    }

    public String getTag() {
        return tag;
    }

    public String getLastTag() {
        return lastTag;
    }

    public boolean isDirty() {
        return dirty;
    }

    //endregion

}

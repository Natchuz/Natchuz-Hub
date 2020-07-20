package com.natchuz.hub.utils.mojang;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MojangUser {

    private UUID uuid;
    private String username;
    @SerializedName("username_history")
    private List<Username> usernameHistory;

    /**
     * @return UUID of user
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * @return Current username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return History of username changes
     */
    public List<Username> getUsernameHistory() {
        return usernameHistory;
    }

    @Override
    public String toString() {
        return "MojangUser{" + "uuid=" + uuid + ", username='" + username + '\'' + ", usernameHistory="
                + usernameHistory + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MojangUser that = (MojangUser) o;
        return Objects.equals(uuid, that.uuid) && Objects.equals(username, that.username)
                && Objects.equals(usernameHistory, that.usernameHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, username, usernameHistory);
    }

    public static class Username {
        private String username;
        @SerializedName("changed_at")
        private Date changedAt;

        /**
         * @return Username
         */
        public String getUsername() {
            return username;
        }

        /**
         * @return When username was set
         * @apiNote Username set when creating account does not have date so date is null
         */
        public Date getChangedAt() {
            return changedAt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Username username1 = (Username) o;
            return Objects.equals(username, username1.username) && Objects.equals(changedAt, username1.changedAt);
        }

        @Override
        public int hashCode() {
            return Objects.hash(username, changedAt);
        }

        @Override
        public String toString() {
            return "Username{" + "username='" + username + '\'' + ", changedAt=" + changedAt + '}';
        }
    }
}

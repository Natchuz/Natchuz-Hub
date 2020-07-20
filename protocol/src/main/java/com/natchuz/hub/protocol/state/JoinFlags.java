package com.natchuz.hub.protocol.state;

/**
 * Join flags, are flags with which the player is sent to different Paper server.
 * They are put in state server (which is redis db)
 */
public enum JoinFlags {
    /**
     * When player joins Bungeecord
     */
    PROXY_JOIN,
    /**
     * When player returns to lobby.
     */
    LOBBY_RETURN
}

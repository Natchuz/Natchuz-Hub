package com.natchuz.hub.core.user;

/**
 * This enum indicates result of inviting, removing, etc. player from friends
 */
public enum InvitingResult {
    /**
     * Operation performed successfully
     */
    OK,
    /**
     * Players are already connected
     */
    ALREADY_ACCEPTED,
    /**
     * Player wasn't invited
     */
    NOT_INVITED,
    /**
     * Players are not connected
     */
    NOT_CONNECTED,
    /**
     * Player to invite himself
     */
    SELF_INVITE,
    /**
     * Internal database error
     */
    INTERNAL_ERROR
}

package com.natchuz.hub.protocol.state;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.EnumSet;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PlayerInfo {
    private UUID uuid;
    private String targetServer;
    private EnumSet<JoinFlags> joinFlags;
}

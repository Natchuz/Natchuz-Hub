package com.natchuz.hub.core.modules;

/**
 * Test for {@link FriendsNotifier}
 */
public class FriendsNotifierTest {

    /*@Mock
    private PrivilegedFacade client;
    @Mock
    private Protocol protocol;
    @Mock
    private ExchangedClient exchangedClient;
    private static Server mockServer;

    /*
        Target is a player which friends of joins the proxy
     *//*
    @Mock
    private Player targetPlayer;
    @Mock
    private User targetUser;
    private UUID targetUUID;
    private List<UUID> targetUserFriends;

    /*
        Reason is a player which joins the proxy
     *//*
    @Mock
    private User reasonUser;
    private UUID reasonUUID;
    private List<UUID> reasonUserFriends;

    private FriendsNotifier friendsNotifier;

    private void setupData() {
        targetUUID = UUID.fromString("365b77dd-659f-4ae6-8f9c-f3f34a82b293");

        targetUserFriends = Lists.newArrayList(
                "9de49fd3-9069-4616-9ba6-e390c86593af",
                "be85f006-6ee7-42cf-a6cd-c04cd90da79a",
                "8e9c1a67-92d1-412d-96a8-a8dc50679d17",
                "449d90d2-f637-45ee-ac94-28755a701a2d",
                "34321872-7a0d-45de-98c1-843a58464c46",
                "f63083fc-7fc4-4420-92e1-814d8435f968",
                "17a8f6d7-360d-4d1f-92d5-22ae5eb49be1",
                "dd53270a-4558-4ad9-b15e-1607560c5d30",
                "10a4679a-0566-452b-a7ef-09c4b7599bdb",
                "18c1b4e5-4e05-42c6-b0d0-bde354c1ef0d"
        ).stream().map(UUID::fromString).collect(Collectors.toList());

        reasonUUID = UUID.fromString("8e9c1a67-92d1-412d-96a8-a8dc50679d17");

        reasonUserFriends = Lists.newArrayList(
                "365b77dd-659f-4ae6-8f9c-f3f34a82b293",
                "dd53270a-4558-4ad9-b15e-1607560c5d30",
                "10a4679a-0566-452b-a7ef-09c4b7599bdb",
                "18c1b4e5-4e05-42c6-b0d0-bde354c1ef0d",
                "772fa48a-c182-44f6-9305-fd293e53dd01",
                "543ca525-bc65-4076-8369-1cc418be9db5",
                "288d3ba9-8932-4030-b9fb-b3b049e54072",
                "bf14d934-2634-4580-aea0-608d00e82dad",
                "66839be8-d4b5-4fe8-b0c9-96ff9ebc6fdb",
                "a9a96871-8d34-4242-b0cf-3113f39820d4"
        ).stream().map(UUID::fromString).collect(Collectors.toList());
    }

    @BeforeAll
    public static void setupBukkit() {
        mockServer = mock(Server.class);
        when(mockServer.getName()).thenReturn("");
        when(mockServer.getVersion()).thenReturn("");
        when(mockServer.getBukkitVersion()).thenReturn("");
        when(mockServer.getLogger()).thenReturn(Logger.getAnonymousLogger());
        Bukkit.setServer(mockServer);
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupData();

        // setup client and protocol
        when(protocol.getClient()).thenReturn(exchangedClient);

        when(client.getProfile(targetPlayer)).thenReturn(targetUser);
        when(targetPlayer.getUniqueId()).thenReturn(targetUUID);
        when(targetUser.getFriends()).thenReturn(targetUserFriends);

        when(client.getProfile(reasonUUID)).thenReturn(reasonUser);

        friendsNotifier = new FriendsNotifier(client, protocol);
    }

    @Test
    public void testSubscribing() {
        PlayerJoinEvent event = new PlayerJoinEvent(targetPlayer, "");
        friendsNotifier.onPlayerJoin(event);

        verify(exchangedClient).subscribe("player.friend.9de49fd3906946169ba6e390c86593af");
        verify(exchangedClient).subscribe("player.friend.be85f0066ee742cfa6cdc04cd90da79a");
        verify(exchangedClient).subscribe("player.friend.10a4679a0566452ba7ef09c4b7599bdb");
    }

    @Test
    public void testUnsubscribing() {
        PlayerQuitEvent event = new PlayerQuitEvent(targetPlayer, "");
        friendsNotifier.onPlayerQuit(event);

        verify(exchangedClient).unsubscribe("player.friend.343218727a0d45de98c1843a58464c46");
        verify(exchangedClient).unsubscribe("player.friend.dd53270a45584ad9b15e1607560c5d30");
        verify(exchangedClient).unsubscribe("player.friend.18c1b4e54e0542c6b0d0bde354c1ef0d");
    }

    @Test
    public void testJoinMessage() {
        when(client.getProfile(eq(UUID.fromString("8e9c1a67-92d1-412d-96a8-a8dc50679d17"))))
                .thenReturn(reasonUser);
        when(reasonUser.getFriends()).thenReturn(reasonUserFriends);

        when(mockServer.getPlayer(any(UUID.class))).thenReturn(null);
        when(mockServer.getPlayer(eq(targetUUID))).thenReturn(targetPlayer);

        friendsNotifier.joinMessage(new String[]{"8e9c1a6792d1412d96a8a8dc50679d17"});

        verify(targetPlayer).sendMessage(anyString());
    }

    @Test
    public void testLeaveMessage() {
        when(client.getProfile(eq(UUID.fromString("8e9c1a67-92d1-412d-96a8-a8dc50679d17"))))
                .thenReturn(reasonUser);
        when(reasonUser.getFriends()).thenReturn(reasonUserFriends);

        when(mockServer.getPlayer(any(UUID.class))).thenReturn(null);
        when(mockServer.getPlayer(eq(targetUUID))).thenReturn(targetPlayer);

        friendsNotifier.leftMessage(new String[]{"8e9c1a6792d1412d96a8a8dc50679d17"});

        verify(targetPlayer).sendMessage(anyString());
    }*/
}

# Natchuz Hub

[![](https://img.shields.io/discord/729693761235451914?color=blue&label=discord&logo=discord)](https://discord.gg/tyzbDfM)
![](https://github.com/Natchuz-Hub/Natchuz-Hub/workflows/Build%20CI/badge.svg)
![](https://img.shields.io/badge/minecraft%20version-1.12.2-brightgreen)
![](https://img.shields.io/github/stars/Natchuz-Hub/Natchuz-Hub?style=social)

**NOTE: currently in migration to Sponge, many things may not work**

**Currently work is still in progress!**

Open source Minecraft minigames server targeting the latest versions of game.
Project is licensed under [AGPL v3 License](https://tldrlegal.com/license/gnu-affero-general-public-license-v3-(agpl-3.0))

## General info

Our goal is to build both fun and competitive minigames.
We decided to utilize all new Minecraft features added from time of 1.8 release, 
that would be used to create new and uniques games.

Project currently works on PaperMC, uses MongoDB as database, Redis as temporary storage and RabbitMQ as message broker.

This will of course change in the near future.

Current features:
- Friends
- Basic KitPvP gamemode
- Multi server system
- Map loading

## Contributing

This project is still in early development, so we don't need any maps etc. 
If you are not a developer, and you somehow got here, 
and still wanna get involved, you can jump on our [Discord]
and ask if there is anything you can do.
Otherwise, please see instructions below.

**Note:** All instructions described here targets linux system. 
This may differ from Windows and other UNIX-like systems.

### Preparing

You will need Java 8, Kotlin compiler and [Docker] along with [Docker Compose] 
to work [without root privileges](https://docs.docker.com/engine/install/linux-postinstall/#manage-docker-as-a-non-root-user).
You will also need a good IDE with lombok support - IntelliJ would be the best.

After cloning, all you need is to simply import as Gradle project to your IDE.

### Building & Running

**Note:** Project still runs on 1.12.2 due to current version of Sponge

You can build using ``docker`` task. This will produce several Docker images.
They can be launched using ``docker-compose up``. This will perform full network launch, 
with all backends and one server per gamemode.

Since full network consumes a lot of resources, and it's a lot harder to debug docker containers,
you can use task ``run`` coming from ``GamemodePlugin`` located under ``./buildSrc``. 
It's a gradle project that setups simple testing server (using LOCAL_SERVER_CONFIGURATION from ``core`` project) 
and launches only one server, faking all backends. Supporting commands like creating fake users on-the fly are planned 
to be implemented soon. This is also the reason you can see ``ServerContext`` concept in ``core`` project.

For more information about specific project, search for readme inside its directory.

**Note:** Running full network may not work now  

### Coding conventions and Future

Longer term plan is to fully replace NMS with an alternative server engine like [Cuberite], 
or even better - [Feather], once they would be fully functional and stable.
I will consider creating Java binds for Cuberite or even full rewrite into Rust for Feather.

[Docker]: https://docs.docker.com/get-docker/
[Discord]: https://discord.gg/tyzbDfM
[Docker Compose]: https://docs.docker.com/compose/install/
[Cuberite]: https://github.com/cuberite/cuberite
[Feather]: https://github.com/feather-rs/feather
# Natchuz Hub

[![](https://img.shields.io/discord/729693761235451914?color=blue&label=discord&logo=discord)](https://discord.gg/tyzbDfM)
![](https://github.com/Natchuz-Hub/Natchuz-Hub/workflows/Build%20CI/badge.svg)
![](https://img.shields.io/badge/minecraft%20version-1.14.4-brightgreen)
![](https://img.shields.io/github/stars/Natchuz-Hub/Natchuz-Hub?style=social)

**Currently work is still in progress!**

Open source Minecraft minigames server targeting the latest versions of game.
Project is licensed under [AGPL v3 License](https://tldrlegal.com/license/gnu-affero-general-public-license-v3-(agpl-3.0))

## General info

Our goal is to build both fun and competitive minigames.
We decided to utilize all new Minecraft features added since the 1.8 release of the game, 
that would be used to create new and uniques games.

Project currently runs on PaperMC, uses MongoDB as the database, Redis as temporary storage and RabbitMQ as a message broker.

This will, very likely, change in the near future.

Currently implemented features:
- Friends
- Basic KitPvP gamemode
- Multi server system
- Map loading

## Contributing

This project is still in early development, so we don't need any maps yet etc. 
If you are not a developer, somehow got here, 
and still wanna get involved, you can jump in our [Discord]
and ask if there is anything to do.
Otherwise, please see instructions below.

**Note:** All instructions described here target the GNU/Linux system. 
This may differ for Windows and other UNIX-like systems.

### Preparing

You will need Java 8, the Kotlin compiler and [Docker] along with [Docker Compose] 
to work [without root privileges](https://docs.docker.com/engine/install/linux-postinstall/#manage-docker-as-a-non-root-user).
You will also need a good IDE with lombok support - We highly recommend IntelliJ.

After cloning, all you need to do is to simply import as a Gradle project to your IDE of choice (We use IntelliJ 😉).

### Building & Running

**Note:** Project still runs on 1.14 since I had no time to upgrade to the latest version.

You can build using the ``docker`` task. This will produce several Docker images.
They can be launched using ``docker-compose up``. This will perform a full launch of the network, 
with all backends and one server per gamemode.

Since running the full network consumes a lot of resources, and it's a lot harder to debug in docker containers,
you can use the task ``run`` from ``GamemodePlugin`` located under ``./buildSrc``. 
It's a gradle project that sets up a simple testing server (using LOCAL_SERVER_CONFIGURATION from the ``core`` project) 
and launches only one server, spoofing all backends. Supporting commands like creating fake users on-the fly are planned 
to be implemented soon. This is also the reason you can see ``ServerContext`` concept in ``core`` project.

For more information about a specific part of the project, search for a README inside its respective directory.  

### Coding conventions and Future

Although the Spigot API is a shit-fest of singletons, we try to at least make our code look clean.
Currently, the code is a big mess, because of how terrible the Spigot API is and how many bad habits and designs it involves.
I'm looking forward to replacing it with the only superior alternative - the Sponge API, as soon as build for newest Minecraft will 
be available. Once that's in place, it will be also time to rewrite some code into a way better language - Kotlin.
Longer term plan is to fully replace NMS with an alternative server engine like [Cuberite], 
or even better - [Feather], once it becomes fully functional and stable.
I will consider creating Java binds for Cuberite or even creating a full rewrite into Rust for Feather.

About the backend: It doesn't exist, and I'm currently investigating a proper way to implement it.

[Docker]: https://docs.docker.com/get-docker/
[Discord]: https://discord.gg/tyzbDfM
[Docker Compose]: https://docs.docker.com/compose/install/
[Cuberite]: https://github.com/cuberite/cuberite
[Feather]: https://github.com/feather-rs/feather

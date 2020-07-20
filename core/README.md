# Core

This directory contains a Spigot plugin that is shipped with every Paper instance.
It contains all features common for every server, like:
- Friends
- Cosmetics
- Map loading
- Users

It also handles communication with different microservices, bungeecord proxy, and other servers.
However, to speed up gamemode development, communication is hidden behind abstraction layer called "Context".
Server can run in following contexts:
- Network context, that runs in a container, where all databases, microservices, etc. are linked and available
- Standalone context, that runs locally, without any need to launch any database, and fakes all these backends
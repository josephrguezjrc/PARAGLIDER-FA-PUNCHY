# Paragliders — Fabric 1.21.11 port

Unofficial Fabric adaptation of Tictim/Paraglider, upstream commit
4d315c7d6196ae0292e96d7e618f801d9c50344c, branch dev/1.21.11.
Original license: GNU GPL v3. All original game assets are retained.

Build with Java 21: `./gradlew build` (Gradle 9.2.1, Fabric Loom 1.14.10).
Integration tests: `./gradlew -PvalidationTest runGametest`.
Built mod: `build/libs/Paraglider-fabric-1.21.11-21.11.0-fabric-port.1.jar`.
Runtime requires Fabric Loader >=0.18.4, Fabric API 0.141.1+1.21.11,
and Forge Config API Port 21.11.1 for Fabric.

The Fabric adaptation replaces deferred registration, persistent attachments,
network handlers, plugin discovery, server/client events, resource conditions,
custom ingredients and loot modifications. Minecraft 1.21.11 code and assets
come from the upstream version for that Minecraft release. Headless integration
tests cover gliding, stamina, heart/stamina vessels and persistence, statue
bartering, brewing, spirit-orb loot and preview packet codecs. Client class loading
and item-property registration were checked separately; visual rendering and
other-mod compatibility were not tested in a running graphical client.

`libs/forgeconfigapiport.jar` is an unmodified upstream Fabric dependency.
Night Config is supplied by this dependency in installed Minecraft; the build
also declares it explicitly for the development runtime.

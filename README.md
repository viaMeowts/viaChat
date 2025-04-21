# viaChat

A simple server-side Fabric mod for Minecraft 1.21.4 that adds local and global chat channels.

## Features

*   **Local Chat:** Messages sent normally are only visible to players within a 100-block radius (configurable in code). Local messages are prefixed with `[L]` (Green) and have gray text.
*   **Global Chat:** Messages prefixed with `!` are sent globally to all players on the server. Global messages are prefixed with `[G]` (Yellow) and have white text.
*   **Player Chat Mode Toggle:** Players can choose whether the `!` prefix triggers global or local chat for them.
*   **Server-Side Only:** This mod only needs to be installed on the server. Clients do not need it.
*   **Simple Localization:** System messages from the mod can be switched between English and Russian by an operator.

## Commands

### Player Commands

*   `/viachat global !`
    *   Sets your preference so that messages starting with `!` are sent to **global** chat. Messages without `!` are sent to local chat. (This is the default behavior).
*   `/viachat local !`
    *   Sets your preference so that messages starting with `!` are sent to **local** chat. Messages without `!` are sent to global chat.
*   `/viachat`
    *   Shows your current chat mode setting (what the `!` prefix does for you).

### Operator Commands

*   `/viachat lang <en|ru>`
    *   Sets the language for system messages sent by viaChat (command feedback, errors) for the entire server. Requires operator permissions.
    *   Example: `/viachat lang ru`
*   `/viachat lang`
    *   Shows the currently selected language for viaChat system messages.

## Installation

1.  Ensure you have a Fabric server setup for Minecraft 1.21.4.
2.  Download the latest `viaChat-*.jar` file from the [Releases](https://github.com/viaMeowts/viaChat/releases)
3.  Place the downloaded `.jar` file into your server's `mods` folder.
4.  Ensure you have [Fabric API](https://modrinth.com/mod/fabric-api) installed in your server's `mods` folder (required dependency).
5.  Restart your server.

## Compatibility

*   **Minecraft:** 1.21.4
*   **Requires:** Fabric Loader, Fabric API

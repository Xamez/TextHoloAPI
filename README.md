# FastInv
[![JitPack](https://jitpack.io/v/Xamez/TextHoloAPI.svg)](https://jitpack.io/#Xamez/TextHoloAPI)

Lightweight and easy-to-use text hologram API for Bukkit plugins.

## Features
* Very small API without dependencies
* Works with all Bukkit versions from 1.8 to 1.17.
* Easy to use

## Installation

### Gradle
```groovy
plugins {
    id 'com.github.johnrengelman.shadow' version '7.0.0'
}

repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Xamez:TextHoloAPI:1.0'
}

shadowJar {
    // Replace 'com.yourpackage' with the package of your plugin 
    relocate 'fr.xamez.textholoapi', 'com.yourpackage.textholoapi'
}
```

### Manual

Just copy these 3 packages into your plugin.

## Usage

### Register FastInv
Before creating text holo, you just need to register your plugin by adding `TextHoloAPI.register(this);` in the `onEnable()` method of your plugin:
```java
@Override
public void onEnable() {
    TextHoloAPI.register(this);
}
```

### Creating an text hologram

Now you can create a text hologram:

```java
package fr.xamez.textholoapi;

import fr.xamez.textholoapi.text.TextHoloAPI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class Example {

    public void sendTextHolo(Player p){
        final ArrayList<String> lines = new ArrayList<>(Arrays.asList("My first line", "§cA second line with color", "&b&lThe last line"));
        TextHoloAPI.displayText(p, p.getLocation().add(0, 2, 0), lines, 1, 40, 0.25, true);
    }

}
```
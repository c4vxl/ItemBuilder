# ItemBuilder Plugin for Minecraft

ItemBuilder is a utility library for creating and interacting with ItemStacks in Minecraft plugins.

## Features

- Easily create custom ItemStacks with specified materials, names, lore, and enchantments.
- Register click event handlers for your custom ItemStacks.

## Installation

To use the ItemBuilder library in your plugin, follow these steps:

1. Clone the `ItemBuilder.kt` file in your project.

2. Register the library in your plugin's main class:

```kotlin
ItemBuilder.register(this);
```

## Example
```kotlin
val itemStack: ItemStack = ItemBuilder(Material.STONE, "My cool item", mutableListOf("This is a line", "This is a new line"), 10)
    .enchant(Enchantment.ARROW_KNOCKBACK, 1)
    .onClick { event ->
        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
            event.player.sendMessage("test")
        }
    }
    .onInvClick { event ->
        if (event.action == InventoryAction.PICKUP_ALL) {
            event.whoClicked.sendMessage("test2")
        }
    }
    .build()
```
This example code will create a ItemStack with the displayname "My cool Item", an lore with "This is a line" in the first line, "This is a new line" in the second line. The itemstack will be a Material.STONE. There will be 10 items in the itemstack.
When a player rightclicks the item he will recive the message "test".
When a player left clicks on the item in an inventory, he will recive the message "test2".

---

## Developer
This Project was Developed by [c4vxl](https://c4vxl.de)

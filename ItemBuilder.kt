package de.c4vxl.pluginbrowser.Util

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin

/*
*
* Item builder by @c4vxl
*   -> https://github.com/c4vxl/
*   -> https://c4vxl.de/
*
*/

object ItemBuilderListener : Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val currentItem: ItemStack = event.currentItem ?: return

        ItemBuilder.onInvClickHandler.filter { currentItem.isSimilar(it.key) }.forEach { (_, u) ->
            u.invoke(event)
        }
    }

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        val item: ItemStack = event.item ?: return
        ItemBuilder.onItemClickHandler.filter { item.isSimilar(it.key) }.forEach { (_, u) ->
            u.invoke(event)
        }
    }
}

class ItemBuilder(private var material: Material, private var name: String = "", private var lore: MutableList<String>? = null, private var amount: Int = 1) {
    companion object {
        val onItemClickHandler: MutableMap<ItemStack, (event: PlayerInteractEvent) -> Unit> = mutableMapOf()
        val onInvClickHandler: MutableMap<ItemStack, (event: InventoryClickEvent) -> Unit> = mutableMapOf()

        lateinit var plugin: JavaPlugin

        fun register(plugin: JavaPlugin) {
            ItemBuilder.plugin = plugin
            Bukkit.getPluginManager().registerEvents(ItemBuilderListener, plugin)
        }

        fun fromItemStack(itemStack: ItemStack): ItemBuilder {
            val itemMeta: ItemMeta = itemStack.itemMeta
            return ItemBuilder(itemStack.type, itemMeta.displayName, itemMeta.lore, itemStack.amount)
        }
    }

    private val itemStack: ItemStack = ItemStack(material, amount)
    private val itemMeta: ItemMeta = itemStack.itemMeta
    val enchantments: HashMap<Enchantment, Int> = HashMap()

    fun setName(name: String): ItemBuilder {
        this.name = name
        return this
    }

    fun setLore(lore: MutableList<String>?): ItemBuilder {
        this.lore = lore
        return this
    }

    fun setAmount(amount: Int): ItemBuilder {
        this.amount = amount
        return this
    }

    fun setMaterial(material: Material): ItemBuilder {
        this.material = material
        return this
    }

    fun unbreakable(boolean: Boolean): ItemBuilder {
        itemMeta.isUnbreakable = boolean
        return this
    }

    fun enchant(enchantment: Enchantment, lvl: Int): ItemBuilder {
        enchantments[enchantment] = lvl
        return this
    }

    private var onInvClick: ((event: InventoryClickEvent) -> Unit) = {}
    private var onItemClick: ((event: PlayerInteractEvent) -> Unit) = {}

    fun onInvClick(x: (event: InventoryClickEvent) -> Unit): ItemBuilder {
        onInvClick = x
        return this
    }

    fun onClick(x: (event: PlayerInteractEvent) -> Unit): ItemBuilder {
        onItemClick = x
        return this
    }

    fun build(): ItemStack {
        itemMeta.lore = lore
        itemMeta.setDisplayName(name)
        itemStack.amount = amount
        itemStack.setItemMeta(itemMeta)
        itemStack.addUnsafeEnchantments(enchantments)

        onInvClickHandler[itemStack] = onInvClick
        onItemClickHandler[itemStack] = onItemClick
        return itemStack
    }
}

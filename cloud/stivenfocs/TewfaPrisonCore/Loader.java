package cloud.stivenfocs.TewfaPrisonCore;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Item;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.enchantments.Enchantment;

import org.bukkit.Material;

import java.util.*;
import java.io.File;
import java.lang.NullPointerException;

import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;

import org.bukkit.Location;

import org.bukkit.block.Block;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.util.Vector;

import org.bukkit.event.inventory.BrewEvent;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.*;

public class Loader extends JavaPlugin implements Listener {
	
	public static Loader plugin;
	
	public Item shop_hologram;
	
	public Inventory upgradesMenu;
	public ItemStack efficiencyUpgradeButton;
	public ItemStack fortuneUpgradeButton;
	public ItemStack playerLevelUpgradeButton;
	
	public Inventory shopMenu;
	
	public Inventory armorUpgradesMenu;
	public ItemStack protectionUpgradeButton;
	
	public Inventory swordUpgradesMenu;
	public ItemStack sharpnessUpgradeButton;
	
	public Inventory bowUpgradesMenu;
	public ItemStack powerUpgradeButton;
	
	public Integer block_regeneration_delay = 3;
	public Integer data_save_delay = 20;
	
	public Location pvpArea_pos1;
	public Location pvpArea_pos2;
	
	public ItemStack customHologramItem(ItemStack item) {
		item = item.clone();
		ItemMeta itemMeta = item.getItemMeta();
		List<String> new_lore = new ArrayList<>();
		new_lore.add("§cYou should not have item..");
		itemMeta.setLore(new_lore);
		item.setItemMeta(itemMeta);
		return item;
	}
	
	public final List<ItemStack> shop_items_ = new ArrayList<>();
	public Iterator<ItemStack> shop_hologram_items;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		getCommand("prisoncore").setExecutor(this);
		getCommand("spawn").setExecutor(this);
		Bukkit.getPluginManager().registerEvents(this, this);
		
		reload();
		
		upgradesMenu = Bukkit.createInventory(null, 27, getConfig().getString("options.upgrades_menu_title"));
	
		efficiencyUpgradeButton = new ItemStack(Material.BOOK, 1);
		ItemMeta efficiencyUpgradeButtonMeta = efficiencyUpgradeButton.getItemMeta();
		efficiencyUpgradeButtonMeta.setDisplayName("§bUpgrade Efficiency");
		List<String> efficiencyUpgradeButton_lore = new ArrayList<>();
		efficiencyUpgradeButton_lore.add("");
		efficiencyUpgradeButton_lore.add("§f+1 Efficiency §bfor");
		efficiencyUpgradeButton_lore.add("§f" + getConfig().getInt("options.upgrade.efficiency.cost") + " §bExp levels.");
		efficiencyUpgradeButtonMeta.setLore(efficiencyUpgradeButton_lore);
		efficiencyUpgradeButton.setItemMeta(efficiencyUpgradeButtonMeta);
		
		fortuneUpgradeButton = new ItemStack(Material.BOOK, 1);
		ItemMeta fortuneUpgradeButtonMeta = fortuneUpgradeButton.getItemMeta();
		fortuneUpgradeButtonMeta.setDisplayName("§bUpgrade Fortune");
		List<String> fortuneUpgradeButton_lore = new ArrayList<>();
		fortuneUpgradeButton_lore.add("");
		fortuneUpgradeButton_lore.add("§f+1 Fortune §bfor");
		fortuneUpgradeButton_lore.add("§f" + getConfig().getInt("options.upgrade.fortune.cost") + " §bExp levels.");
		fortuneUpgradeButtonMeta.setLore(fortuneUpgradeButton_lore);
		fortuneUpgradeButton.setItemMeta(fortuneUpgradeButtonMeta);
		
		playerLevelUpgradeButton = new ItemStack(Material.EMERALD, 1);
		ItemMeta playerLevelUpgradeButtonMeta = playerLevelUpgradeButton.getItemMeta();
		playerLevelUpgradeButtonMeta.setDisplayName("§6Player Level Upgrade");
		List<String> playerLevelUpgradeButton_lore = new ArrayList<>();
		playerLevelUpgradeButton_lore.add("");
		playerLevelUpgradeButton_lore.add("§f+1 Player Level §efor");
		playerLevelUpgradeButton_lore.add("§f" + getConfig().getInt("options.upgrade.player_level.cost") + " §eExp levels.");
		playerLevelUpgradeButtonMeta.setLore(playerLevelUpgradeButton_lore);
		playerLevelUpgradeButton.setItemMeta(playerLevelUpgradeButtonMeta);
		
		upgradesMenu.setItem(11, efficiencyUpgradeButton);
		upgradesMenu.setItem(12, fortuneUpgradeButton);
		upgradesMenu.setItem(14, playerLevelUpgradeButton);
		
		shopMenu = Bukkit.createInventory(null, 36, getConfig().getString("options.shop_menu_title"));
		
		shopMenu.setItem(10, new ItemStack(Material.GOLDEN_APPLE));
		shopMenu.setItem(11, new ItemStack(Material.STICK));
		shopMenu.setItem(12, new ItemStack(Material.IRON_INGOT));
		shopMenu.setItem(13, new ItemStack(Material.DIAMOND));
		shopMenu.setItem(14, new ItemStack(Material.STRING));
		shopMenu.setItem(15, new ItemStack(Material.ARROW, 5));
		shopMenu.setItem(16, new ItemStack(Material.MILK_BUCKET));
		shopMenu.setItem(19, new ItemStack(Material.POTION, 1, (short) 8201));
		shopMenu.setItem(20, new ItemStack(Material.POTION, 1, (short) 8194));
		shopMenu.setItem(21, new ItemStack(Material.POTION, 1, (short) 16388));
		shopMenu.setItem(22, new ItemStack(Material.POTION, 1, (short) 16453));
		shopMenu.setItem(23, new ItemStack(Material.POTION, 1, (short) 16424));
		shopMenu.setItem(24, new ItemStack(Material.POTION, 1, (short) 16417));
		shopMenu.setItem(25, new ItemStack(Material.POTION, 1, (short) 16426));
		
		armorUpgradesMenu = Bukkit.createInventory(null, 27, getConfig().getString("options.armor_upgrades_menu_title"));
		
		protectionUpgradeButton = new ItemStack(Material.BOOK, 1);
		ItemMeta protectionUpgradeButtonMeta = protectionUpgradeButton.getItemMeta();
		protectionUpgradeButtonMeta.setDisplayName("§bUpgrade Protection");
		List<String> protectionUpgradeButton_lore = new ArrayList<>();
		protectionUpgradeButton_lore.add("");
		protectionUpgradeButton_lore.add("§f+1 Protection §efor");
		protectionUpgradeButton_lore.add("§f" + getConfig().getInt("options.upgrade.protection.cost") + " §eExp levels.");
		protectionUpgradeButtonMeta.setLore(protectionUpgradeButton_lore);
		protectionUpgradeButton.setItemMeta(protectionUpgradeButtonMeta);
		
		armorUpgradesMenu.setItem(13, protectionUpgradeButton);
		
		swordUpgradesMenu = Bukkit.createInventory(null, 27, getConfig().getString("options.sword_upgrades_menu_title"));
		
		sharpnessUpgradeButton = new ItemStack(Material.BOOK, 1);
		ItemMeta sharpnessUpgradeButtonMeta = sharpnessUpgradeButton.getItemMeta();
		sharpnessUpgradeButtonMeta.setDisplayName("§bUpgrade Sharpness");
		List<String> sharpnessUpgradeButton_lore = new ArrayList<>();
		sharpnessUpgradeButton_lore.add("");
		sharpnessUpgradeButton_lore.add("§f+1 Sharpness §efor");
		sharpnessUpgradeButton_lore.add("§f" + getConfig().getInt("options.upgrade.sharpness.cost") + " §eExp levels.");
		sharpnessUpgradeButtonMeta.setLore(sharpnessUpgradeButton_lore);
		sharpnessUpgradeButton.setItemMeta(sharpnessUpgradeButtonMeta);
		
		swordUpgradesMenu.setItem(13, sharpnessUpgradeButton);
		
		bowUpgradesMenu = Bukkit.createInventory(null, 27, getConfig().getString("options.bow_upgrades_menu_title"));
		
		powerUpgradeButton = new ItemStack(Material.BOOK, 1);
		ItemMeta powerUpgradeButtonMeta = powerUpgradeButton.getItemMeta();
		powerUpgradeButtonMeta.setDisplayName("§bUpgrade Power");
		List<String> powerUpgradeButton_lore = new ArrayList<>();
		powerUpgradeButton_lore.add("");
		powerUpgradeButton_lore.add("§f+1 Power §efor");
		powerUpgradeButton_lore.add("§f" + getConfig().getInt("options.upgrade.power.cost") + " §eExp levels.");
		powerUpgradeButtonMeta.setLore(powerUpgradeButton_lore);
		powerUpgradeButton.setItemMeta(powerUpgradeButtonMeta);
		
		bowUpgradesMenu.setItem(13, powerUpgradeButton);
		
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			data_save_delay--;
			if (data_save_delay < 1) {
				data_save_delay = getConfig().getInt("options.data_save_delay");
				saveDataConfig();
			}
			
			block_regeneration_delay--;
			if (block_regeneration_delay < 1) {
				block_regeneration_delay = getConfig().getInt("options.block_regeneration_delay");
				
				List<String> blocks_location = getConfig().getStringList("options.blocks_location");
				if (blocks_location.size() > 0) {
					String location_string = blocks_location.get(new Random().nextInt(blocks_location.size() - 1));
					String[] loc = location_string.split(",");
					Location loc_ = new Location(Bukkit.getWorld(loc[0]),Double.parseDouble(loc[1]),Double.parseDouble(loc[2]),Double.parseDouble(loc[3]));
					loc_.getWorld().getBlockAt(loc_).setType(Material.IRON_ORE);
				}
			}
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.setFoodLevel(100);
			}
			
			for (UUID playerUUID : new ArrayList<>(combat_tag.keySet())) {
				combat_tag.put(playerUUID, combat_tag.get(playerUUID) - 1);
				if (combat_tag.get(playerUUID) < 1) combat_tag.remove(playerUUID);
			}
		}, 0L, 20L);
		
		
		for (ItemStack shop_menu_item : shopMenu.getContents()) {
			if (shop_menu_item != null) shop_items_.add(shop_menu_item);
		}
		shop_hologram_items = new ArrayList<>(shop_items_).iterator();
		
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			if (shop_hologram_items.hasNext()) {
				shop_hologram.setItemStack(customHologramItem(shop_hologram_items.next()));
			} else {
				shop_hologram_items = new ArrayList<>(shop_items_).iterator();
				shop_hologram.setItemStack(customHologramItem(shop_hologram_items.next()));
			}
		}, 0L, 5L);
	}
	
	public void onDisable() {
		for (HumanEntity viewer : new ArrayList<>(upgradesMenu.getViewers())) {
			viewer.closeInventory();
		}
		for (HumanEntity viewer : new ArrayList<>(shopMenu.getViewers())) {
			viewer.closeInventory();
		}
		for (HumanEntity viewer : new ArrayList<>(armorUpgradesMenu.getViewers())) {
			viewer.closeInventory();
		}
		for (HumanEntity viewer : new ArrayList<>(swordUpgradesMenu.getViewers())) {
			viewer.closeInventory();
		}
		for (HumanEntity viewer : new ArrayList<>(bowUpgradesMenu.getViewers())) {
			viewer.closeInventory();
		}
		if (shop_hologram != null) shop_hologram.remove();
	}
	
	public void reload() {
		reloadConfig();
		
		getConfig().options().header("Developed with LOV by StivenFocs");
		getConfig().options().copyDefaults(true);
		
		getConfig().addDefault("options.spawn_teleport_offset", 0);
		getConfig().addDefault("options.upgrades_menu_title", "Upgrades Menu");
		getConfig().addDefault("options.shop_menu_title", "Shop Menu");
		getConfig().addDefault("options.shop_hologram.name", "§6§lShop");
		getConfig().addDefault("options.shop_hologram.location", "");
		getConfig().addDefault("options.armor_upgrades_menu_title", "Armor Upgrades Menu");
		getConfig().addDefault("options.sword_upgrades_menu_title", "Sword Upgrades Menu");
		getConfig().addDefault("options.bow_upgrades_menu_title", "Bow Upgrades Menu");
		getConfig().addDefault("options.upgrade.efficiency.cost", 15);
		getConfig().addDefault("options.upgrade.fortune.cost", 20);
		getConfig().addDefault("options.upgrade.player_level.cost", 300);
		getConfig().addDefault("options.upgrade.protection.cost", 10);
		getConfig().addDefault("options.upgrade.sharpness.cost", 15);
		getConfig().addDefault("options.upgrade.power.cost", 30);
		getConfig().addDefault("options.main_pickaxe_material", "IRON_PICKAXE");
		List<String> new_allowed_tools = new ArrayList<>();
		new_allowed_tools.add("IRON_PICKAXE");
		getConfig().addDefault("options.allowed_tools", new_allowed_tools);
		List<String> new_allowed_armors = new ArrayList<>();
		new_allowed_armors.add("DIAMOND_BOOTS");
		new_allowed_armors.add("DIAMOND_LEGGINGS");
		new_allowed_armors.add("DIAMOND_CHESTPLATE");
		new_allowed_armors.add("DIAMOND_HELMET");
		new_allowed_armors.add("IRON_BOOTS");
		new_allowed_armors.add("IRON_LEGGINGS");
		new_allowed_armors.add("IRON_CHESTPLATE");
		new_allowed_armors.add("IRON_HELMET");
		getConfig().addDefault("options.allowed_armors", new_allowed_armors);
		List<String> new_allowed_swords = new ArrayList<>();
		new_allowed_swords.add("IRON_SWORD");
		new_allowed_swords.add("DIAMOND_SWORD");
		getConfig().addDefault("options.allowed_swords", new_allowed_swords);
		if (getConfig().get("options.blocks") == null) {
			getConfig().addDefault("options.blocks.COBBLESTONE", 1);
			getConfig().addDefault("options.blocks.STONE", 3);
			getConfig().addDefault("options.blocks.IRON_ORE", 5);
		}
		getConfig().addDefault("options.blocks_location", new ArrayList<>());
		getConfig().addDefault("options.block_regeneration_delay", 3);
		getConfig().addDefault("options.data_save_delay", 20);
		getConfig().addDefault("options.pvp_area", "");
		getConfig().addDefault("options.combat_tag_duration", 10);
		
		saveConfig();
		reloadConfig();
		
		block_regeneration_delay = getConfig().getInt("options.block_regeneration_delay");
		data_save_delay = getConfig().getInt("options.data_save_delay");
		
		reloadDataConfig();
		
		dataConfig.options().header("Developed with LOV by StivenFocs");
		dataConfig.options().copyDefaults(true);
		
		saveDataConfig();
		reloadDataConfig();
		
		if (!getConfig().getString("options.pvp_area").equals("")) {
			String[] locations_strings = getConfig().getString("options.pvp_area").split("\\|");
			
			pvpArea_pos1 = stringToLocation(locations_strings[0]);
			pvpArea_pos2 = stringToLocation(locations_strings[1]);
		}
		
		if (shop_hologram != null) shop_hologram.remove();
		if (!getConfig().getString("options.shop_hologram.location").equals("")) {
			Location loc = stringToLocation(getConfig().getString("options.shop_hologram.location"));
			Entity entity = loc.getWorld().dropItem(loc, customHologramItem(new ItemStack(Material.STONE_BUTTON)));
			entity.setVelocity(new Vector(0, 0, 0));
			if (!getConfig().getString("options.shop_hologram.name").equals("")) {
				entity.setCustomName(getConfig().getString("options.shop_hologram.name"));
				entity.setCustomNameVisible(true);
			}
			Item item_entity = (Item) entity;
			item_entity.setPickupDelay(-1);
			shop_hologram = item_entity;
		}
	}
	
	public File dataFile = new File(getDataFolder() + "/data.yml");
	public FileConfiguration dataConfig;
	
	public void saveDataConfig() {
		try {
			if (!dataFile.exists()) dataFile.createNewFile();
			if (dataConfig == null) throw new NullPointerException("dataConfig instance is null!");
			else dataConfig.save(dataFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void reloadDataConfig() {
		try {
			if (!dataFile.exists()) dataFile.createNewFile();
			dataConfig = YamlConfiguration.loadConfiguration(dataFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/////////////////////////////////////////
	
	public ItemStack getMainPickaxe() {
		ItemStack main_pickaxe = new ItemStack(Material.valueOf(getConfig().getString("options.main_pickaxe_material")));
		ItemMeta main_pickaxe_meta = main_pickaxe.getItemMeta();
		main_pickaxe_meta.spigot().setUnbreakable(true);
		main_pickaxe.setItemMeta(main_pickaxe_meta);
		
		return main_pickaxe;
	}
	
	/////////////////////////////////////////
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("spawn")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				p.teleport(stringToLocation(getConfig().getString("options.spawn")));
			} else sender.sendMessage("§cOnly players can do this.");
		}
		return false;
	}
	
	public Location stringToLocation(String location_string) {
		
		String[] loc = location_string.split(",");
		if (loc.length > 4) {
			return new Location(Bukkit.getWorld(loc[0]),Double.parseDouble(loc[1]),Double.parseDouble(loc[2]),Double.parseDouble(loc[3]),Float.parseFloat(loc[4]),Float.parseFloat(loc[5]));
		} else {
			return new Location(Bukkit.getWorld(loc[0]),Double.parseDouble(loc[1]),Double.parseDouble(loc[2]),Double.parseDouble(loc[3]));
		}
	}
	
	public void sendActionbarMessage(Player p, String message) {
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
	
	public boolean isInPvPArea(Location location) {
		if (location.getWorld().getName().equals(pvpArea_pos1.getWorld().getName())) {
			if (location.getBlockX() >= Math.min(pvpArea_pos1.getBlockX(), pvpArea_pos2.getBlockX()) && location.getBlockX() <= Math.max(pvpArea_pos1.getBlockX(), pvpArea_pos2.getBlockX())) {
				if (location.getBlockY() >= Math.min(pvpArea_pos1.getBlockY(), pvpArea_pos2.getBlockY()) && location.getBlockY() <= Math.max(pvpArea_pos1.getBlockY(), pvpArea_pos2.getBlockY())) {
					if (location.getBlockZ() >= Math.min(pvpArea_pos1.getBlockZ(), pvpArea_pos2.getBlockZ()) && location.getBlockZ() <= Math.max(pvpArea_pos1.getBlockZ(), pvpArea_pos2.getBlockZ())) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public List<Location> getNearbyBlocks(Location location, Integer range) {
		List<Location> blocks_locations = new ArrayList<>();
		
		for (int x = location.getBlockX() + range; x >= (location.getBlockX() - range); x--) {
			for (int y = location.getBlockY() + range; y >= (location.getBlockY() - range); y--) {
				for (int z = location.getBlockZ() + range; z >= (location.getBlockZ() - range); z--) {
					Location block_loc = new Location(location.getWorld(), x, y, z);
					blocks_locations.add(block_loc);
				}
			}
		}
		
		return blocks_locations;
	}		
	
	/////////////////////////////////////////
	
	public static EntityPlayer createPlayer(Player player, Location location) {
        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer(); // get server
        WorldServer world = ((CraftWorld) player.getWorld()).getHandle(); // get the world the player who died was standing in.
        GameProfile botGameProfile = new GameProfile(UUID.randomUUID(), "You"); // create a gameProfile for the bot
        Collection <Property> skinProperty = ((CraftPlayer) player).getProfile().getProperties().get("textures"); // get his textures
        botGameProfile.getProperties().putAll("textures", skinProperty); // put the textures on the bot
        PlayerInteractManager interactManager = new PlayerInteractManager(world);
        EntityPlayer botPlayer = new EntityPlayer(minecraftServer, world, botGameProfile, interactManager); // initialize bot

        Location playerLocation = location;
        botPlayer.setLocation(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
        return botPlayer;
    }
	
	public HashMap<UUID, EntityPlayer> stats_npc = new HashMap<>();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		
		if (!p.getInventory().contains(getMainPickaxe().getType()) && !p.getEnderChest().contains(getMainPickaxe().getType())) {
			p.getInventory().addItem(getMainPickaxe());
		}
		
		event.getPlayer().teleport(stringToLocation(getConfig().getString("options.spawn")));
		
		PlayerConnection conn = ((CraftPlayer) p).getHandle().playerConnection;
		EntityPlayer botPlayer = createPlayer(p, stringToLocation(getConfig().getString("options.player_npc.location")));
		conn.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, botPlayer));
		conn.sendPacket(new PacketPlayOutNamedEntitySpawn(botPlayer));
		//conn.sendPacket(new PacketPlayOutEntity.PacketPlayOutRelEntityMove(botPlayer.getId(), (byte) 0, (byte) 0, (byte) 0, true));
		stats_npc.put(p.getUniqueId(), botPlayer);
		System.out.println(botPlayer.getId());
	}
	
	HashMap<UUID, List<Location>> fake_changed_blocks = new HashMap<>();
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		
		if (!p.getInventory().contains(getMainPickaxe().getType()) && !p.getEnderChest().contains(getMainPickaxe().getType())) {
			p.getInventory().addItem(getMainPickaxe());
		}
		
		if (p.getLocation().getBlockY() <= getConfig().getInt("options.spawn_teleport_offset")) {
			p.teleport(stringToLocation(getConfig().getString("options.spawn")));
		}
		
		if (combat_tag.containsKey(p.getUniqueId())) {
			//if (!isInPvPArea(p.getLocation())) event.setCancelled(true); <-- TO REDEFINE
			
			if (!fake_changed_blocks.containsKey(p.getUniqueId())) fake_changed_blocks.put(p.getUniqueId(), new ArrayList<>());
			
			List<Location> nearby_blocks = getNearbyBlocks(p.getLocation(), 2);
			for (Location fake_block_location : new ArrayList<>(fake_changed_blocks.get(p.getUniqueId()))) {
				if (!nearby_blocks.contains(fake_block_location)) {
					Block block = fake_block_location.getWorld().getBlockAt(fake_block_location);
					p.sendBlockChange(block.getLocation(), block.getType(), block.getData());
					fake_changed_blocks.get(p.getUniqueId()).remove(fake_block_location);
				}
			}
			
			int range = 2;
			for (int x = p.getLocation().getBlockX() + range; x >= (p.getLocation().getBlockX() - range); x--) {
				for (int y = p.getLocation().getBlockY() + range; y >= (p.getLocation().getBlockY() - range); y--) {
					for (int z = p.getLocation().getBlockZ() + range; z >= (p.getLocation().getBlockZ() - range); z--) {
						if (y >= p.getLocation().getBlockY()) {
							Location block_loc = new Location(p.getLocation().getWorld(), x, y, z);
							if (!isInPvPArea(block_loc)) {
								p.sendBlockChange(new Location(p.getLocation().getWorld(), x, y, z), Material.STAINED_GLASS, (byte) 14);
								fake_changed_blocks.get(p.getUniqueId()).add(block_loc);
							}
						}
					}
				}
			}
		} else {
			if (fake_changed_blocks.containsKey(p.getUniqueId())) {
				for (Location fake_block_location : new ArrayList<>(fake_changed_blocks.get(p.getUniqueId()))) {
					Block block = fake_block_location.getWorld().getBlockAt(fake_block_location);
					p.sendBlockChange(block.getLocation(), block.getType(), block.getData());
					fake_changed_blocks.get(p.getUniqueId()).remove(fake_block_location);
				}
				if (fake_changed_blocks.get(p.getUniqueId()).size() < 1) fake_changed_blocks.remove(p.getUniqueId());
			}
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		Player p = event.getPlayer();
		
		if (!p.isOp()) {
			if (getConfig().getStringList("options.allowed_tools").contains(event.getItemDrop().getItemStack().getType().toString())) event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player p = event.getPlayer();
		
		if (!p.isOp()) event.setCancelled(true);
		if (getConfig().getStringList("options.allowed_tools").contains(p.getInventory().getItemInHand().getType().toString())) {
			if (getConfig().getInt("options.blocks." + event.getBlock().getType()) > 0) {
				int amount = getConfig().getInt("options.blocks." + event.getBlock().getType());
				if (p.getInventory().getItemInHand() != null && p.getInventory().getItemInHand().getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) > 0) amount = amount * (p.getInventory().getItemInHand().getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) + 1);
				p.giveExp(amount);
				PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + "+" + amount + " Exp" + "\"}"), (byte) 2);
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
				
				event.getBlock().setType(Material.STONE);
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		
		if (!p.isOp()) event.setCancelled(true);
	}
	
	//public HashMap<UUID, Inventory> players_brewings = new HashMap<>();
	
	@EventHandler
	public void onEntityInteract(PlayerInteractAtEntityEvent event) {
		System.out.println(event.getRightClicked().getEntityId());
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		
		if (!isInPvPArea(p.getLocation())) {
			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
				if (p.isSneaking()) {
					if (getConfig().getStringList("options.allowed_tools").contains(p.getInventory().getItemInHand().getType().toString())) {
						event.setCancelled(true);
						p.openInventory(upgradesMenu);
						return;
					} else if (getConfig().getStringList("options.allowed_armors").contains(p.getInventory().getItemInHand().getType().toString())) {
						event.setCancelled(true);
						if (PlayerData.getPlayerData(p.getUniqueId()).getLevel() >= 1) p.openInventory(armorUpgradesMenu);
						else p.sendMessage("§cUpgrade to player level §f1 §cto access this menu.");
						return;
					} else if (getConfig().getStringList("options.allowed_swords").contains(p.getInventory().getItemInHand().getType().toString())) {
						event.setCancelled(true);
						if (PlayerData.getPlayerData(p.getUniqueId()).getLevel() >= 1) p.openInventory(swordUpgradesMenu);
						else p.sendMessage("§cUpgrade to player level §f1 §cto access this menu.");
						return;
					} else if (p.getInventory().getItemInHand().getType().equals(Material.BOW)) {
						event.setCancelled(true);
						if (PlayerData.getPlayerData(p.getUniqueId()).getLevel() >= 2) p.openInventory(bowUpgradesMenu);
						else p.sendMessage("§cUpgrade to player level §f2 §cto access this menu.");
						return;
					}
				}
			}
			
			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if (getConfig().getInt("options.blocks." + event.getClickedBlock().getType()) > 0) {
					String personal_amount = "";
					if (p.getInventory().getItemInHand() != null && !p.getInventory().getItemInHand().getType().equals(Material.AIR) && p.getInventory().getItemInHand().getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) > 0) 
						personal_amount = " §f(§b" + (getConfig().getInt("options.blocks." + event.getClickedBlock().getType()) * (p.getInventory().getItemInHand().getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) + 1)) + "§f)";
					sendActionbarMessage(p, event.getClickedBlock().getType().toString().toLowerCase() + ": " + getConfig().getInt("options.blocks." + event.getClickedBlock().getType()) + " Exp" + personal_amount);
				}
				if (event.getClickedBlock().getType().equals(Material.ENDER_PORTAL_FRAME)) {
					if (PlayerData.getPlayerData(p.getUniqueId()).getLevel() >= 1) p.openInventory(shopMenu);
					else p.sendMessage("§cUpgrade to level §f1 §cto access this menu.");
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
		
		Player p = (Player) event.getWhoClicked();
		
		if (event.getInventory().equals(upgradesMenu)) {
			event.setCancelled(true);
			
			if (event.getCurrentItem() != null) {
				if (p.getInventory().getItemInHand() != null) {
					if (event.getCurrentItem().isSimilar(efficiencyUpgradeButton)) {
						if (p.getLevel() >= getConfig().getInt("options.upgrade.efficiency.cost")) {
							p.setLevel(p.getLevel() - getConfig().getInt("options.upgrade.efficiency.cost"));
							ItemMeta handItemMeta = p.getItemInHand().getItemMeta();
							handItemMeta.addEnchant(Enchantment.DIG_SPEED, handItemMeta.getEnchantLevel(Enchantment.DIG_SPEED) + 1, true);
							p.getItemInHand().setItemMeta(handItemMeta);
							
							p.sendMessage("§eEfficiency enchantment upgraded to level §f" + handItemMeta.getEnchantLevel(Enchantment.DIG_SPEED));
							p.closeInventory();
						} else {
							p.sendMessage("§cNot enough levels!");
							p.closeInventory();
						}
					} else if (event.getCurrentItem().isSimilar(fortuneUpgradeButton)) {
						if (p.getLevel() >= getConfig().getInt("options.upgrade.fortune.cost")) {
							if (event.getClick().equals(ClickType.SHIFT_LEFT)) {
								while (p.getLevel() >= getConfig().getInt("options.upgrade.fortune.cost")) {
									p.setLevel(p.getLevel() - getConfig().getInt("options.upgrade.fortune.cost"));
									ItemMeta handItemMeta = p.getItemInHand().getItemMeta();
									handItemMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, handItemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) + 1, true);
									p.getItemInHand().setItemMeta(handItemMeta);
								}
								p.sendMessage("§eFortune enchantment upgraded to level §f" + p.getItemInHand().getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS));
								p.closeInventory();
							} else {
								p.setLevel(p.getLevel() - getConfig().getInt("options.upgrade.fortune.cost"));
								ItemMeta handItemMeta = p.getItemInHand().getItemMeta();
								handItemMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, handItemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) + 1, true);
								p.getItemInHand().setItemMeta(handItemMeta);
								
								p.sendMessage("§eFortune enchantment upgraded to level §f" + handItemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS));
								p.closeInventory();
							}
						} else {
							p.sendMessage("§cNot enough levels!");
							p.closeInventory();
						}
					} else if (event.getCurrentItem().isSimilar(playerLevelUpgradeButton)) {
						if (p.getLevel() >= getConfig().getInt("options.upgrade.player_level.cost")) {
							p.setLevel(p.getLevel() - getConfig().getInt("options.upgrade.player_level.cost"));
							
							PlayerData pData = PlayerData.getPlayerData(p.getUniqueId());
							pData.setLevel(pData.getLevel() + 1);
							
							p.sendMessage("§6Player Level upgraded to level §f" + pData.getLevel());
							p.closeInventory();
							
							Bukkit.getPluginManager().callEvent(new PlayerUpgradeLevelEvent(p, pData.getLevel()));
						} else {
							p.sendMessage("§cNot enough levels!");
							p.closeInventory();
						}
					}
				} else p.sendMessage("§cerror #1");
			}
		} else if (event.getInventory().equals(shopMenu)) {
			event.setCancelled(true);
			
			if (event.getCurrentItem() != null) {
				boolean isFull = true;
				for (ItemStack item : p.getInventory().getContents()) {
					if (item == null || item.getType().equals(Material.AIR)) isFull = false;
				}
				
				if (!isFull) {
					if (event.getCurrentItem().getType().equals(Material.GOLDEN_APPLE)) {
						if (p.getLevel() >= 10) {
							p.setLevel(p.getLevel() - 10);
							p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
						} else p.sendMessage("§cYou need 10 levels to buy this.");
					} else if (event.getCurrentItem().getType().equals(Material.STICK)) {
						if (p.getLevel() >= 1) {
							p.setLevel(p.getLevel() - 1);
							p.getInventory().addItem(new ItemStack(Material.STICK));
						} else p.sendMessage("§cYou need 1 levels to buy this.");
					} else if (event.getCurrentItem().getType().equals(Material.IRON_INGOT)) {
						if (p.getLevel() >= 2) {
							p.setLevel(p.getLevel() - 2);
							p.getInventory().addItem(new ItemStack(Material.IRON_INGOT));
						} else p.sendMessage("§cYou need 2 levels to buy this.");
					} else if (event.getCurrentItem().getType().equals(Material.DIAMOND)) {
						if (p.getLevel() >= 5) {
							p.setLevel(p.getLevel() - 5);
							p.getInventory().addItem(new ItemStack(Material.DIAMOND));
						} else p.sendMessage("§cYou need 5 levels to buy this.");
					} else if (event.getCurrentItem().getType().equals(Material.STRING)) {
						if (p.getLevel() >= 1) {
							p.setLevel(p.getLevel() - 1);
							p.getInventory().addItem(new ItemStack(Material.STRING));
						} else p.sendMessage("§cYou need 1 levels to buy this.");
					} else if (event.getCurrentItem().getType().equals(Material.ARROW)) {
						if (p.getLevel() >= 1) {
							p.setLevel(p.getLevel() - 1);
							p.getInventory().addItem(new ItemStack(Material.ARROW, 5));
						} else p.sendMessage("§cYou need 1 levels to buy this.");
					} else if (event.getCurrentItem().getType().equals(Material.MILK_BUCKET)) {
						if (p.getLevel() >= 2) {
							p.setLevel(p.getLevel() - 2);
							p.getInventory().addItem(new ItemStack(Material.MILK_BUCKET));
						} else p.sendMessage("§cYou need 2 levels to buy this.");
					} else if (event.getCurrentItem().getType().equals(Material.POTION)) {
						if (PlayerData.getPlayerData(p.getUniqueId()).getLevel() >= 3) {
							if (p.getLevel() >= 5) {
								p.setLevel(p.getLevel() - 5);
								p.getInventory().addItem(event.getCurrentItem().clone());
							} else p.sendMessage("§cYou need 5 levels to buy this.");
						} else p.sendMessage("§cUpgrade to player level §f3 §cto purchase this type of item.");
					}
				} else {
					p.sendMessage("§cYour inventory is full!");
					p.closeInventory();
				}
			}
		} else if (event.getInventory().equals(armorUpgradesMenu)) {
			event.setCancelled(true);
			
			if (event.getCurrentItem() != null) {
				if (p.getInventory().getItemInHand() != null) {
					if (event.getCurrentItem().isSimilar(protectionUpgradeButton)) {
						if (p.getLevel() >= getConfig().getInt("options.upgrade.protection.cost")) {
							p.setLevel(p.getLevel() - getConfig().getInt("options.upgrade.protection.cost"));
							ItemMeta handItemMeta = p.getItemInHand().getItemMeta();
							handItemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, handItemMeta.getEnchantLevel(Enchantment.PROTECTION_ENVIRONMENTAL) + 1, true);
							p.getItemInHand().setItemMeta(handItemMeta);
							
							p.sendMessage("§eProtection enchantment upgraded to level §f" + handItemMeta.getEnchantLevel(Enchantment.PROTECTION_ENVIRONMENTAL));
							p.closeInventory();
						} else {
							p.sendMessage("§cNot enough levels!");
							p.closeInventory();
						}
					}
				} else p.sendMessage("§cerror #1");
			}
		} else if (event.getInventory().equals(swordUpgradesMenu)) {
			event.setCancelled(true);
			
			if (event.getCurrentItem() != null) {
				if (p.getInventory().getItemInHand() != null) {
					if (event.getCurrentItem().isSimilar(sharpnessUpgradeButton)) {
						if (p.getLevel() >= getConfig().getInt("options.upgrade.sharpness.cost")) {
							p.setLevel(p.getLevel() - getConfig().getInt("options.upgrade.sharpness.cost"));
							ItemMeta handItemMeta = p.getItemInHand().getItemMeta();
							handItemMeta.addEnchant(Enchantment.DAMAGE_ALL, handItemMeta.getEnchantLevel(Enchantment.DAMAGE_ALL) + 1, true);
							p.getItemInHand().setItemMeta(handItemMeta);
							
							p.sendMessage("§eSharpness enchantment upgraded to level §f" + handItemMeta.getEnchantLevel(Enchantment.DAMAGE_ALL));
							p.closeInventory();
						} else {
							p.sendMessage("§cNot enough levels!");
							p.closeInventory();
						}
					}
				} else p.sendMessage("§cerror #1");
			}
		} else if (event.getInventory().equals(bowUpgradesMenu)) {
			event.setCancelled(true);
			
			if (event.getCurrentItem() != null) {
				if (p.getInventory().getItemInHand() != null) {
					if (event.getCurrentItem().isSimilar(powerUpgradeButton)) {
						if (p.getLevel() >= getConfig().getInt("options.upgrade.power.cost")) {
							p.setLevel(p.getLevel() - getConfig().getInt("options.upgrade.power.cost"));
							ItemMeta handItemMeta = p.getItemInHand().getItemMeta();
							handItemMeta.addEnchant(Enchantment.ARROW_DAMAGE, handItemMeta.getEnchantLevel(Enchantment.ARROW_DAMAGE) + 1, true);
							p.getItemInHand().setItemMeta(handItemMeta);
							
							p.sendMessage("§ePower enchantment upgraded to level §f" + handItemMeta.getEnchantLevel(Enchantment.ARROW_DAMAGE));
							p.closeInventory();
						} else {
							p.sendMessage("§cNot enough levels!");
							p.closeInventory();
						}
					}
				} else p.sendMessage("§cerror #1");
			}
		} else {
			if (event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR)) {
				if (Enchantment.DURABILITY.canEnchantItem(event.getCurrentItem())) {
					ItemMeta meta = event.getCurrentItem().getItemMeta();
					meta.spigot().setUnbreakable(true);
					event.getCurrentItem().setItemMeta(meta);
				}
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			
			if (!event.getCause().equals(DamageCause.ENTITY_ATTACK) && !event.getCause().equals(DamageCause.CUSTOM) && !event.getCause().equals(DamageCause.PROJECTILE)) {
				event.setCancelled(true);
				p.setFireTicks(0);
			}
		}
	}
	
	public static HashMap<UUID, Integer> combat_tag = new HashMap<>();
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			
			if (pvpArea_pos1 != null && pvpArea_pos2 != null) {
				Location location = p.getLocation();
				
				if (location.getWorld().getName().equals(pvpArea_pos1.getWorld().getName())) {
					if (location.getBlockX() >= Math.min(pvpArea_pos1.getBlockX(), pvpArea_pos2.getBlockX()) && location.getBlockX() <= Math.max(pvpArea_pos1.getBlockX(), pvpArea_pos2.getBlockX())) {
						if (location.getBlockY() >= Math.min(pvpArea_pos1.getBlockY(), pvpArea_pos2.getBlockY()) && location.getBlockY() <= Math.max(pvpArea_pos1.getBlockY(), pvpArea_pos2.getBlockY())) {
							if (location.getBlockZ() >= Math.min(pvpArea_pos1.getBlockZ(), pvpArea_pos2.getBlockZ()) && location.getBlockZ() <= Math.max(pvpArea_pos1.getBlockZ(), pvpArea_pos2.getBlockZ())) {
								
								if (event.getDamager() instanceof Player) {
									Player attacker = (Player) event.getDamager();
									
									combat_tag.put(p.getUniqueId(), getConfig().getInt("options.combat_tag_duration"));
									combat_tag.put(attacker.getUniqueId(), getConfig().getInt("options.combat_tag_duration"));
								}
								
								return;
							}
						}
					}
				}
			}
			
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = (Player) event.getEntity();
		
		event.setDroppedExp(0);
		
		combat_tag.remove(p.getUniqueId());
		if (p.getKiller() != null) {
			Player killer = p.getKiller();
			
			combat_tag.remove(killer.getUniqueId());
			event.setNewLevel(p.getLevel() / 2);
			killer.setLevel(killer.getLevel() + (p.getLevel() / 2));
		}
		
		for (ItemStack drop_item : new ArrayList<>(event.getDrops())) {
			if (getConfig().getStringList("options.allowed_tools").contains(drop_item.getType().toString())) {
				event.getDrops().remove(drop_item);
				p.getInventory().addItem(drop_item);
			}
		}
	}
	
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		if (event.toWeatherState()) event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerLevelup(PlayerUpgradeLevelEvent event) {
		if (event.getLevel() == 1) event.getPlayer().sendMessage("§aThe Items shop is now unlocked!");
		if (event.getLevel() == 2) event.getPlayer().sendMessage("§aThe Bow Upgrades Menu is now unlocked!");
		if (event.getLevel() == 3) event.getPlayer().sendMessage("§aYou can now purchase potions on the shop!");
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		if (event.getItem().equals(shop_hologram)) event.setCancelled(true);
	}
	
	@EventHandler
	public void onItemDespawns(ItemDespawnEvent event) {
		if (event.getEntity().equals(shop_hologram)) event.setCancelled(true);
	}
	
}

class PlayerData {

	public static HashMap<UUID, PlayerData> players_data = new HashMap<>();
	
	public static PlayerData getPlayerData(UUID playerUID) {
		if (!players_data.containsKey(playerUID)) players_data.put(playerUID, new PlayerData(playerUID));
		return players_data.get(playerUID);
	}
	
	///////////////////////////////////
	
	final UUID playerUID;
	
	public PlayerData(UUID playerUID) {
		this.playerUID = playerUID;
		addDefaults();
	}
	
	///////////////////////////////////
	
	public void addDefaults() {
	}
	
	public int getLevel() {
		return Loader.plugin.dataConfig.getInt(playerUID + ".level");
	}
	
	public void setLevel(Integer new_level) {
		Loader.plugin.dataConfig.set(playerUID + ".level", new_level);
	}

}

class MaterialEData {
	
	public final Material material;
	public final short data;
	
	public MaterialEData(Material material, short data) {
		this.material = material;
		this.data = data;
	}
	
}
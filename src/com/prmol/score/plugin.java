package com.prmol.score;

import java.io.File;
import java.text.SimpleDateFormat;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

public class plugin extends JavaPlugin {
	ConfigCore cc ;
	Data data ;
	CommandListener cl ;
	EntityListener el;
	Rank rank ;

	@Override
	public void onEnable() {
		cc= new ConfigCore(this);
		data= new Data(this);
		cl= new CommandListener(this, data);
		el = new EntityListener(this, cc, data);
		rank= new Rank(this);
		
		this.getCommand("score").setExecutor(cl);
		this.getServer().getPluginManager().registerEvents(el, this);
		final JavaPlugin plugin = this;
		rank.runTaskTimer(this, 0L, 20 * 30L);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				File rankFile = new File("plugins/score/", "rank.yml");
				YamlConfiguration rankConfig = YamlConfiguration
						.loadConfiguration(rankFile);
				Location location = new Location(plugin.getServer().getWorld(
						rankConfig.getString("hdw")),
						rankConfig.getDouble("hdx"),
						rankConfig.getDouble("hdy"),
						rankConfig.getDouble("hdz"));
				Hologram hologram = HologramsAPI.createHologram(plugin,
						location);
				hologram.clearLines();
				hologram.appendTextLine("§e=====§a服务器玩家积分排名榜前五§e=====");
				ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
				hologram.appendItemLine(item);
				String time = new SimpleDateFormat("yyyyMM").format(System
						.currentTimeMillis());
				for (int i = 1; i < 5; i++) {
					String name = rankConfig.getString(time + "." + i);
					if (name != null) {
						hologram.appendTextLine("§a" + i + ". " + name);
					} else {
						break;
					}
				}

			}
		}.runTaskLater(this, 10 * 20L);

	}
}

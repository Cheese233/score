package com.prmol.score;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

public class CommandListener implements CommandExecutor {
	JavaPlugin plugin;
	Data data;
	String prefix = "§a[§e积分§a]§6";

	public CommandListener(JavaPlugin plugin, Data data) {
		this.plugin = plugin;
		this.data = data;
	}

	FileFilter yamlFileFilter = new FileFilter() {
		@Override
		public boolean accept(File file) {
			if (file.getName().endsWith(".yml")) {
				return true;
			}
			return false;
		}
	};

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (label.equalsIgnoreCase("score")) {
			if (args.length != 0) {
				if (args[0].equalsIgnoreCase("rank")) {
					File rankFile = new File("plugins/score/", "rank.yml");
					if (!rankFile.exists()) {
						sender.sendMessage(prefix + "排名文件不存在！请联系管理员或OP!");
						return true;
					}
					YamlConfiguration rankConfig = YamlConfiguration
							.loadConfiguration(rankFile);

					String time = new SimpleDateFormat("yyyyMM").format(System
							.currentTimeMillis());
					for (int i = 1; i < rankConfig.getInt("rank"); i++) {
						String name = rankConfig.getString(time + "." + i);
						if (name != null) {
							sender.sendMessage("§a" + i + ". " + name);
						} else {
							break;
						}
					}
					sender.sendMessage(prefix + "服务器积分排名前§c§l"
							+ rankConfig.getInt("rank") + "§6如上所列");
					return true;
				} else if (args.length == 3) {

					if (args[0].equals("reduce")) {
						if (!sender.isOp()) {
							sender.sendMessage(prefix + "你没有所需要的权限!");
							return true;
						}
						try {
							data.changeScore(args[1], Integer.valueOf(args[2]),
									false);
						} catch (NumberFormatException e) {
							sender.sendMessage(prefix + "积分必须是一个整数!");
							return true;
						}
						sender.sendMessage(prefix + "成功减去积分!");
						return true;
					}
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("me")) {
						if (!(sender instanceof Player)) {
							sender.sendMessage(prefix + "只有玩家可以查询自己的积分!");
							return true;
						}
						String playerName = ((Player) sender).getName();
						((Player) sender).sendMessage(prefix + "您的当前积分: §a"
								+ data.getPlayerScore(playerName));
						return true;
					} else if (args[0].equalsIgnoreCase("help")) {
						sender.sendMessage(prefix + "/score me  查看自己的积分");
						sender.sendMessage(prefix + "/score rank 查看服务器积分排行榜");
						sender.sendMessage(prefix
								+ "/score reduce <玩家> <积分> 为玩家减去指定积分(OP可用)");
						sender.sendMessage(prefix
								+ "/score showhere 将悬浮显示排名移动至此(OP可用)");
						sender.sendMessage(prefix + "/reload 清理旧HD)");
						sender.sendMessage(prefix
								+ "                  Score插件v1.0 §7by www.prmol.com");
					} else if (args[0].equalsIgnoreCase("clearhd")) {
						if (!sender.isOp()) {
							sender.sendMessage(prefix + "你没有所需权限!");
							return true;
						}
						plugin.getServer().dispatchCommand(
								plugin.getServer().getConsoleSender(),
								"hd reload");
						sender.sendMessage(prefix
								+ "已经刷新旧的HolographicDisplays!");
					}

					else if (args[0].equalsIgnoreCase("showhere")) {
						if (!(sender instanceof Player)) {
							sender.sendMessage(prefix + "只有玩家才能移动全息排名显示!");
							return true;
						}
						if (!sender.isOp()) {
							sender.sendMessage(prefix + "你没有所需权限!");
							return true;
						}
						Location location = ((Player) sender).getLocation();
						Hologram hologram = HologramsAPI.createHologram(plugin,
								location);
						hologram.clearLines();
						hologram.appendTextLine("§e=====§a服务器玩家积分排名榜前五§e=====");
						ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
						hologram.appendItemLine(item);

						File rankFile = new File("plugins/score/", "rank.yml");
						YamlConfiguration rankConfig = YamlConfiguration
								.loadConfiguration(rankFile);

						rankConfig.set("hdx", location.getX());
						rankConfig.set("hdy", location.getY());
						rankConfig.set("hdz", location.getZ());
						rankConfig.set("hdw", location.getWorld().getName());
						try {
							rankConfig.save(rankFile);
						} catch (IOException e) {
							e.printStackTrace();
						}
						String time = new SimpleDateFormat("yyyyMM")
								.format(System.currentTimeMillis());
						for (int i = 1; i < 5; i++) {
							String name = rankConfig.getString(time + "." + i);
							if (name != null) {
								hologram.appendTextLine("§a" + i + ". " + name);
							} else {
								break;
							}
						}
						return true;
					}
				}
			}
		}
		return false;
	}
}

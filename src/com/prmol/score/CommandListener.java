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
	String prefix = "��a[��e���֡�a]��6";

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
						sender.sendMessage(prefix + "�����ļ������ڣ�����ϵ����Ա��OP!");
						return true;
					}
					YamlConfiguration rankConfig = YamlConfiguration
							.loadConfiguration(rankFile);

					String time = new SimpleDateFormat("yyyyMM").format(System
							.currentTimeMillis());
					for (int i = 1; i < rankConfig.getInt("rank"); i++) {
						String name = rankConfig.getString(time + "." + i);
						if (name != null) {
							sender.sendMessage("��a" + i + ". " + name);
						} else {
							break;
						}
					}
					sender.sendMessage(prefix + "��������������ǰ��c��l"
							+ rankConfig.getInt("rank") + "��6��������");
					return true;
				} else if (args.length == 3) {

					if (args[0].equals("reduce")) {
						if (!sender.isOp()) {
							sender.sendMessage(prefix + "��û������Ҫ��Ȩ��!");
							return true;
						}
						try {
							data.changeScore(args[1], Integer.valueOf(args[2]),
									false);
						} catch (NumberFormatException e) {
							sender.sendMessage(prefix + "���ֱ�����һ������!");
							return true;
						}
						sender.sendMessage(prefix + "�ɹ���ȥ����!");
						return true;
					}
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("me")) {
						if (!(sender instanceof Player)) {
							sender.sendMessage(prefix + "ֻ����ҿ��Բ�ѯ�Լ��Ļ���!");
							return true;
						}
						String playerName = ((Player) sender).getName();
						((Player) sender).sendMessage(prefix + "���ĵ�ǰ����: ��a"
								+ data.getPlayerScore(playerName));
						return true;
					} else if (args[0].equalsIgnoreCase("help")) {
						sender.sendMessage(prefix + "/score me  �鿴�Լ��Ļ���");
						sender.sendMessage(prefix + "/score rank �鿴�������������а�");
						sender.sendMessage(prefix
								+ "/score reduce <���> <����> Ϊ��Ҽ�ȥָ������(OP����)");
						sender.sendMessage(prefix
								+ "/score showhere ��������ʾ�����ƶ�����(OP����)");
						sender.sendMessage(prefix + "/reload �����HD)");
						sender.sendMessage(prefix
								+ "                  Score���v1.0 ��7by www.prmol.com");
					} else if (args[0].equalsIgnoreCase("clearhd")) {
						if (!sender.isOp()) {
							sender.sendMessage(prefix + "��û������Ȩ��!");
							return true;
						}
						plugin.getServer().dispatchCommand(
								plugin.getServer().getConsoleSender(),
								"hd reload");
						sender.sendMessage(prefix
								+ "�Ѿ�ˢ�¾ɵ�HolographicDisplays!");
					}

					else if (args[0].equalsIgnoreCase("showhere")) {
						if (!(sender instanceof Player)) {
							sender.sendMessage(prefix + "ֻ����Ҳ����ƶ�ȫϢ������ʾ!");
							return true;
						}
						if (!sender.isOp()) {
							sender.sendMessage(prefix + "��û������Ȩ��!");
							return true;
						}
						Location location = ((Player) sender).getLocation();
						Hologram hologram = HologramsAPI.createHologram(plugin,
								location);
						hologram.clearLines();
						hologram.appendTextLine("��e=====��a��������һ���������ǰ���e=====");
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
								hologram.appendTextLine("��a" + i + ". " + name);
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

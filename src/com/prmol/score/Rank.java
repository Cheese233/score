package com.prmol.score;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Rank extends BukkitRunnable {
	JavaPlugin plugin;

	public Rank(JavaPlugin plugin)
	{
		this.plugin=plugin;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList<Map.Entry<String, String>> sortMap(Map map) {
		List<Map.Entry<String, String>> entries = new ArrayList<Map.Entry<String, String>>(
				map.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> obj1,
					Map.Entry<String, String> obj2) {
				return Integer.valueOf(obj2.getValue()) - Integer.valueOf(obj1.getValue());
			}
		});
		return (ArrayList<Entry<String, String>>) entries;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void run() {
		String time = new SimpleDateFormat("yyyyMM").format(System
				.currentTimeMillis());
		File rankFile = new File("plugins/score/", "rank.yml");
		if (!rankFile.getParentFile().exists()) {
			rankFile.getParentFile().mkdir();
		}
		if (!rankFile.exists()) {
			plugin.saveResource("rank.yml", false);
		}

		YamlConfiguration rankConfig = YamlConfiguration
				.loadConfiguration(rankFile);
		FileFilter yamlFileFilter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.getName().endsWith(".yml")) {
					return true;
				}
				return false;
			}
		};

		// 读取
		File playersDataFolder = new File("plugins/score/playersdata/");
		File files[] = playersDataFolder.listFiles(yamlFileFilter);
		Map playerRank = new TreeMap();
		for (File file : files) {
			YamlConfiguration config = YamlConfiguration
					.loadConfiguration(file);
			plugin.getLogger().info(file.getName());
			playerRank.put(file.getName().replace(".yml", ""),
					"" + config.getInt("score"));
			plugin.getLogger().info(
					file.getName().replace(".yml", "") + "积分"
							+ config.getInt("score"));
		}

		// <玩家名称, 玩家积分>
		/*List<Map.Entry<String, String>> list_Data = new ArrayList<Map.Entry<String, String>>(
				playerRank.entrySet());
		Collections.sort(list_Data,
				new Comparator<Map.Entry<String, String>>() {
					public int compare(Map.Entry<String, String> o1,
							Map.Entry<String, String> o2) {
						if (o2.getValue() != null && o1.getValue() != null
								&& o2.getValue().compareTo(o1.getValue()) > 0) {
							return 1;
						} else {
							return -1;
						}

					}
				});*/
		

		if (rankConfig.isConfigurationSection(time) == false) {
			rankConfig.createSection(time + "sent");
		}
		
		// 排名
		ArrayList<Map.Entry<String, String>> entries = sortMap(playerRank);
		int rank = rankConfig.getInt("rank");
		List<String> needGiftPlayers = new ArrayList<>();

		for (int i = 0; i < rank; i++) {

		
			rankConfig.set(time+"."+(i+1),entries.get(i).getKey());
			if (!rankConfig.getConfigurationSection(time).getBoolean("sent")) {
				needGiftPlayers.add(entries.get(i).getKey());
				rankConfig.set(time + ".sent", true);
			}
			if (i >= playerRank.size()-1) {
				break;
			}
//			Iterator<Entry<String, String>> ite = playerRank.entrySet()
//					.iterator();
//			while (ite.hasNext()) {
//				Entry<String, String> e = (Entry<String, String>) ite.next();
//				rankConfig.set(time + "." + (i + 1), e.getKey());
//				
//
//			}

		}
		if (!rankConfig.getConfigurationSection(time).getBoolean("sent")) {
			rankConfig.set(time + ".needGift", needGiftPlayers);
		}
		try {
			rankConfig.save(rankFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

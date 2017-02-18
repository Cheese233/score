package com.prmol.score;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Data {
	JavaPlugin plugin;
	Logger logger;
	File playersDataFolder = new File("plugins/score/playersdata");

	public Data(JavaPlugin plugin) {
		this.plugin = plugin;
		logger = plugin.getLogger();
		if (!plugin.getDataFolder().exists()) {
			logger.info("plugin data folder not exists!");
			plugin.getDataFolder().mkdir();
			logger.info("plugin data folder made!");
		}
		if (!playersDataFolder.exists()) {
			logger.info("players data folder not exists!");
			playersDataFolder.mkdir();
			logger.info("players data folder made!");
		}
		plugin.saveResource("playersdata/Amazing_coder.yml", false);
		logger.info("example player data created!");
	}

	/*
	 * -1 ��Ҳ����������ļ� -2 ��������ļ���ʽ����ȷ
	 */
	public int getPlayerScore(String playerName) {
		File playerDataFile = new File(playersDataFolder, playerName + ".yml");
		if (!playerDataFile.exists()) {
			return -1;
		}
		YamlConfiguration playerConfig = YamlConfiguration
				.loadConfiguration(playerDataFile);
		return playerConfig.getInt("score");

	}

	/*
	 * -1 �Ѿ�������������ļ� -2 �����ļ���ʱ����� 0 �ɹ�
	 */
	public int createPlayerDataFile(String playerName) {
		File playerDataFile = new File(playersDataFolder, playerName + ".yml");
		if (playerDataFile.exists()) {
			return -1;
		}
		try {
			playerDataFile.createNewFile();
		} catch (Exception e) {
			return -2;
		}
		YamlConfiguration.loadConfiguration(playerDataFile).set("score", 0);

		return 0;
	}

	/*
	 * -1 ��������� -2 �����ļ���ʽ�� 0�ɹ�
	 */
	private class d {
		private Player player;

		public d(Player player) {
			this.player = player;
		}

		public void add(String displayName) {
			player.setDisplayName("��f[" + displayName + "��f]��r"
					+ player.getDisplayName());
		}
	}

	public int changeScore(String PlayerName, int score, boolean isAdd) {
		File playerDataFile = new File(playersDataFolder, PlayerName + ".yml");
		if (!playerDataFile.exists()) {
			return -1;
		}
		YamlConfiguration playerConfig = YamlConfiguration
				.loadConfiguration(playerDataFile);
		if (isAdd) {
			playerConfig.set("score", playerConfig.getInt("score") + score);
		} else {
			playerConfig.set("score", playerConfig.getInt("score") - score);
		}
		try {
			playerConfig.save(playerDataFile);
		} catch (IOException e) {
			e.printStackTrace();
			plugin.getLogger().info(PlayerName + "��������ʱ����!");
		}
		d d=new d(Bukkit.getPlayer(PlayerName));
		d.add("��7δ֪��λ");
		if (score >= 120000) {
			d.add("��b����e�� ��6V");
		} else if (score >= 150000) {
			d.add("��b����e�� ��bIV");
		} else if (score >= 200000) {
			d.add("��b����e�� ��3III");
		} else if (score >= 300000) {
			d.add("��b����e�� ��4II");
		} else if (score >= 500000) {
			d.add("��b����e�� ��5I");
		} else if (score >= 700000) {
			d.add("��b��ʯ ��6V");
		} else if (score >= 900000) {
			d.add("��b��ʯ ��bIV");
		} else if (score >= 1200000) {
			d.add("��b��ʯ ��3III");
		} else if (score >= 1500000) {
			d.add("��b��ʯ ��4II");
		} else if (score >= 2000000) {
			d.add("��b��ʯ ��5I");
		} else if (score >= 3200000) {
			d.add("��a��l������ʦ");
		} else if (score >= 6666666) {
			d.add("��6��l��ǿ����");
		}
		return 0;
	}
}

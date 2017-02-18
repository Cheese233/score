package com.prmol.score;

import java.io.File;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigCore {
	JavaPlugin plugin;
	public ConfigCore(JavaPlugin plugin)
	{
		this.plugin=plugin;
		if(!plugin.getDataFolder().exists())
		{
			plugin.getDataFolder().mkdir();
		}
		if(!new File(plugin.getDataFolder(),"config.yml").exists())
		{
			plugin.saveDefaultConfig();
		}
	}
	public int getAddScore()
	{
		return plugin.getConfig().getInt("addScore",5);
	}
	public int getReduceScore()
	{
		return plugin.getConfig().getInt("reduceScore",3);
	}
	public int getAddScoreLow()
	{
		return plugin.getConfig().getInt("addScore_low",3);
	}
	public int getReduceScoreHigh()
	{
		return plugin.getConfig().getInt("reduceScore_high",5);
	}
	public List<String> getEnableWorlds()
	{
		return plugin.getConfig().getStringList("enableWorlds");
	}
}

package com.prmol.score;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityListener implements Listener {
	JavaPlugin plugin;
	ConfigCore cc;
	Data data;
	String prefix = "§a[§e积分§a]§6";

	public void debug(Player player, String message, String logger) {
		if (!message.equals("") && player != null) {
			player.sendMessage(message);
		}
		if (!logger.equals("")) {
			plugin.getLogger().info(logger);
		}
	}

	public EntityListener(JavaPlugin plugin, ConfigCore cc, Data data) {
		this.plugin = plugin;
		this.cc = cc;
		this.data = data;
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent evt) {
		if (evt.getDamager() instanceof Player) {
			File rankFile = new File("plugins/score/", "rank.yml");
			if (!rankFile.getParentFile().exists()) {
				rankFile.getParentFile().mkdir();
			}
			if (!rankFile.exists()) {
				plugin.saveResource("rank.yml", false);
			}

			YamlConfiguration rankConfig = YamlConfiguration
					.loadConfiguration(rankFile);
			String time = new SimpleDateFormat("yyyyMM").format(System
					.currentTimeMillis());
			boolean enoughtRank = false;
			for (int i = 1; i < rankConfig.getInt("rank"); i++) {
				if (rankConfig.getString(time + "." + i).equals(
						((Player) evt.getDamager()).getName())) {
					enoughtRank = true;
					break;
				}
			}
			if (enoughtRank) {
				evt.getEntity()
						.getLocation()
						.getWorld()
						.playEffect(evt.getEntity().getLocation(),
								Effect.STEP_SOUND, 57);
			}
		}
	}

	private class d {
		private Player player;

		public d(Player player) {
			this.player = player;
		}

		public void add(String displayName) {
			player.setDisplayName("§f[" + displayName + "§f]§r"
					+ player.getDisplayName());
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent evt) {
		Player player = evt.getPlayer();
		if (data.createPlayerDataFile(player.getName()) == 0) {
			debug(null, "",
					"已经创建玩家数据: plugins/score/playersdata/" + player.getName()
							+ ".yml √");
		}
		File rankFile = new File("plugins/score/", "rank.yml");
		YamlConfiguration rank = YamlConfiguration.loadConfiguration(rankFile);
		int score = data.getPlayerScore(player.getName());
		player.sendMessage("§e您的积分: " + score);
		d d = new d(player);
		d.add("§未知段位");
		if (score >= 120000) {
			d.add("§b铂§e金 §6V");
		} else if (score >= 150000) {
			d.add("§b铂§e金 §bIV");
		} else if (score >= 200000) {
			d.add("§b铂§e金 §3III");
		} else if (score >= 300000) {
			d.add("§b铂§e金 §4II");
		} else if (score >= 500000) {
			d.add("§b铂§e金 §5I");
		} else if (score >= 700000) {
			d.add("§b钻石 §6V");
		} else if (score >= 900000) {
			d.add("§b钻石 §bIV");
		} else if (score >= 1200000) {
			d.add("§b钻石 §3III");
		} else if (score >= 1500000) {
			d.add("§b钻石 §4II");
		} else if (score >= 2000000) {
			d.add("§b钻石 §5I");
		} else if (score >= 3200000) {
			d.add("§a§l超凡大师");
		} else if (score >= 6666666) {
			d.add("§6§l最强王者");
		}
		/*
		 * if (score >= 0 && score <= 1199) { player.setDisplayName("§6[青铜]" +
		 * player.getDisplayName()); // 青铜 } else if (score >= 1200 && score <=
		 * 1499) { player.setDisplayName("§f[白银]" + player.getDisplayName()); //
		 * 白银 } else if (score >= 1450 && score <= 1699) {
		 * player.setDisplayName("§e[黄金]" + player.getDisplayName()); // 黄金 }
		 * else if (score >= 1700 && score <= 1999) {
		 * player.setDisplayName("§a[铂金]" + player.getDisplayName()); // 铂金 }
		 * else if (score >= 2000) { player.setDisplayName("§b[钻石]" +
		 * player.getDisplayName()); // 钻石 }
		 */
		List<ItemStack> gifts = new ArrayList<>();
		for (String gift : rank.getStringList("gifts")) {
			String[] attr = gift.split(" ");

			int id = Integer.valueOf(attr[0]);
			String name = attr[1].replace("%", " ");
			List<String> lores = new ArrayList<>();
			Collections.addAll(lores, attr[2].split("~"));
			for (String lore : lores) {
				lore.replace("%", " ");
			}
			@SuppressWarnings("deprecation")
			ItemStack item = new ItemStack(id);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			meta.setLore(lores);
			for (String en : attr[3].split(",")) {
				String[] enAttrs = en.split(":");
				meta.addEnchant(Enchantment.getByName(enAttrs[0]),
						Integer.valueOf(enAttrs[1]), false);

			}
			item.setItemMeta(meta);
			gifts.add(item);
		}
		int freeSlotsCount = 0;
		for (ItemStack item : player.getInventory().getContents()) {
			if (item == null) {
				freeSlotsCount++;
			}
		}
		String time = new SimpleDateFormat("yyyyMM").format(System
				.currentTimeMillis());
		List<String> needGiftPlayers = rank.getConfigurationSection(time)
				.getStringList("needGift");
		boolean isNeed = false;
		for (String needGiftPlayer : needGiftPlayers) {
			if (player.getName().equals(needGiftPlayer)) {
				isNeed = true;
				break;
			}
		}
		if (!isNeed) {
			player.sendMessage(prefix + "您未收到奖励");
			return;
		}
		if (freeSlotsCount < gifts.size()) {
			player.sendMessage(prefix + "您的背包空位少于 " + freeSlotsCount
					+ " 个,请清理背包空位后重新登录游戏领取当前月份的积分前十礼包!");
			return;
		}

		needGiftPlayers.remove(player.getName());
		rank.set(time + ".needGift", needGiftPlayers);
		try {
			rank.save(rankFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (ItemStack gift : gifts) {
			player.getInventory().addItem(gift);
			player.sendMessage(prefix + gift.getItemMeta().getDisplayName()
					+ " §a已经放入您的背包!");
		}
		player.sendMessage(prefix + "提示: 您当月积分排名名列前茅, 系统已经自动发放奖励到您的背包中!");

	}

//	ItemStack itemBanned = null; // 需要禁止的物品
//	int slot = 0; // 副手位置的slot ，自己去测试
//
//	@EventHandler
//	public void onInventyClick(InventoryClickEvent evt) {
//		if (!(evt.getWhoClicked() instanceof Player)) {
//			if (evt.getInventory().getType() == InventoryType.PLAYER) {
//				if (evt.getClick() == ClickType.LEFT) {
//					if (evt.getRawSlot() == slot) {
//						if (evt.getClickedInventory().getItem(evt.getRawSlot())
//								.isSimilar(itemBanned)) {
//							evt.setCancelled(true);
//							((Player) evt.getWhoClicked()).sendMessage("mmp");
//						}
//					}
//				}
//			}
//		}
//	}
//
//	@EventHandler
//	public void onPlayerSwapItem(PlayerSwapHandItemsEvent evt) {
//		if (evt.getOffHandItem().isSimilar(itemBanned)) {
//			evt.setCancelled(true);
//			evt.getPlayer().sendMessage("mmp");
//		}
//	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent evt) {
		List<String> enableWorlds = cc.getEnableWorlds();
		boolean isEnableWorld = false;
		for (String enableWorldName : enableWorlds) {
			if (evt.getEntity().getLocation().getWorld().getName()
					.equals(enableWorldName)) {
				isEnableWorld = true;
			}
		}
		if (evt.getEntity() instanceof Player
				&& evt.getEntity().getKiller() instanceof Player
				&& isEnableWorld) {
			Player victim = ((Player) evt.getEntity());
			Player killer = ((Player) evt.getEntity().getKiller());
			int vicitimScore = data.getPlayerScore(victim.getName());
			int killerScore = data.getPlayerScore(killer.getName());
			int vicitimLevel = duanwei.getLevel(duanwei
					.getduanwei(vicitimScore));
			int killerLevel = duanwei.getLevel(duanwei.getduanwei(killerScore));
			if (killerLevel < vicitimLevel) {
				data.changeScore(killer.getName(), cc.getAddScoreLow(), true);
				killer.sendMessage(prefix + "§e你杀死了" + victim.getName()
						+ ", 获得了" + cc.getAddScoreLow() + "积分");
				data.changeScore(victim.getName(), cc.getReduceScoreHigh(),
						false);
				victim.sendMessage(prefix + "§你被" + killer.getName()
						+ "杀死了, 丢失了" + cc.getReduceScoreHigh() + "积分");

			} else {
				// victimLevel>=killerLevel
				data.changeScore(killer.getName(), cc.getAddScore(), true);
				killer.sendMessage(prefix + "§e你杀死了" + victim.getName()
						+ ", 获得了" + cc.getAddScore() + "积分");
				data.changeScore(victim.getName(), cc.getReduceScore(), false);
				victim.sendMessage(prefix + "§你被" + killer.getName()
						+ "杀死了, 丢失了" + cc.getReduceScore() + "积分");

			}
			/*
			 * int victimScore = data.getPlayerScore(victim.getName()); int
			 * killerScore = data.getPlayerScore(killer.getName()); if
			 * (killerScore == -1) { killer.sendMessage(prefix +
			 * "你的积分数据文件不存在或数据格式损坏! 请尝试重新进入服务器或联系管理员!"); return; } if
			 * (victimScore == -1) { victim.sendMessage(prefix +
			 * "你的积分数据文件不存在或数据格式损坏! 请尝试重新进入服务器或联系管理员!"); return; }
			 * 
			 * if (killerScore <= victimScore) {
			 * data.changeScore(killer.getName(), cc.getAddScore(), true);
			 * killer.sendMessage(prefix + "§a你杀死了玩家 §e" + victim.getName() +
			 * " §a,获得了§e" + cc.getAddScore() + " §a积分,当前积分: §e" + (killerScore
			 * + cc.getAddScore())); } else { if
			 * (data.getPlayerScore(killer.getName()) <= cc .getReduceScore()) {
			 * data.changeScore(killer.getName(),
			 * data.getPlayerScore(killer.getName()), false); } else {
			 * data.changeScore(killer.getName(), cc.getReduceScore(), false); }
			 * killer.sendMessage(prefix + "§a你杀死了比你低分的玩家 §e" + victim.getName()
			 * + "§a ,你受到惩罚丢掉了 §e" + cc.getReduceScore() + " §a积分");
			 * 
			 * }
			 */
		}
	}
}

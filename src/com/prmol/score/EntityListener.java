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
	String prefix = "��a[��e���֡�a]��6";

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
			player.setDisplayName("��f[" + displayName + "��f]��r"
					+ player.getDisplayName());
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent evt) {
		Player player = evt.getPlayer();
		if (data.createPlayerDataFile(player.getName()) == 0) {
			debug(null, "",
					"�Ѿ������������: plugins/score/playersdata/" + player.getName()
							+ ".yml ��");
		}
		File rankFile = new File("plugins/score/", "rank.yml");
		YamlConfiguration rank = YamlConfiguration.loadConfiguration(rankFile);
		int score = data.getPlayerScore(player.getName());
		player.sendMessage("��e���Ļ���: " + score);
		d d = new d(player);
		d.add("��δ֪��λ");
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
		/*
		 * if (score >= 0 && score <= 1199) { player.setDisplayName("��6[��ͭ]" +
		 * player.getDisplayName()); // ��ͭ } else if (score >= 1200 && score <=
		 * 1499) { player.setDisplayName("��f[����]" + player.getDisplayName()); //
		 * ���� } else if (score >= 1450 && score <= 1699) {
		 * player.setDisplayName("��e[�ƽ�]" + player.getDisplayName()); // �ƽ� }
		 * else if (score >= 1700 && score <= 1999) {
		 * player.setDisplayName("��a[����]" + player.getDisplayName()); // ���� }
		 * else if (score >= 2000) { player.setDisplayName("��b[��ʯ]" +
		 * player.getDisplayName()); // ��ʯ }
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
			player.sendMessage(prefix + "��δ�յ�����");
			return;
		}
		if (freeSlotsCount < gifts.size()) {
			player.sendMessage(prefix + "���ı�����λ���� " + freeSlotsCount
					+ " ��,����������λ�����µ�¼��Ϸ��ȡ��ǰ�·ݵĻ���ǰʮ���!");
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
					+ " ��a�Ѿ��������ı���!");
		}
		player.sendMessage(prefix + "��ʾ: �����»�����������ǰé, ϵͳ�Ѿ��Զ����Ž��������ı�����!");

	}

//	ItemStack itemBanned = null; // ��Ҫ��ֹ����Ʒ
//	int slot = 0; // ����λ�õ�slot ���Լ�ȥ����
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
				killer.sendMessage(prefix + "��e��ɱ����" + victim.getName()
						+ ", �����" + cc.getAddScoreLow() + "����");
				data.changeScore(victim.getName(), cc.getReduceScoreHigh(),
						false);
				victim.sendMessage(prefix + "���㱻" + killer.getName()
						+ "ɱ����, ��ʧ��" + cc.getReduceScoreHigh() + "����");

			} else {
				// victimLevel>=killerLevel
				data.changeScore(killer.getName(), cc.getAddScore(), true);
				killer.sendMessage(prefix + "��e��ɱ����" + victim.getName()
						+ ", �����" + cc.getAddScore() + "����");
				data.changeScore(victim.getName(), cc.getReduceScore(), false);
				victim.sendMessage(prefix + "���㱻" + killer.getName()
						+ "ɱ����, ��ʧ��" + cc.getReduceScore() + "����");

			}
			/*
			 * int victimScore = data.getPlayerScore(victim.getName()); int
			 * killerScore = data.getPlayerScore(killer.getName()); if
			 * (killerScore == -1) { killer.sendMessage(prefix +
			 * "��Ļ��������ļ������ڻ����ݸ�ʽ��! �볢�����½������������ϵ����Ա!"); return; } if
			 * (victimScore == -1) { victim.sendMessage(prefix +
			 * "��Ļ��������ļ������ڻ����ݸ�ʽ��! �볢�����½������������ϵ����Ա!"); return; }
			 * 
			 * if (killerScore <= victimScore) {
			 * data.changeScore(killer.getName(), cc.getAddScore(), true);
			 * killer.sendMessage(prefix + "��a��ɱ������� ��e" + victim.getName() +
			 * " ��a,����ˡ�e" + cc.getAddScore() + " ��a����,��ǰ����: ��e" + (killerScore
			 * + cc.getAddScore())); } else { if
			 * (data.getPlayerScore(killer.getName()) <= cc .getReduceScore()) {
			 * data.changeScore(killer.getName(),
			 * data.getPlayerScore(killer.getName()), false); } else {
			 * data.changeScore(killer.getName(), cc.getReduceScore(), false); }
			 * killer.sendMessage(prefix + "��a��ɱ���˱���ͷֵ���� ��e" + victim.getName()
			 * + "��a ,���ܵ��ͷ������� ��e" + cc.getReduceScore() + " ��a����");
			 * 
			 * }
			 */
		}
	}
}

package services.headpat.spigotextensions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
	@Contract(pure = true)
	public static @NotNull String covertColorCodes(@NotNull String str) {
		return str.replaceAll("&(?=[0-9a-fkl-mr])", "§");
	}

	/**
	 * @param chance integer percentage.
	 * @return The chance result;
	 */
	public static boolean percentChance(int chance) {
		return Math.random() < (chance / 100.0d);
	}

	public static int timeToTicks(int minutes, int seconds) {
		return ((minutes * 60) + seconds) * 20;
	}

	/**
	 * Wraps text that will displayed in a lore to the best of its ability.
	 *
	 * @param lore       The full length lore with no breaks.
	 * @param lineLength The maximum length for a single line.
	 * @return The text-wrapped lore.
	 */
	public static @NotNull List<String> wrapLore(@NotNull String lore, int lineLength, ChatColor loreChatColor) {
		String[] words = lore.split(" ");

		List<String> result = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		for (String word : words) {
			if (builder.length() == 0 || ((builder.length() + 1 + word.length()) <= lineLength)) {
				if (builder.length() > 0) {
					builder.append(' ');
				}
			} else {
				result.add(loreChatColor + "" + ChatColor.ITALIC + builder.toString());
				builder.setLength(0);
			}
			builder.append(word);
		}
		if (builder.length() != 0) {
			result.add(loreChatColor + "" + ChatColor.ITALIC + builder.toString());
		}

		return (result);
	}

	/**
	 * Wraps text that will displayed in a lore to the best of its ability.
	 *
	 * @param lore The full length lore with no breaks.
	 * @return The text-wrapped lore.
	 */
	public static @NotNull List<String> wrapLore(String lore) {
		return (Utils.wrapLore(lore, 25, ChatColor.DARK_PURPLE));
	}

	/**
	 * Wraps text that will displayed in a lore to the best of its ability.
	 *
	 * @param lore          The full length lore with no breaks.
	 * @param loreChatColor The color of the lore.
	 * @return The text-wrapped lore.
	 */
	public static @NotNull List<String> wrapLore(String lore, ChatColor loreChatColor) {
		return (Utils.wrapLore(lore, 25, loreChatColor));
	}

	/**
	 * Gets nearby players only, removing the time to find regular entities.
	 *
	 * @param location      Location of where to search.
	 * @param radius        Radius to search for players.
	 * @param sortByClosest Whether to sort Set by proximity.
	 * @return Players near the specified location.
	 */
	public static LinkedHashSet<Player> getNearbyPlayers(Location location, double radius, boolean sortByClosest) {
		Stream<? extends Player> stream = Bukkit.getOnlinePlayers().stream().filter((player) -> {
			if (location.getWorld() != player.getWorld()) {
				return (false);
			}
			return (location.distanceSquared(player.getLocation()) <= (radius * radius));
		});
		if (sortByClosest) {
			stream = stream.sorted(Comparator.comparingDouble(p -> p.getLocation().distanceSquared(location)));
		}
		return (stream.collect(Collectors.toCollection(LinkedHashSet::new)));
	}
}

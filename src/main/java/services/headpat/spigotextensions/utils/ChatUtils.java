package services.headpat.spigotextensions.utils;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatUtils {
	/**
	 * @param str The original string using `&` color codes.
	 * @return The string with all color codes using `&` convert to `§`.
	 */
	@Contract(pure = true)
	public static @NotNull String covertColorCodes(@NotNull String str) {
		return str.replaceAll("&(?=[0-9a-fkl-mr])", "§");
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
		return (ChatUtils.wrapLore(lore, 25, ChatColor.DARK_PURPLE));
	}

	/**
	 * Wraps text that will displayed in a lore to the best of its ability.
	 *
	 * @param lore          The full length lore with no breaks.
	 * @param loreChatColor The color of the lore.
	 * @return The text-wrapped lore.
	 */
	public static @NotNull List<String> wrapLore(String lore, ChatColor loreChatColor) {
		return (ChatUtils.wrapLore(lore, 25, loreChatColor));
	}
}

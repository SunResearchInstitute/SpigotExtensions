package services.headpat.spigotextensions.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ChatUtils {
	/**
	 * @param str The original string using `&` color codes.
	 * @return The string with all color codes using `&` convert to `§`.
	 */
	public static @NonNull String covertColorCodes(@NonNull String str) {
		return str.replaceAll("&(?=[0-9a-fkl-mr])", "§");
	}

	/**
	 * Wraps text that will be displayed in a lore to the best of its ability.
	 *
	 * @param lore          The full length lore with no breaks.
	 * @param lineLength    The maximum length for a single line.
	 * @param loreChatColor The Color of the lore.
	 * @return The text-wrapped lore.
	 */
	@Deprecated // ONLY ON Paper
	public static @NonNull List<String> wrapLore(@NonNull String lore, int lineLength, ChatColor loreChatColor) {
		String[] words = lore.split(" ");

		List<String> result = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		for (String word : words) {
			if (builder.length() == 0 || ((builder.length() + 1 + word.length()) <= lineLength)) {
				if (builder.length() > 0) {
					builder.append(' ');
				}
			} else {
				result.add(loreChatColor + "" + ChatColor.ITALIC + builder);
				builder.setLength(0);
			}
			builder.append(word);
		}
		if (builder.length() != 0) {
			result.add(loreChatColor + "" + ChatColor.ITALIC + builder);
		}

		return (result);
	}

	/**
	 * Wraps text that will be displayed in a lore to the best of its ability.
	 *
	 * @param lore The full length lore with no breaks.
	 * @return The text-wrapped lore.
	 */
	@Deprecated // ONLY ON Paper
	public static @NonNull List<String> wrapLore(String lore) {
		return (ChatUtils.wrapLore(lore, 25, ChatColor.DARK_PURPLE));
	}

	/**
	 * Wraps text that will be displayed in a lore to the best of its ability.
	 *
	 * @param lore          The full length lore with no breaks.
	 * @param loreChatColor The color of the lore.
	 * @return The text-wrapped lore.
	 */
	@Deprecated // ONLY ON Paper
	public static @NonNull List<String> wrapLore(String lore, ChatColor loreChatColor) {
		return (ChatUtils.wrapLore(lore, 25, loreChatColor));
	}

	/**
	 * Wraps text that will be displayed in a lore to the best of its ability.
	 *
	 * @param lore The full length lore with no breaks.
	 * @return The text-wrapped lore.
	 */
	public static @NonNull List<Component> createLore(@NonNull String lore) {
		return createLore(lore, 25, NamedTextColor.DARK_PURPLE);
	}

	/**
	 * Wraps text that will be displayed in a lore to the best of its ability.
	 *
	 * @param lore       The full length lore with no breaks.
	 * @param lineLength The maximum length for a single line.
	 * @return The text-wrapped lore.
	 */
	public static @NonNull List<Component> createLore(@NonNull String lore, int lineLength) {
		return createLore(lore, lineLength, NamedTextColor.DARK_PURPLE);
	}

	/**
	 * Wraps text that will be displayed in a lore to the best of its ability.
	 *
	 * @param lore          The full length lore with no breaks.
	 * @param loreChatColor The Color of the lore.
	 * @return The text-wrapped lore.
	 */
	public static @NonNull List<Component> createLore(@NonNull String lore, TextColor loreChatColor) {
		return createLore(lore, 25, loreChatColor);
	}

	/**
	 * Wraps text that will be displayed in a lore to the best of its ability.
	 *
	 * @param lore          The full length lore with no breaks.
	 * @param lineLength    The maximum length for a single line.
	 * @param loreChatColor The Color of the lore.
	 * @return The text-wrapped lore.
	 */
	public static @NonNull List<Component> createLore(@NonNull String lore, int lineLength, TextColor loreChatColor) {
		String[] words = lore.split(" ");

		List<Component> result = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		for (String word : words) {
			if (builder.length() == 0 || ((builder.length() + 1 + word.length()) <= lineLength)) {
				if (builder.length() > 0) {
					builder.append(' ');
				}
			} else {
				result.add(Component.text(builder.toString()).color(loreChatColor).decorate(TextDecoration.ITALIC));
				builder.setLength(0);
			}
			builder.append(word);
		}
		if (builder.length() != 0) {
			result.add(Component.text(builder.toString()).color(loreChatColor).decorate(TextDecoration.ITALIC));
		}

		return (result);
	}
}
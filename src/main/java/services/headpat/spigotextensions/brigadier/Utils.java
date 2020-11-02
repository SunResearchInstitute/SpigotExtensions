package services.headpat.spigotextensions.brigadier;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Utils {
	@NotNull
	@Contract(pure = true)
	public static String[] addBeginningString(String str, @NotNull String... strings) {
		String[] newStrings = new String[strings.length + 1];
		newStrings[0] = str;
		System.arraycopy(strings, 0, newStrings, 1, strings.length);
		return newStrings;
	}

	public static @NotNull
	String getBrigadierString(@NotNull Command command, String[] args) {
		return String.join(" ", Utils.addBeginningString(command.getName(), args));
	}

	public static void sendUsageMessage(@NotNull CommandSender sender, String[] brigadierUsages) {
		sender.sendMessage(ChatColor.RED + "Usages:");
		Arrays.stream(brigadierUsages).forEach(s -> sender.sendMessage(ChatColor.RED + "/" + s));
	}
}

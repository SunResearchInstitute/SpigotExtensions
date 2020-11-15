package services.headpat.spigotextensions.brigadier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Creates autocomplete suggestions and onCommand handling automatically using {@link LiteralArgumentBuilder}.
 */
public class BrigadierExecutor implements TabExecutor {
	protected CommandDispatcher<CommandSender> commandDispatcher;

	/**
	 * Returning 1 from an executor in the LiteralArgumentBuilder will act as true and 0 and below will act as false.
	 *
	 * @param argumentBuilder The literal argument builder to create the command from.
	 */
	public BrigadierExecutor(LiteralArgumentBuilder<CommandSender> argumentBuilder) {
		commandDispatcher = new CommandDispatcher<>();
		commandDispatcher.register(argumentBuilder);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		try {
			int result = this.commandDispatcher.execute(getBrigadierString(command, args), sender);
			if (result <= 0) {
				sendUsageMessage(sender, this.commandDispatcher.getAllUsage(this.commandDispatcher.getRoot(), sender, true));
				return true;
			}
		} catch (CommandSyntaxException e) {
			if (e.getMessage() != null)
				sender.sendMessage(ChatColor.RED + e.getMessage());

			sendUsageMessage(sender, this.commandDispatcher.getAllUsage(this.commandDispatcher.getRoot(), sender, true));
		}
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		String commandArgs = getBrigadierString(command, args);
		Suggestions suggestions = this.commandDispatcher.getCompletionSuggestions(this.commandDispatcher.parse(commandArgs, sender)).join();
		return suggestions.getList().stream().map(Suggestion::getText).collect(Collectors.toList());
	}

	private static void sendUsageMessage(@NotNull CommandSender sender, String[] brigadierUsages) {
		sender.sendMessage(ChatColor.RED + "Usages:");
		Arrays.stream(brigadierUsages).forEach(s -> sender.sendMessage(ChatColor.RED + "/" + s));
	}

	private static @NotNull
	String getBrigadierString(@NotNull Command command, String[] args) {
		return String.join(" ", addBeginningString(command.getName(), args));
	}

	private static String @NotNull [] addBeginningString(String str, @NotNull String @NotNull ... strings) {
		String[] newStrings = new String[strings.length + 1];
		newStrings[0] = str;
		System.arraycopy(strings, 0, newStrings, 1, strings.length);
		return newStrings;
	}
}

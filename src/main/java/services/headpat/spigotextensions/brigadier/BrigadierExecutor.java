package services.headpat.spigotextensions.brigadier;

import com.google.common.collect.ObjectArrays;
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

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Creates autocomplete suggestions and onCommand handling automatically using {@link LiteralArgumentBuilder}.
 */
public class BrigadierExecutor implements TabExecutor {
	protected CommandDispatcher<CommandSender> commandDispatcher;

	/**
	 * Returning 1 from an executor in the LiteralArgumentBuilder will act as true and 0 and below will act as false.
	 *
	 * @param dispatcherConsumer The consumer to register commands with.
	 */
	public BrigadierExecutor(@NotNull Consumer<CommandDispatcher<CommandSender>> dispatcherConsumer) {
		this.commandDispatcher = new CommandDispatcher<>();
		dispatcherConsumer.accept(commandDispatcher);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		try {
			int result = this.commandDispatcher.execute(getCommandString(alias, args), sender);
			if (result <= 0) {
				sendUsageMessage(sender, this.commandDispatcher);
				return true;
			}
		} catch (CommandSyntaxException e) {
			if (e.getMessage() != null)
				sender.sendMessage(ChatColor.RED + e.getMessage());

			sendUsageMessage(sender, this.commandDispatcher);
			return true;
		}
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		String commandString = getCommandString(alias, args);
		Suggestions suggestions = this.commandDispatcher.getCompletionSuggestions(this.commandDispatcher.parse(commandString, sender)).join();
		return suggestions.getList().stream().map(Suggestion::getText).collect(Collectors.toList());
	}

	private static void sendUsageMessage(@NotNull CommandSender sender, @NotNull CommandDispatcher<CommandSender> commandDispatcher) {
		sender.sendMessage(ChatColor.RED + "Usages:");
		for (String s : commandDispatcher.getAllUsage(commandDispatcher.getRoot(), sender, true))
			sender.sendMessage(ChatColor.RED + "/" + s);
	}

	public String getCommandString(String alias, String[] args) {
		return String.join(" ", ObjectArrays.concat(alias, args));
	}
}

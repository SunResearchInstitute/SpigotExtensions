package services.headpat.spigotextensions.brigadier.argumenttypes;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * {@link Player} argument type to be used by brigadier.
 */
public class PlayerArgumentType implements ArgumentType<Player> {
	/**
	 * Shortcut to create a new {@link PlayerArgumentType} instance.
	 *
	 * @return {@link PlayerArgumentType} instance.
	 */
	@Contract(value = " -> new", pure = true)
	public static @NotNull
	PlayerArgumentType player() {
		return new PlayerArgumentType();
	}

	/**
	 * Quick shortcut of {@link CommandContext#getArgument(String, Class)} for a player argument.
	 *
	 * @param context Command context.
	 * @param name    Name of the argument.
	 * @return The player specified by the argument name in the command context.
	 */
	public static Player getPlayer(@NotNull CommandContext<?> context, String name) {
		return context.getArgument(name, Player.class);
	}

	@Override
	public Player parse(@NotNull StringReader reader) throws CommandSyntaxException {
		Player player = Bukkit.getPlayer(reader.readUnquotedString());
		if (player == null) {
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create("player not found.");
		} else
			return player;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
		Bukkit.getOnlinePlayers().forEach(player -> {
			if (player.getName().toLowerCase().startsWith(builder.getRemaining().toLowerCase()))
				builder.suggest(player.getName());
		});
		return builder.buildFuture();
	}

	@Override
	public Collection<String> getExamples() {
		return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
	}
}

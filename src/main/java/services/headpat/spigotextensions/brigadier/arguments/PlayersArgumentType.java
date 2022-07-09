package services.headpat.spigotextensions.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * {@link Player} argument type to be used by brigadier.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayersArgumentType implements ArgumentType<Collection<? extends Player>> {
    /**
     * Shortcut to create a new {@link PlayersArgumentType} instance.
     *
     * @return {@link PlayersArgumentType} instance.
     */
    public static @NonNull
    PlayersArgumentType players() {
        return new PlayersArgumentType();
    }

    /**
     * Quick shortcut of {@link CommandContext#getArgument(String, Class)} for a player argument.
     *
     * @param context Command context.
     * @param name    Name of the argument.
     * @return The player specified by the argument name in the command context.
     */
    public static Collection<? extends Player> getPlayers(@NonNull CommandContext<?> context, String name) {
        return ((Collection<? extends Player>) context.getArgument(name, Collection.class));
    }

    @Override
    public Collection<? extends Player> parse(@NonNull StringReader reader) throws CommandSyntaxException {
        String str = reader.readUnquotedString();
        if (str.equals("**")) {
            return Bukkit.getOnlinePlayers();
        }

        Player player = Bukkit.getPlayer(str);
        if (player == null) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create("Player not found.");
        } else {
            return List.of(player);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, @NonNull SuggestionsBuilder builder) {
        List<String> strings = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        strings.add("**");
        strings.forEach(s -> {
            if (s.toLowerCase().startsWith(builder.getRemaining().toLowerCase()))
                builder.suggest(s);
        });
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
    }
}

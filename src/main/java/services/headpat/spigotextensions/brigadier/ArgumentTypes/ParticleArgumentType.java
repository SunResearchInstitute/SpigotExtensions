package services.headpat.spigotextensions.brigadier.ArgumentTypes;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.Particle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ParticleArgumentType implements ArgumentType<Particle> {
	@Contract(value = " -> new", pure = true)
	public static @NotNull ParticleArgumentType particle() {
		return new ParticleArgumentType();
	}

	@Override
	public Particle parse(StringReader reader) throws CommandSyntaxException {
		Particle particle;
		try {
			particle = Particle.valueOf(reader.readString());
		} catch (Exception e) {
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create("invalid particle.");
		}
		return particle;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return null;
	}

	@Override
	public Collection<String> getExamples() {
		return Arrays.stream(Particle.values()).map(Enum::name).collect(Collectors.toList());
	}
}

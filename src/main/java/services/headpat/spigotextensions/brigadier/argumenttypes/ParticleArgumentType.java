package services.headpat.spigotextensions.brigadier.argumenttypes;

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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ParticleArgumentType implements ArgumentType<Particle> {
	@Contract(value = " -> new", pure = true)
	public static @NotNull ParticleArgumentType particle() {
		return new ParticleArgumentType();
	}

	public static Particle getParticle(@NotNull CommandContext<?> context, String name) {
		return context.getArgument(name, Particle.class);
	}

	@Override
	public Particle parse(@NotNull StringReader reader) throws CommandSyntaxException {
		Particle particle;
		String str = reader.getRemaining().toUpperCase();
		if (str.equals("NONE")) {
			particle = null;
		} else {
			try {
				particle = Particle.valueOf(str);
			} catch (Exception e) {
				throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create("invalid particle.");
			}
		}
		return particle;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		List<String> particleNames = Arrays.stream(Particle.values()).map(Enum::name).collect(Collectors.toList());
		particleNames.add("NONE");
		for (String particleName : particleNames) {
			if (particleName.toLowerCase().startsWith(builder.getRemaining().toLowerCase()))
				builder.suggest(particleName);
		}
		return builder.buildFuture();
	}

	@Override
	public Collection<String> getExamples() {
		List<String> examples = Arrays.stream(Particle.values()).map(Enum::name).collect(Collectors.toList());
		examples.add("NONE");
		return examples;
	}
}

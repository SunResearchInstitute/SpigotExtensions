package services.headpat.spigotextensions.brigadier.ArgumentTypes;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.Material;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MaterialArgumentType implements ArgumentType<Material> {
	@Contract(value = " -> new", pure = true)
	public static @NotNull MaterialArgumentType material() {
		return new MaterialArgumentType();
	}

	@Override
	public Material parse(@NotNull StringReader reader) throws CommandSyntaxException {
		Material material = Material.getMaterial(reader.readString());
		if (material == null)
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, null);
		else
			return material;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		for (Material value : Material.values()) {
			if (value.name().toLowerCase().startsWith(builder.getRemaining().toLowerCase()))
				builder.suggest(value.name());
		}
		return builder.buildFuture();
	}

	@Override
	public Collection<String> getExamples() {
		return Arrays.stream(Material.values()).map(Enum::name).collect(Collectors.toList());
	}
}

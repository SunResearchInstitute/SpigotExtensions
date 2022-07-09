package services.headpat.spigotextensions.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * {@link Material} argument type to be used by brigadier.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MaterialArgumentType implements ArgumentType<Material> {
	/**
	 * Shortcut to create a new {@link MaterialArgumentType} instance.
	 *
	 * @return {@link MaterialArgumentType} instance.
	 */
	public static MaterialArgumentType material() {
		return new MaterialArgumentType();
	}

	/**
	 * Quick shortcut of {@link CommandContext#getArgument(String, Class)} for a material argument.
	 *
	 * @param context Command context.
	 * @param name    Name of the argument.
	 * @return The material specified by the argument name in the command context.
	 */
	public static Material getMaterial(CommandContext<?> context, String name) {
		return context.getArgument(name, Material.class);
	}

	@Override
	public Material parse(StringReader reader) throws CommandSyntaxException {
		String str = reader.readUnquotedString().toUpperCase();
		Material material = Material.getMaterial(str);
		if (material == null) {
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create("invalid material.");
		} else
			return material;

	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		Arrays.stream(Material.values()).map(Enum::name).collect(Collectors.toList()).forEach(materialName -> {
			if (materialName.toLowerCase().startsWith(builder.getRemaining().toLowerCase()))
				builder.suggest(materialName);
		});
		return builder.buildFuture();
	}

	@Override
	public Collection<String> getExamples() {
		return Arrays.stream(Material.values()).map(Enum::name).collect(Collectors.toList());
	}
}

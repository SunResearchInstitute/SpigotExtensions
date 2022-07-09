package services.headpat.spigotextensions.brigadier;

import com.google.common.collect.ObjectArrays;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import lombok.NonNull;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Function;

/**
 * Creates autocomplete suggestions (using commodore) and onCommand handling automatically.
 */
public class BrigadierExecutor implements CommandExecutor {
    protected final CommandDispatcher<CommandSender> commandDispatcher;
    private final LiteralCommandNode<CommandSender> commandNode;

    /**
     * Returning 1 from an executor in the LiteralArgumentBuilder will act as true and 0 and below will act as false.
     *
     * @param dispatcherConsumer The consumer to register commands with and to return the LiteralCommandNode.
     */
    public BrigadierExecutor(@NonNull Function<CommandDispatcher<CommandSender>, LiteralCommandNode<CommandSender>> dispatcherConsumer) {
        this.commandDispatcher = new CommandDispatcher<>();
        this.commandNode = dispatcherConsumer.apply(commandDispatcher);
    }

    public void registerCommand(JavaPlugin plugin, PluginCommand command) {
        command.setExecutor(this);
        if (CommodoreProvider.isSupported()) {
            Commodore commodore = CommodoreProvider.getCommodore(plugin);
            commodore.register(command, commandNode);
        }
    }

    @Override
    public final boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String alias, @NonNull String[] args) {
        try {
            int result = this.commandDispatcher.execute(getCommandString(alias, args), sender);
            if (result <= 0) {
                return false;
            }
        } catch (CommandSyntaxException e) {
            if (e.getMessage() != null)
                sender.sendMessage(ChatColor.RED + e.getMessage());

            return false;
        }
        return true;
    }

    public final String getCommandString(String alias, String[] args) {
        return String.join(" ", ObjectArrays.concat(alias, args));
    }
}

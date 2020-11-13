package services.headpat.spigotextensions.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerUtils {
	/**
	 * Gets nearby players only, removing the time to find regular entities.
	 *
	 * @param location      Location of where to search.
	 * @param radius        Radius to search for players.
	 * @param sortByClosest Whether to sort Set by proximity.
	 * @return Players near the specified location.
	 */
	public static LinkedHashSet<Player> getNearbyPlayers(Location location, double radius, boolean sortByClosest) {
		Stream<? extends Player> stream = Bukkit.getOnlinePlayers().stream().filter((player) -> {
			if (location.getWorld() != player.getWorld()) {
				return (false);
			}
			return (location.distanceSquared(player.getLocation()) <= (radius * radius));
		});
		if (sortByClosest) {
			stream = stream.sorted(Comparator.comparingDouble(p -> p.getLocation().distanceSquared(location)));
		}
		return (stream.collect(Collectors.toCollection(LinkedHashSet::new)));
	}
}

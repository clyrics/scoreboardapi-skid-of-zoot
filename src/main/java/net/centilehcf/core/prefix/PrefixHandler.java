package net.centilehcf.core.prefix;

import net.centilehcf.core.Core;
import net.centilehcf.core.network.packet.PacketDeletePrefix;
import net.centilehcf.core.network.packet.PacketUpdatePrefix;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrefixHandler {

    private final Core plugin = Core.get();

    private final MongoCollection<Document> prefixCollection;

    @Getter
    private final List<Prefix> prefixes;

    public PrefixHandler() {
        this.prefixes = new ArrayList<>();
        this.prefixCollection = plugin.getMongoDatabase().getCollection("prefixes");
        loadPrefixes();
    }

    public Prefix getPrefixByName(String search) {
        return this.prefixes.stream().filter(prefix -> prefix.getName().equalsIgnoreCase(search)).findFirst().orElse(null);
    }

    public Prefix getDefaultPrefix() {
        return this.prefixes.stream().filter(prefix -> prefix.getName().equalsIgnoreCase("Default")).findFirst().orElse(null);

    }

    private void loadPrefixes() {
        for (Document document : prefixCollection.find()) {
            Prefix prefix = new Prefix(document.getString("name"));
            prefix.setPrefix(ChatColor.translateAlternateColorCodes('&', document.getString("prefix")));
            prefix.setWeight(document.getInteger("weight"));
            savePrefix(prefix);
        }

        // Create the default prefix if it doesn't exist!
        if (getDefaultPrefix() == null) {
            Prefix prefix = new Prefix("Default");
            prefix.setPrefix("");
            prefix.setWeight(-1);
            savePrefix(prefix);
        }
    }

    public Optional<Document> getPrefixDocumentFromDb(String name){
        return Optional.ofNullable(prefixCollection.find(Filters.eq("name", name)).first());
    }


    public void loadPrefixByName(String name) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            getPrefixDocumentFromDb(name).ifPresent(document -> {
                Prefix prefix = getPrefixByName(name);

                if (prefix != null) {
                    prefix.setPrefix(ChatColor.translateAlternateColorCodes('&', document.getString("prefix")));
                    prefix.setWeight(document.getInteger("weight"));
                } else {
                    prefix = new Prefix(document.getString("name"));
                    prefix.setPrefix(ChatColor.translateAlternateColorCodes('&', document.getString("prefix")));
                    prefix.setWeight(document.getInteger("weight"));
                    if (prefixes.contains(prefix)){
                        prefixes.remove(prefix);
                        prefixes.add(prefix);
                    } else {
                        prefixes.add(prefix);
                    }
                }
            });
        });
    }

    public void savePrefix(Prefix prefix) {
        if (!prefixes.contains(prefix)) {
            prefixes.add(prefix);
        }
        Document document = new Document();
        document.put("name", prefix.getName());
        document.put("prefix", prefix.getPrefix().replace(ChatColor.COLOR_CHAR, '&'));
        document.put("weight", prefix.getWeight());

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
           prefixCollection.replaceOne(Filters.eq("name", prefix.getName()), document, new ReplaceOptions().upsert(true));
           Core.get().getPidgin().sendPacket(new PacketUpdatePrefix(prefix.getName()));
        });
    }

    public void removePrefix(Prefix prefix) {
        prefixes.remove(prefix);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            prefixCollection.deleteOne(Filters.eq("name", prefix.getName())); // Deletes the prefix
            Core.get().getPidgin().sendPacket(new PacketDeletePrefix(prefix.getName()));
            plugin.getMongoDatabase().getCollection("profiles") // Everyone that had that prefix gets their current one set to the default
                    .updateMany(Filters.eq("prefix", prefix.getName()), new Document("$set", getDefaultPrefix().getName()));
        });
    }
}

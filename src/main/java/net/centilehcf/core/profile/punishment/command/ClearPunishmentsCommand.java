package net.centilehcf.core.profile.punishment.command;

import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.Core;
import net.centilehcf.core.network.packet.PacketRemovePunishments;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.util.CC;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

@CommandMeta(label = "clearpunishments", permission = "command.clearpunishments", async = true)
public class ClearPunishmentsCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile) {
        // If it is for a single profile only
        if (profile != null) {
            Command.broadcastCommandMessage(sender, CC.YELLOW + "Cleared all punishments for " + profile.getUsername() + '.');
            profile.getPunishments().clear();
            profile.save();
            Core.get().getPidgin().sendPacket(new PacketRemovePunishments(profile.getUuid()));
        }
    }

    public void execute(CommandSender sender) {
        // For all profiles
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(CC.RED + "This command can only be executed from the console!");
        } else {
            Core.get().debug(CC.YELLOW + "Attempting to clear all punishments. This could take a few seconds...");
            UpdateResult result = Core.get().getMongoDatabase()
                    .getCollection("profiles")
                    .updateMany(new Document(), new Document("$set", new Document("punishments", "[]")), new UpdateOptions().upsert(false));
            if (result.wasAcknowledged()) {
                Core.get().debug(CC.AQUA + "Cleared all punishments. \n * Result was acknowledged \n * Modified " + result.getModifiedCount() + " documents");
            } else {
                Core.get().debug(CC.RED + "Couldn't clear all punishments\n" + CC.RED + CC.BOLD + "The result was not acknowledged");
            }
        }
    }
}

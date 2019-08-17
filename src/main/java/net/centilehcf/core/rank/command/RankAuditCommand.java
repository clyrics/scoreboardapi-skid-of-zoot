package net.centilehcf.core.rank.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.Core;
import net.centilehcf.core.profile.grant.Grant;
import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.HastebinUtil;
import net.centilehcf.core.util.TimeUtil;
import net.centilehcf.core.uuid.UUIDCache;
import org.bson.Document;
import org.bukkit.command.CommandSender;
import org.fusesource.jansi.Ansi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@CommandMeta(label = "rank audit", permission = "rank.manager", async = true)
public class RankAuditCommand {

    public void execute(CommandSender sender, Rank rank) {
        sender.sendMessage(CC.YELLOW + "Preforming audit on " + CC.RED + rank.getDisplayName() + CC.YELLOW + ". This may take a few seconds.");
        StringBuffer buffer = new StringBuffer();
        buffer.append("----------------------\n");
        buffer.append("Audit for ").append(rank.getDisplayName()).append('\n');
        buffer.append("Prefix: ").append(rank.getPrefix()).append(" Suffix: ").append(rank.getSuffix()).append('\n');
        buffer.append("Color: ").append(rank.getColor().name()).append('\n');
        buffer.append("--\n");
        buffer.append("Permissions:\n");
        rank.getPermissions().forEach(s -> buffer.append(s).append('\n'));
        buffer.append("--\n");
        buffer.append("Users that have this rank:\n");
        for (Document document : Core.get().getMongoDatabase().getCollection("profiles").find()) {
            List<Grant> grantList = new ArrayList<>();
            JsonArray jsonGrants = new JsonParser().parse(document.getString("grants")).getAsJsonArray();
            for (JsonElement jsonElement : jsonGrants) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Rank jsonRank = Rank.getRankByUuid(UUID.fromString(jsonObject.get("rank").getAsString()));

                if (jsonRank != null) {
                    grantList.add(Grant.DESERIALIZER.deserialize(jsonObject));
                }
            }

            for (Grant grant : grantList) {
                if (!grant.isRemoved()) {
                    buffer.append(document.getString("username")).append(" (").append(document.getString("uuid")).append(")\n");
                    buffer.append(" *").append("Added by: ").append(grant.getAddedBy() != null ? Core.get().getUuidCache().getName(grant.getAddedBy()) : "Console").append(" (").append(grant.getAddedBy() != null ? grant.getAddedBy() : UUIDCache.CONSOLE_UUID.toString()).append(")\n");
                    buffer.append(" *").append("Date added: ").append(grant.getAddedAtDate()).append('\n');
                    buffer.append(" *").append("Reason: ").append(grant.getAddedReason()).append('\n');
                    buffer.append("\u001B[32m").append(" ** ACTIVE **").append("\u001B[0m");
                }
            }
        }
        buffer.append("----------------------\n");
        buffer.append("Generated on ").append(TimeUtil.dateToString(new Date())).append(" by ").append(sender.getName()).append('\n');
        buffer.append("----------------------");
        sender.sendMessage(CC.GREEN + "Summary of Audit: " + HastebinUtil.paste(buffer.toString()));
    }
}

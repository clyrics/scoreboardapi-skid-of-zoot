package net.centilehcf.core.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.centilehcf.core.Core;
import net.centilehcf.core.prefix.Prefix;
import net.centilehcf.core.profile.grant.Grant;
import net.centilehcf.core.profile.grant.event.GrantAppliedEvent;
import net.centilehcf.core.profile.grant.event.GrantExpireEvent;
import net.centilehcf.core.profile.option.ProfileOptions;
import net.centilehcf.core.profile.option.ProfileStaffOptions;
import net.centilehcf.core.profile.punishment.Punishment;
import net.centilehcf.core.profile.punishment.PunishmentType;
import net.centilehcf.core.rank.Rank;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import net.centilehcf.core.util.Cooldown;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;

public class Profile {

    @Getter
    private static Map<UUID, Profile> profiles = new HashMap<>();
    private static MongoCollection<Document> collection;
    @Getter
    private final UUID uuid;
    @Getter
    private final ProfileOptions options = new ProfileOptions();
    @Getter
    private final ProfileStaffOptions staffOptions = new ProfileStaffOptions();
    @Getter
    private final List<Grant> grants;
    @Getter
    private final List<Punishment> punishments;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private Long firstSeen;
    @Getter
    @Setter
    private Long lastSeen;
    @Getter
    @Setter
    private String currentAddress;
    @Getter
    private List<String> ipAddresses = new ArrayList<>();
    @Getter
    private List<UUID> knownAlts = new ArrayList<>();
    @Getter
    private List<String> individualPermissions;
    @Getter
    private Grant activeGrant;
    @Getter
    @Setter
    private Prefix prefix;
    @Getter
    @Setter
    private UUID replyTo;
    @Getter
    @Setter
    private boolean loaded;
    @Getter
    @Setter
    private Cooldown requestCooldown = new Cooldown(0);
    @Setter
    @Getter
    private Cooldown chatCooldown = new Cooldown(1000);

    public Profile(String username, UUID uuid) {
        this.username = username;
        this.uuid = uuid;
        this.grants = new ArrayList<>();
        this.punishments = new ArrayList<>();
        this.individualPermissions = new ArrayList<>();

        load();
    }

    public static void init() {
        collection = Core.get().getMongoDatabase().getCollection("profiles");
    }

    public static Profile getByUuid(UUID uuid) {
        if (profiles.containsKey(uuid)) {
            return profiles.get(uuid);
        }

        return new Profile(null, uuid);
    }

    public static Profile getByUsername(String username) {
        Player player = Bukkit.getPlayer(username);

        if (player != null) {
            return profiles.get(player.getUniqueId());
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);

        if (offlinePlayer.hasPlayedBefore()) {
            if (profiles.containsKey(offlinePlayer.getUniqueId())) {
                return profiles.get(offlinePlayer.getUniqueId());
            }

            return new Profile(offlinePlayer.getName(), offlinePlayer.getUniqueId());
        }

        UUID uuid = Core.get().getUuidCache().getUuid(username);

        if (uuid != null) {
            if (profiles.containsKey(uuid)) {
                return profiles.get(uuid);
            }

            return new Profile(username, uuid);
        }

        return null;
    }

    public static List<Profile> getByIpAddress(String ipAddress) {
        List<Profile> profiles = new ArrayList<>();

        try (MongoCursor<Document> cursor = collection.find(Filters.eq("currentAddress", ipAddress)).iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                profiles.add(new Profile(document.getString("username"), UUID.fromString(document.getString("uuid"))));
            }
        }

        return profiles;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public String getColoredUsername() {
        return activeGrant.getRank().getColor() + username;
    }

    public Punishment getActiveMute() {
        for (Punishment punishment : punishments) {
            if (punishment.getType() == PunishmentType.MUTE && punishment.isActive()) {
                return punishment;
            }
        }

        return null;
    }

    public Punishment getActiveBlacklist() {
        for (Punishment punishment : punishments) {
            if (punishment.getType() == PunishmentType.BLACKLIST && punishment.isActive()) {
                return punishment;
            }
        }
        return null;
    }

    public Punishment getActiveBan() {
        for (Punishment punishment : punishments) {
            if (punishment.getType().isBan() && punishment.isActive()) {
                return punishment;
            }
        }

        return null;
    }

    public int getPunishmentCountByType(PunishmentType type) {
        int i = 0;

        for (Punishment punishment : punishments) {
            if (punishment.getType() == type) i++;
        }

        return i;
    }

    public Rank getActiveRank() {
        return activeGrant.getRank();
    }

    public void setActiveGrant(Grant grant) {
        activeGrant = grant;

        Player player = getPlayer();
        if (player != null) {
            player.setDisplayName(grant.getRank().getPrefix() + player.getName() + grant.getRank().getSuffix());
        }
    }

    public void activateNextGrant() {
        List<Grant> grants = new ArrayList<>(this.grants);

        grants.sort(Comparator.comparingInt(grant -> grant.getRank().getWeight()));

        for (Grant grant : grants) {
            if (!grant.isRemoved() && !grant.hasExpired()) {
                setActiveGrant(grant);
            }
        }
    }

    public void checkGrants() {
        for (Grant grant : grants) {
            if (!grant.isRemoved() && grant.hasExpired()) {
                grant.setRemovedAt(System.currentTimeMillis());
                grant.setRemovedReason("Grant expired");
                grant.setRemoved(true);

                if (activeGrant != null && activeGrant.equals(grant)) {
                    activeGrant = null;
                }

                Player player = getPlayer();

                if (player != null) {
                    new GrantExpireEvent(player, grant).call();
                }
            }
        }

        if (activeGrant == null) {
            activateNextGrant();

            if (activeGrant != null) {
                return;
            }

            Grant grant = new Grant(UUID.randomUUID(), Rank.getDefaultRank(), null,
                    System.currentTimeMillis(), "Default", Integer.MAX_VALUE);
            this.grants.add(grant);

            setActiveGrant(grant);

            Player player = getPlayer();

            if (player != null) {
                new GrantAppliedEvent(player, grant).call();
            }
        }
    }

    public void setupPermissionsAttachment(Core core, Player player) {
        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            if (attachmentInfo.getAttachment() == null) {
                continue;
            }

            attachmentInfo.getAttachment().getPermissions().forEach((permission, value) -> {
                attachmentInfo.getAttachment().unsetPermission(permission);
            });
        }

        PermissionAttachment attachment = player.addAttachment(core);

        for (String perm : activeGrant.getRank().getAllPermissions()) { // Rank permissions
            attachment.setPermission(perm, true);
        }

        // Check for 'null' permissions
        individualPermissions.removeIf(s -> s == null || s.isEmpty());

        for (String permission : individualPermissions) { // Individual permissions
            attachment.setPermission(permission, true);
        }

        player.recalculatePermissions();
    }

    public void load() {

        final Document document = collection.find(Filters.eq("uuid", uuid.toString())).first();

        if (document != null) {
            if (username == null) {
                username = document.getString("username");
            }

            firstSeen = document.getLong("firstSeen");
            lastSeen = document.getLong("lastSeen");
            currentAddress = document.getString("currentAddress");
            ipAddresses = Core.GSON.fromJson(document.getString("ipAddresses"), Core.LIST_STRING_TYPE);
            this.individualPermissions = Core.GSON.fromJson(document.getString("individualPermissions"), Core.LIST_STRING_TYPE);

            Prefix prefix = Core.get().getPrefixHandler().getPrefixByName(document.getString("prefix"));
            if (prefix != null) {
                this.prefix = prefix;
            } else {
                this.prefix = Core.get().getPrefixHandler().getDefaultPrefix();
            }

            Document optionsDocument = (Document) document.get("options");
            options.setPublicChatEnabled(optionsDocument.getBoolean("publicChatEnabled"));
            options.setPrivateChatEnabled(optionsDocument.getBoolean("privateChatEnabled"));
            options.setPrivateChatSoundsEnabled(optionsDocument.getBoolean("privateChatSoundsEnabled"));

            JsonArray grants = new JsonParser().parse(document.getString("grants")).getAsJsonArray();
            for (JsonElement jsonElement : grants) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Rank rank = Rank.getRankByUuid(UUID.fromString(jsonObject.get("rank").getAsString()));

                if (rank != null) {
                    this.grants.add(Grant.DESERIALIZER.deserialize(jsonObject));
                }
            }

            JsonArray punishments = new JsonParser().parse(document.getString("punishments")).getAsJsonArray();
            for (JsonElement jsonElement : punishments) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                this.punishments.add(Punishment.DESERIALIZER.deserialize(jsonObject));
            }
        }

        checkGrants();

        // Set loaded to true
        loaded = true;
    }

    public void save() {
        Document document = new Document();
        document.put("username", username);
        document.put("uuid", uuid.toString());
        document.put("firstSeen", firstSeen);
        document.put("lastSeen", lastSeen);
        document.put("currentAddress", currentAddress);
        document.put("ipAddresses", Core.GSON.toJson(ipAddresses, Core.LIST_STRING_TYPE));
        document.put("individualPermissions", Core.GSON.toJson(individualPermissions, Core.LIST_STRING_TYPE));

        if (this.prefix != null) {
            document.put("prefix", this.prefix.getName());
        } else {
            document.put("prefix", Core.get().getPrefixHandler().getDefaultPrefix().getName());
        }

        Document optionsDocument = new Document();
        optionsDocument.put("publicChatEnabled", options.isPublicChatEnabled());
        optionsDocument.put("privateChatEnabled", options.isPrivateChatEnabled());
        optionsDocument.put("privateChatSoundsEnabled", options.isPrivateChatSoundsEnabled());
        document.put("options", optionsDocument);

        JsonArray grants = new JsonArray();
        for (Grant grant : this.grants) {
            grants.add(Grant.SERIALIZER.serialize(grant));
        }
        document.put("grants", grants.toString());

        JsonArray punishments = new JsonArray();
        for (Punishment punishment : this.punishments) {
            punishments.add(Punishment.SERIALIZER.serialize(punishment));
        }
        document.put("punishments", punishments.toString());

        collection.replaceOne(Filters.eq("uuid", uuid.toString()), document, new ReplaceOptions().upsert(true));
    }

}

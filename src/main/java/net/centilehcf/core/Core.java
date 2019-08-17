package net.centilehcf.core;

import com.comphenix.protocol.ProtocolLibrary;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minexd.pidgin.Pidgin;
import net.centilehcf.core.chat.Chat;
import net.centilehcf.core.chat.command.MuteChatCommand;
import net.centilehcf.core.chat.listener.ChatListener;
import net.centilehcf.core.essentials.Essentials;
import net.centilehcf.core.essentials.command.*;
import net.centilehcf.core.essentials.listener.DurabilityListener;
import net.centilehcf.core.essentials.listener.EssentialsListener;
import net.centilehcf.core.hook.VaultProvider;
import net.centilehcf.core.network.NetworkPacketListener;
import net.centilehcf.core.network.packet.*;
import net.centilehcf.core.prefix.Prefix;
import net.centilehcf.core.prefix.PrefixHandler;
import net.centilehcf.core.prefix.command.*;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.profile.ProfileTypeAdapter;
import net.centilehcf.core.profile.command.OptionsCommand;
import net.centilehcf.core.profile.command.StaffChatToggleCommand;
import net.centilehcf.core.profile.command.individualperms.AddIndividualPermissionCommand;
import net.centilehcf.core.profile.command.individualperms.ListIndividualPermissionsCommand;
import net.centilehcf.core.profile.command.individualperms.RemoveIndividualPermissionCommand;
import net.centilehcf.core.profile.grant.command.ClearGrantsCommand;
import net.centilehcf.core.profile.grant.command.GrantCommand;
import net.centilehcf.core.profile.grant.command.GrantsCommand;
import net.centilehcf.core.profile.grant.command.SetRankCommand;
import net.centilehcf.core.profile.grant.listener.GrantListener;
import net.centilehcf.core.profile.ProfileListener;
import net.centilehcf.core.profile.option.commands.TogglePrivateMessagesCommand;
import net.centilehcf.core.profile.punishment.command.*;
import net.centilehcf.core.profile.command.AltsCommand;
import net.centilehcf.core.profile.punishment.listener.PunishmentListener;
import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.rank.RankTypeAdapter;
import net.centilehcf.core.profile.command.StaffChatCommand;
import net.centilehcf.core.rank.command.*;
import net.centilehcf.core.tab.TabAdapter;
import net.centilehcf.core.tab.TabEngine;
import net.centilehcf.core.tab.test.TestLayoutProvider;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.TaskUtil;
import net.centilehcf.core.util.duration.Duration;
import net.centilehcf.core.util.duration.DurationTypeAdapter;
import net.centilehcf.core.util.menu.MenuListener;
import net.centilehcf.core.uuid.UUIDCache;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.qrakn.honcho.Honcho;
import com.qrakn.phoenix.lang.file.type.BasicConfigurationFile;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import lombok.Getter;
import net.centilehcf.core.network.packet.PacketDeleteGrant;
import net.centilehcf.core.network.packet.PacketDeleteRank;
import net.centilehcf.core.network.packet.PacketStaffSwitchServer;
import net.centilehcf.core.prefix.command.AddPrefixCommand;
import net.centilehcf.core.prefix.command.ListPrefixCommand;
import net.centilehcf.core.prefix.command.PrefixTypeAdapter;
import net.centilehcf.core.prefix.command.SetPrefixCommand;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
public class Core extends JavaPlugin {

    public static Gson GSON = new Gson();
    public static Type LIST_STRING_TYPE = new TypeToken<List<String>>() {
    }.getType();
    private static Core core;
    private BasicConfigurationFile mainConfig;
    private Honcho honcho;
    private MongoDatabase mongoDatabase;
    private JedisPool jedisPool;
    private UUIDCache uuidCache;
    private Essentials essentials;
    private PrefixHandler prefixHandler;
    private Chat chat;
    private Pidgin pidgin;
    private boolean loaded;

    @Override
    public void onEnable() {
        core = this;

        getServer().getServicesManager().register(Permission.class, new VaultProvider(), this, ServicePriority.Highest);

        mainConfig = new BasicConfigurationFile(this, "config");

        loadMongo();
        loadRedis();

        uuidCache = new UUIDCache(this);
        essentials = new Essentials(this);
        chat = new Chat(this);

        honcho = new Honcho(this);

        Arrays.asList(
                new BroadcastCommand(),
                new ClearCommand(),
                new DayCommand(),
                new GameModeCommand(),
                new HealCommand(),
                new HidePlayerCommand(),
                new MoreCommand(),
                new NightCommand(),
                new SetSlotsCommand(),
                new SetSpawnCommand(),
                new ShowPlayerCommand(),
                new SpawnCommand(),
                new SunsetCommand(),
                new AltsCommand(),
                new BanCommand(),
                new BlacklistCommand(),
                new UnblacklistCommand(),
                new HistoryCommand(),
                new KickCommand(),
                new MuteCommand(),
                new UnbanCommand(),
                new UnmuteCommand(),
                new WarnCommand(),
                new GrantCommand(),
                new GrantsCommand(),
                new StaffChatCommand(),
                new StaffChatToggleCommand(),
                new MuteChatCommand(),
                new OptionsCommand(),
                new RankAddPermissionCommand(),
                new RankCreateCommand(),
                new RankDeleteCommand(),
                new RankRenameCommand(),
                new RankRemovePermissionCommand(),
                new RankSetColorCommand(),
                new RankSetPrefixCommand(),
                new RankSetSuffixCommand(),
                new RankSetWeightCommand(),
                new RankAuditCommand(),
                new RankDumpCommand(),
                new RankListCommand(),
                new PingCommand(),
                new AddIndividualPermissionCommand(),
                new ListIndividualPermissionsCommand(),
                new RemoveIndividualPermissionCommand(),
                new PrefixCommand(),
                new AddPrefixCommand(),
                new DeletePrefixCommand(),
                new ListPrefixCommand(),
                new SetPrefixCommand(),
                new SetPrefixCommand(),
                new TeleportAllCommand(),
                new TeleportHereCommand(),
                new MasssayCommand(),
                new RawCommand(),
                new TeleportPositionCommand(),
                new MessageCommand(),
                new ReplyCommand(),
                new TeleportCommand(),
                new InvseeCommand(),
                new StreamingCommand(),
                new WorldCommand(),
                new ClearChatCommand(),
                new SpawnerCommand(),
                new RequestCommand(),
                new ListCommand(),
                new ClearGrantsCommand(),
                new ClearPunishmentsCommand(),
                new TogglePrivateMessagesCommand(),
                new CraftCommand(),
                new RenameCommand(),
                new SetRankCommand()
        ).forEach(honcho::registerCommand);


        honcho.registerTypeAdapter(Rank.class, new RankTypeAdapter());
        honcho.registerTypeAdapter(Profile.class, new ProfileTypeAdapter());
        honcho.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        honcho.registerTypeAdapter(Prefix.class, new PrefixTypeAdapter());

        Arrays.asList(
                new ProfileListener(this),
                new MenuListener(this),
                new EssentialsListener(this),
                new ChatListener(this),
                new GrantListener(this),
                new PunishmentListener(this),
                new DurabilityListener(this)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        pidgin = new Pidgin("zoot",
                mainConfig.getString("REDIS.HOST"),
                mainConfig.getInteger("REDIS.PORT"),
                mainConfig.getBoolean("REDIS.AUTHENTICATION.ENABLED") ?
                        mainConfig.getString("REDIS.AUTHENTICATION.PASSWORD") : null
        );

        Arrays.asList(
                PacketAddGrant.class,
                PacketBroadcastPunishment.class,
                PacketDeleteGrant.class,
                PacketDeleteRank.class,
                PacketRefreshRank.class,
                PacketStaffChat.class,
                PacketStaffJoinNetwork.class,
                PacketStaffLeaveNetwork.class,
                PacketStaffSwitchServer.class,
                PacketUpdatePrefix.class,
                PacketDeletePrefix.class,
                PacketRequestCommand.class,
                PacketClearGrants.class,
                PacketServerRestart.class,
                PacketRemovePunishments.class
        ).forEach(pidgin::registerPacket);

        pidgin.registerListener(new NetworkPacketListener(this));
        Rank.init();
        Profile.init();

        ProtocolLibrary.getProtocolManager().addPacketListener(new TabAdapter());

/*        TabEngine.init();
        TabEngine.setLayoutProvider(new TestLayoutProvider());*/

        this.prefixHandler = new PrefixHandler();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Profile profile : Profile.getProfiles().values()) {
                    profile.checkGrants();
                }
            }
        }.runTaskTimerAsynchronously(this, 20L, 20L);
        TaskUtil.runLater(() -> this.loaded = true, 60L);
        Core.get().getPidgin().sendPacket(new PacketServerRestart( Core.get().getMainConfig().getString("SERVER_NAME"), CC.GREEN + "Online"));
    }

    @Override
    public void onDisable() {
        Core.get().getPidgin().sendPacket(new PacketServerRestart( Core.get().getMainConfig().getString("SERVER_NAME"), CC.RED +"Offline"));
        try {
            jedisPool.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints a message and exception to console. If the server is not in debug mode, the messages will be suppressed.
     *
     * @param level     The log level.
     * @param message   The message.
     * @param exception The thrown exception.
     */
    public void debug(Level level, String message, Exception exception) {
        getLogger().log(level, message);
        exception.printStackTrace();
    }

    /**
     * Prints a message to console and server operators.
     *
     * @param message The message.
     */
    public void debug(String message) {
        broadcastOps(CC.translate("&e(Debug) &r" + message));
    }

    /**
     * Prints a message triggered by an action a player performed to console and server operators.
     *
     * @param player  The player that triggered this log.
     * @param message The message.
     */
    public void debug(Player player, String message) {
        broadcastOps(CC.translate("&e(Debug) &r" + player.getDisplayName() + ": " + message));
    }

    /**
     * Broadcasts a message to all server operators.
     *
     * @param message The message.
     */
    public static void broadcastOps(String message) {
        Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(op -> op.sendMessage(message));
    }

    private void loadMongo() {
        if (mainConfig.getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
            mongoDatabase = new MongoClient(
                    new ServerAddress(
                            mainConfig.getString("MONGO.HOST"),
                            mainConfig.getInteger("MONGO.PORT")),
                    MongoCredential.createCredential(
                            mainConfig.getString("MONGO.AUTHENTICATION.USERNAME"),
                            "admin", mainConfig.getString("MONGO.AUTHENTICATION.PASSWORD").toCharArray()),
                    MongoClientOptions.builder().build()
            ).getDatabase("zoot");
        } else {
            mongoDatabase = new MongoClient(mainConfig.getString("MONGO.HOST"), mainConfig.getInteger("MONGO.PORT"))
                    .getDatabase("zoot");
        }
    }

    private void loadRedis() {
        jedisPool = new JedisPool(mainConfig.getString("REDIS.HOST"), mainConfig.getInteger("REDIS.PORT"));

        if (mainConfig.getBoolean("REDIS.AUTHENTICATION.ENABLED")) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.auth(mainConfig.getString("REDIS.AUTHENTICATION.PASSWORD"));
            }
        }
    }

    public static Core get() {
        return core;
    }

}

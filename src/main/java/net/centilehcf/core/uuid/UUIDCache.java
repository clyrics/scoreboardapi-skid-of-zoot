package net.centilehcf.core.uuid;

import net.centilehcf.core.Core;
import net.centilehcf.core.bootstrap.Bootstrapped;
import net.minecraft.util.com.google.gson.JsonObject;
import net.minecraft.util.com.google.gson.JsonParser;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.logging.Level;

public class UUIDCache extends Bootstrapped {

    public static final UUID CONSOLE_UUID = UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670");

    public UUIDCache(Core core) {
        super(core);
        // Ensure the CONSOLE name & uuid are in the cache
        core.getServer().getScheduler().runTaskAsynchronously(core, () -> update("CONSOLE", CONSOLE_UUID));
    }

    private static UUID getUUIDFromNameMojang(String name) throws IOException, ParseException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = reader.readLine();

        if (line == null) {
            return null;
        }

        String[] id = line.split(",");

        String part = id[0];
        part = part.substring(7, 39);

        return UUID.fromString(part.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                "$1-$2-$3-$4-$5"));
    }

    private static String getNameFromUUIDMojang(UUID uuid) throws IOException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + uuid.toString().replace("-", ""));
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = reader.readLine();

        if (line == null) {
            return null;
        }

        JsonObject mojangQuery = new JsonParser().parse(line).getAsJsonObject();

        return mojangQuery.get("name").getAsString();
    }

    public UUID getUuid(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The parameter 'name' cannot be null or empty (UUID cache)");
        }

        if (core.getServer().isPrimaryThread()) {
            throw new IllegalStateException("Cannot query on main thread (Redis profile cache)");
        }

        try (Jedis jedis = core.getJedisPool().getResource()) {
            String uuid = jedis.hget("uuid-cache:name-to-uuid", name.toLowerCase());

            if (uuid != null) {
                return UUID.fromString(uuid);
            }
        } catch (Exception e) {
            core.debug(Level.WARNING, "Could not connect to redis", e);
        }

        try {
            UUID uuid = getUUIDFromNameMojang(name);

            if (uuid != null) {
                update(name, uuid);
                return uuid;
            }
        } catch (Exception e) {
            core.debug(Level.WARNING, "Could not fetch from Mojang API", e);
        }

        return null;
    }

    public String getName(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("The parameter 'uuid' cannot be null (UUID cache)");
        }

        if (core.getServer().isPrimaryThread()) {
            throw new IllegalStateException("Cannot query on main thread (Redis profile cache)");
        }

        try (Jedis jedis = core.getJedisPool().getResource()) {
            String name = jedis.hget("uuid-cache:uuid-to-name", uuid.toString());

            if (name != null) {
                return name;
            }
        } catch (Exception e) {
            core.debug(Level.WARNING, "Could not connect to redis", e);
        }

        try {
            String name = getNameFromUUIDMojang(uuid);

            if (name != null) {
                update(name, uuid);
                return name;
            }
        } catch (Exception e) {
            core.debug(Level.WARNING, "Could not fetch from Mojang API", e);
        }
        return null;
    }

    public void update(String name, UUID uuid) {
        try (Jedis jedis = core.getJedisPool().getResource()) {
            jedis.hset("uuid-cache:name-to-uuid", name.toLowerCase(), uuid.toString());
            jedis.hset("uuid-cache:uuid-to-name", uuid.toString(), name);
        }
    }

}

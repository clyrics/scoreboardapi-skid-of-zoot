package net.centilehcf.core.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import lombok.Getter;
import lombok.Setter;
import net.centilehcf.core.util.json.JsonChain;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/21/2019.
 */
@Getter
@Setter
public class PacketRequestCommand implements Packet {

    private String playerName;
    private String serverName;
    private String request;

    public PacketRequestCommand() {

    }

    public PacketRequestCommand(String serverName, String playerName, String request) {
        this.serverName = serverName;
        this.playerName = playerName;
        this.request = request;
    }

    public int id() {
        return 12;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .addProperty("serverName", serverName)
                .addProperty("playerName", playerName)
                .addProperty("request", request)
                .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        serverName = jsonObject.get("serverName").getAsString();
        playerName = jsonObject.get("playerName").getAsString();
        request = jsonObject.get("request").getAsString();
    }

}

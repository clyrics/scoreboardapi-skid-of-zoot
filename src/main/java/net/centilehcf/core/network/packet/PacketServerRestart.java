package net.centilehcf.core.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import lombok.Getter;
import lombok.Setter;
import net.centilehcf.core.util.json.JsonChain;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 5/21/2019.
 */
@Getter
@Setter
public class PacketServerRestart implements Packet {

    private String serverName;
    private String status;

    public PacketServerRestart() {
    }

    public PacketServerRestart(String serverName, String status) {
        this.serverName = serverName;
        this.status = status;
    }

    public int id() {
        return 14;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .addProperty("serverName", serverName)
                .addProperty("status", status)
                .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        serverName = jsonObject.get("serverName").getAsString();
        status = jsonObject.get("status").getAsString();
    }
}

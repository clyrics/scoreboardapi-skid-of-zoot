package net.centilehcf.core.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.centilehcf.core.util.json.JsonChain;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class PacketRemovePunishments implements Packet {

    private UUID uuid;

    public PacketRemovePunishments() {
    }

    @Override
    public int id() {
        return 15;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("uuid", uuid.toString()).get();
    }

    @Override
    public void deserialize(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
    }
}

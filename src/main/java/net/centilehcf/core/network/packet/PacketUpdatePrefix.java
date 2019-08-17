package net.centilehcf.core.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import net.centilehcf.core.Core;
import net.centilehcf.core.util.json.JsonChain;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PacketUpdatePrefix implements Packet {

    private String prefixName;

    public PacketUpdatePrefix(String prefixName) {
        this.prefixName = prefixName;
    }

    @Override
    public int id() {
        return 10;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("name", prefixName).get();
    }

    @Override
    public void deserialize(JsonObject object) {
        Core.get().getPrefixHandler().loadPrefixByName(object.get("name").getAsString());
    }
}

package net.centilehcf.core.network.packet;

import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.centilehcf.core.util.json.JsonChain;

import java.util.UUID;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 5/14/2019.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketClearGrants implements Packet {

    private UUID uuid;

    @Override
    public int id() {
        return 13;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
                .addProperty("uuid", uuid.toString())
                .get();
    }

    @Override
    public void deserialize(JsonObject object) {
        uuid = UUID.fromString(object.get("uuid").getAsString());
    }

}

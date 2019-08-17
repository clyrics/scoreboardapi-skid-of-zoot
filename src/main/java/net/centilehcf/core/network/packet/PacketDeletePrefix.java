package net.centilehcf.core.network.packet;


import com.google.gson.JsonObject;
import com.minexd.pidgin.packet.Packet;
import net.centilehcf.core.Core;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.util.json.JsonChain;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PacketDeletePrefix implements Packet {

    private String prefix;

    public PacketDeletePrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public int id() {
        return 11;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain().addProperty("name", prefix).get();
    }

    @Override
    public void deserialize(JsonObject object) {
        Profile.getProfiles().forEach((uuid, profile) -> {
            if (profile.getPrefix().getName().equalsIgnoreCase(object.get("name").getAsString())) {
                profile.setPrefix(Core.get().getPrefixHandler().getDefaultPrefix());
            }
        });
    }
}

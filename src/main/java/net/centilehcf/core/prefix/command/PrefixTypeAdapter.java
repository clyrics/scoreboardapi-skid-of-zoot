package net.centilehcf.core.prefix.command;

import net.centilehcf.core.Core;
import net.centilehcf.core.prefix.Prefix;
import com.qrakn.honcho.command.adapter.CommandTypeAdapter;

import java.util.List;
import java.util.stream.Collectors;

public class PrefixTypeAdapter implements CommandTypeAdapter {

    @Override
    public <T> T convert(String string, Class<T> type) {
        return type.cast(Core.get().getPrefixHandler().getPrefixByName(string));
    }

    @Override
    public <T> List<String> tabComplete(String string, Class<T> type) {
        return Core.get().getPrefixHandler().getPrefixes().stream().map(Prefix::getName).collect(Collectors.toList());
    }
}

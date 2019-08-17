package net.centilehcf.core.bootstrap;

import net.centilehcf.core.Core;
import lombok.Getter;

@Getter
public class Bootstrapped {

    protected final Core core;

    public Bootstrapped(Core core) {
        this.core = core;
    }
}

package net.centilehcf.core.prefix;

import net.centilehcf.core.util.CC;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.centilehcf.core.util.CC;

@Getter
@Setter
@EqualsAndHashCode
public class Prefix implements Comparable<Prefix> {

    private String name;
    private String prefix = "";
    private int weight;

    public Prefix(String name) {
        this.name = name;
    }

    public String getPrefixInfo() {
        return this.getName() + CC.RESET + "(W: " + this.getWeight() + ") (P: " + this.getPrefix() + ")";
    }

    @Override
    public int compareTo(Prefix other) {
        return Integer.compare(this.weight, other.weight);
    }
}

package net.centilehcf.core.util;

import lombok.experimental.UtilityClass;

import java.util.Collection;

@UtilityClass
public class CollectionUtil {


    // Taken from Peter Lawrey on StackOverflow
    // https://stackoverflow.com/questions/21092086/get-random-element-from-collection
    public <T> T random(Collection<T> coll) {
        int num = (int) (Math.random() * coll.size());
        for (T t : coll) if (--num < 0) return t;
        throw new AssertionError();
    }
}

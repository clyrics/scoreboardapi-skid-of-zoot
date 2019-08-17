import net.centilehcf.core.util.HastebinUtil;

import java.util.concurrent.ThreadLocalRandom;

public class HastebinTest {

    public static void main(String[] args) {
        String test = "Hello " + ThreadLocalRandom.current().nextInt();
        System.out.println(HastebinUtil.paste(test));
    }
}

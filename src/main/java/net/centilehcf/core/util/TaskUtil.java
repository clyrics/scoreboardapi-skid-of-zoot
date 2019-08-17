package net.centilehcf.core.util;

import net.centilehcf.core.Core;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 3/30/2019.
 */
public class TaskUtil {
    public TaskUtil() {
    }

    public static void run(Runnable runnable) {
        Core.get().getServer().getScheduler().runTask(Core.get(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        Core.get().getServer().getScheduler().runTaskTimer(Core.get(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(Core.get(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        Core.get().getServer().getScheduler().runTaskLater(Core.get(), runnable, delay);
    }

    public static void runAsync(Runnable runnable) {
        Core.get().getServer().getScheduler().runTaskAsynchronously(Core.get(), runnable);
    }

    public static void runTimerAsync(Runnable runnable, long delay, long timer) {
        Core.get().getServer().getScheduler().runTaskTimerAsynchronously(Core.get(), runnable, delay, timer);
    }

    public static Thread runAsyncReturn(Runnable runnable) {
        return new Thread(runnable);
    }
}

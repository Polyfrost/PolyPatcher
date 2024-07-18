package club.sk1er.patcher.mixins.bugfixes;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.spongepowered.asm.mixin.*;

import net.minecraft.client.gui.ServerListEntryNormal;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.UnknownHostException;
import java.util.concurrent.*;

@Mixin(ServerListEntryNormal.class)
public class ServerListEntryNormalMixin_BufferFix {
    @Shadow
    @Final
    private GuiMultiplayer owner;
    @Shadow
    @Final
    private ServerData server;

    @Mutable
    @Final
    @Shadow
    private static ThreadPoolExecutor field_148302_b;

    @Unique
    private static final int MAX_THREAD_COUNT_PINGER = 50;
    @Unique
    private static final int MAX_THREAD_COUNT_TIMEOUT = 100;
    @Unique
    private static final int SERVER_TIMEOUT = 4;
    @Unique
    private static ThreadPoolExecutor patcher$threadPoolExecutor;

    // Note: if servers are added, this will be inaccurate
    // But it should be good enough still
    // Can't bother to mixin onto some other classes just to change that (rn at least).
    @Unique
    private static final int patcher$serverCountCache;
    static {
        patcher$serverCountCache = new ServerList(Minecraft.getMinecraft()).countServers();
        // Note: not even sure this reassignement works since the field is final
        patcher$threadPoolExecutor = new ScheduledThreadPoolExecutor(Math.min(patcher$serverCountCache + 5, MAX_THREAD_COUNT_PINGER), (new ThreadFactoryBuilder()).setNameFormat("Patcher Server Pinger #%d").setDaemon(true).build());
        field_148302_b = patcher$threadPoolExecutor;
    }
    @Unique
    private final ScheduledExecutorService patcher$timeoutExecutor = Executors.newScheduledThreadPool(Math.min(patcher$serverCountCache + 5, MAX_THREAD_COUNT_TIMEOUT));

    @Unique
    private static int patcher$runningTaskCount = 0;

    @Unique
    private Runnable patcher$getPingTask() {
        return new Thread(() -> {
            try {
                owner.getOldServerPinger().ping(server);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Unique
    private void patcher$setServerFail(String error) {
        server.pingToServer = -1L;
        server.serverMOTD = error;
    }

    @Redirect(method = "drawEntry", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/ThreadPoolExecutor;submit(Ljava/lang/Runnable;)Ljava/util/concurrent/Future;"))
    public Future<?> patcher$drawEntry(ThreadPoolExecutor instance, Runnable r) {
        // Check if too many running tasks, if yes cancel & set to "spamming"
        if (patcher$runningTaskCount > patcher$serverCountCache * 2) {
            patcher$setServerFail(ChatColor.GRAY + "Stop spamming... refresh the server list.");
            return patcher$threadPoolExecutor.submit(() -> {});
        }

        // Start up the timeout task
        final Future<?> future = patcher$timeoutExecutor.submit(patcher$getPingTask());
        patcher$runningTaskCount++;

        // "Vanilla" behavior, modified to:
        // - use a timeout for the task instead of the ping directly
        // - handle future.get()'s exceptions instead of the ping's exceptions
        return patcher$threadPoolExecutor.submit(() -> {
            try {
                future.get(SERVER_TIMEOUT, TimeUnit.SECONDS);
            } catch (TimeoutException e1) {
                patcher$setServerFail(ChatColor.RED + "Timed out");
            } catch (ExecutionException e2) {
                if (e2.getCause() instanceof UnknownHostException)
                    patcher$setServerFail(ChatColor.DARK_RED + "Can't resolve hostname");
                else
                    patcher$setServerFail(ChatColor.DARK_RED + "Can't connect to server.");

            } catch (Exception e3) {
                // Shouldn't happen anymore but just in case
                patcher$setServerFail(ChatColor.DARK_RED + "Can't connect to server.");
            }
            patcher$runningTaskCount--;
        });
    }
}

package club.sk1er.patcher.events;

public class ServerJoinEvent { //todo implement
    public final boolean isLocal;

    public ServerJoinEvent(boolean isLocal) {
        this.isLocal = isLocal;
    }
}

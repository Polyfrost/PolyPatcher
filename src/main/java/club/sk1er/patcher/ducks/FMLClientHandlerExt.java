package club.sk1er.patcher.ducks;

public interface FMLClientHandlerExt {
    default boolean patcher$hasErrors() {
        return false;
    }
}

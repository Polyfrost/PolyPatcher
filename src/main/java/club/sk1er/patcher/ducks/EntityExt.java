package club.sk1er.patcher.ducks;

public interface EntityExt {
    default boolean patcher$isGlowing() {
        return false;
    }
}

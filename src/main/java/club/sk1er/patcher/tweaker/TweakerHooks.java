package club.sk1er.patcher.tweaker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TweakerHooks {
    public static long clientLoadTime;

    static void invokeExit() {
        try {
            final Class<?> aClass = Class.forName("java.lang.Shutdown");
            final Method exit = aClass.getDeclaredMethod("exit", int.class);
            exit.setAccessible(true);
            exit.invoke(null, 0);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

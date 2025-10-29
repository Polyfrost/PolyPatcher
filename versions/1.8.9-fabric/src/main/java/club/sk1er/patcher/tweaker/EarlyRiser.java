package club.sk1er.patcher.tweaker;
//#if FABRIC

import club.sk1er.patcher.tweaker.other.ModClassTransformer;
import club.sk1er.patcher.tweaker.transform.IClassTransformer;
import club.sk1er.patcher.tweaker.transform.PatcherTransformer;
import com.chocohead.mm.api.ClassTinkerers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * straight up tweaking rn
 */
public class EarlyRiser implements Runnable {
    @Override
    public void run() {
        TweakerHooks.clientLoadTime = System.currentTimeMillis();
        List<IClassTransformer> transformers = Arrays.asList(new ClassTransformer(), new ModClassTransformer());
        for (IClassTransformer transformer : transformers) {
            for (Map.Entry<String, PatcherTransformer> target : transformer.getTransformerMap().entries()) {
                ClassTinkerers.addTransformation(target.getKey(), (bytes) -> target.getValue().transform(bytes, target.getKey()));
            }
        }
    }
}
//#endif

package club.sk1er.patcher.tweaker.transform;

import com.google.common.collect.Multimap;

public interface IClassTransformer {
    Multimap<String, PatcherTransformer> getTransformerMap();
}

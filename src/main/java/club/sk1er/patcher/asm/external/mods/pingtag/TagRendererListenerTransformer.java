package club.sk1er.patcher.asm.external.mods.pingtag;

import club.sk1er.patcher.tweaker.transform.CommonTransformer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class TagRendererListenerTransformer implements CommonTransformer {

    /**
     * The class name that's being transformed
     *
     * @return the class name
     */
    @Override
    public String[] getClassName() {
        return new String[]{"me.powns.pingtag.rendering.TagRenderListener"};
    }

    /**
     * Perform any asm in order to transform code
     *
     * @param classNode the transformed class node
     * @param name      the transformed class name
     */
    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("render")) {
                methodNode.instructions.insert(modifyNametagRenderState(true));
                break;
            }
        }
    }
}

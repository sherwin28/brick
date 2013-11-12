package net.isger.brick.plugin;

import java.util.HashMap;
import java.util.Map;

import net.isger.brick.BrickException;
import net.isger.brick.blue.Marks.TYPE;
import net.isger.brick.util.Reflects;
import net.isger.brick.util.anno.Ignore;
import net.isger.brick.util.reflect.BoundMethod;

@Ignore
public class DynamicTarget {

    private static Map<Class<?>, Map<String, BoundMethod>> allMeths;

    static {
        allMeths = new HashMap<Class<?>, Map<String, BoundMethod>>();
    }

    protected DynamicTarget() {
        Class<?> clazz = this.getClass();
        if (!allMeths.containsKey(clazz)) {
            Map<String, BoundMethod> methods = new HashMap<String, BoundMethod>();
            for (BoundMethod method : Reflects.getBoundMethods(clazz)) {
                methods.put(method.getName(), method);
            }
            allMeths.put(clazz, methods);
        }
    }

    private BoundMethod getMethod(String name) {
        Class<?> clazz = this.getClass();
        Map<String, BoundMethod> methods = allMeths.get(this.getClass());
        BoundMethod method = methods.get(name
                + TYPE.getMethDesc(TYPE.VOID.name));
        if (method == null) {
            throw new BrickException("Not found the method " + name + " in "
                    + clazz.getName());
        }
        return method;
    }

    protected void invoke() {
        PluginCommand cmd = PluginCommand.getAction();
        String target = cmd.getTarget();
        if (target != null) {
            getMethod(target).invoke(this);
        }
    }

    protected void invoke(String target) {
        getMethod(target).invoke(this);
    }
}

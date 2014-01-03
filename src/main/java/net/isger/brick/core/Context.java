package net.isger.brick.core;

import java.util.HashMap;
import java.util.Map;

import net.isger.brick.plugin.Plugin;

/**
 * 上下文
 * 
 * @author issing
 * 
 */
public class Context {

    public static final String COMMAND = "command";

    public static final String MODULE = "module";

    public static final String PLUGIN = "plugin";

    public static final String STUB = "stub";

    private static final ThreadLocal<Context> CONTEXT;

    private Map<String, Object> contextMap;

    static {
        CONTEXT = new ThreadLocal<Context>();
    }

    public Context() {
        this.contextMap = new HashMap<String, Object>();
    }

    public Context(Map<String, Object> context) {
        this();
        if (context != null) {
            this.contextMap.putAll(context);
        }
    }

    Map<String, Object> getContextMap() {
        return contextMap;
    }

    public Object get(String key) {
        return contextMap.get(key);
    }

    public Object set(String key, Object value) {
        return contextMap.put(key, value);
    }

    public static Context getActionContext() {
        return CONTEXT.get();
    }

    public static void setActionContext(Context context) {
        CONTEXT.set(context);
    }

    public static Object getAction(String key) {
        Context context = getActionContext();
        if (context == null) {
            return null;
        }
        return context.get(key);
    }

    public static void setAction(String key, Object value) {
        Context context = getActionContext();
        if (context != null) {
            context.set(key, value);
        }
    }

    public static Command getActionCommand() {
        return (Command) getAction(COMMAND);
    }

    public static void setActionCommand(Command command) {
        setAction(COMMAND, command);
    }

    public static Module getActionModule() {
        return (Module) getAction(MODULE);
    }

    public static void setActionModule(Module module) {
        setAction(MODULE, module);
    }

    public static Plugin getActionPlugin() {
        return (Plugin) getAction(PLUGIN);
    }

    public static void setActionPlugin(Plugin plugin) {
        setAction(PLUGIN, plugin);
    }

}

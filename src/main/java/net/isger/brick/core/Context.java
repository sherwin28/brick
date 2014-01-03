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

    private static final ThreadLocal<Context> actionContext;

    private Map<String, Object> context;

    static {
        actionContext = new ThreadLocal<Context>();
    }

    public Context() {
        this(null);
    }

    public Context(Map<String, Object> context) {
        this.context = new HashMap<String, Object>();
        if (context != null) {
            this.context.putAll(context);
        }
    }

    public static Context getActionContext() {
        return actionContext.get();
    }

    public static void setContext(Context context) {
        actionContext.set(context);
    }

    Map<String, Object> getContextMap() {
        return context;
    }

    public Object get(String key) {
        return context.get(key);
    }

    public Object set(String key, Object value) {
        return context.put(key, value);
    }

    public Command getCommand() {
        return (Command) get(COMMAND);
    }

    public Module getModule() {
        return (Module) get(MODULE);
    }

    public Plugin getPlugin() {
        return (Plugin) get(PLUGIN);
    }

    public static Command getActionCommand() {
        Context context = getActionContext();
        if (context == null) {
            return null;
        }
        return context.getCommand();
    }

    public static Module getActionModule() {
        Context context = getActionContext();
        if (context == null) {
            return null;
        }
        return context.getModule();
    }

    public static Plugin getActionPlugin() {
        Context context = getActionContext();
        if (context == null) {
            return null;
        }
        return context.getPlugin();
    }

}

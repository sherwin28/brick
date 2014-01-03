package net.isger.brick.core;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import net.isger.brick.BrickConstants;
import net.isger.brick.BrickException;
import net.isger.brick.core.inject.Container;
import net.isger.brick.core.preparer.Preparers;
import net.isger.brick.util.anno.Alias;
import net.isger.brick.util.anno.Ignore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象控制台
 * 
 * @author issing
 * 
 */
public abstract class AbstractConsole implements Console {

    private static final Logger LOG;

    @Ignore
    private transient boolean initialized;

    @Alias(BrickConstants.KEY_BRICK_NAME)
    protected String name;

    @Alias(BrickConstants.KEY_BRICK_CONTAINER)
    protected Container container;

    static {
        LOG = LoggerFactory.getLogger(AbstractConsole.class);
    }

    protected AbstractConsole() {
        initialized = false;
    }

    @SuppressWarnings("unchecked")
    public synchronized void initial() {
        if (initialized) {
            return;
        }
        initialized = true;
        // 加载应用配置
        Object res = load();
        if (res instanceof Collection) {
            load((Collection<?>) res);
        } else if (res instanceof Map) {
            load((Map<String, Object>) res);
        } else {
            throw new BrickException("Unexpected load the modules by " + res);
        }
        container.getInstance(Modules.class).initial();
    }

    public Container getContainer() {
        return container;
    }

    /**
     * 加载资源
     * 
     * @return
     */
    protected abstract Object load();

    /**
     * 加载资源
     * 
     * @param res
     */
    @SuppressWarnings("unchecked")
    protected void load(Collection<?> res) {
        for (Object config : res) {
            if (config instanceof String) {
                config = load((String) config);
            }
            if (!(config instanceof Map)) {
                throw new BrickException(
                        "Unexpected load the modules for brick by " + config);
            }
            load((Map<String, Object>) config);
        }
    }

    /**
     * 加载资源
     * 
     * @param res
     * @return
     */
    protected abstract Object load(String res);

    /**
     * 加载配置
     * 
     * @param res
     */
    protected void load(Map<String, Object> res) {
        Modules modules = container.getInstance(Modules.class);
        Module module;
        String name;
        for (Entry<String, Object> entry : res.entrySet()) {
            name = entry.getKey();
            module = modules.get(name);
            if (module == null) {
                LOG.warn("Skipped the invalid module [{}]", name);
                continue;
            }
            module.load(entry.getValue());
        }
    }

    public void execute() {
        Command cmd = Context.getActionCommand();
        Preparers prepares = container.getInstance(Preparers.class);
        prepares.prepare();
        String moduleName = Commands.getModuleName(cmd);
        if (moduleName == null) {
            operate();
        } else {
            Module module = container.getInstance(Modules.class)
                    .get(moduleName);
            if (module == null) {
                throw new BrickException("Not found the module by "
                        + moduleName);
            }
            Context.setActionModule(module);
            module.execute();
        }
        prepares.cleanup();
    }

    public void execute(Command cmd) {
        Context.setActionCommand(cmd);
        execute();
    }

    /**
     * 核心操作
     * 
     */
    protected abstract void operate();

    public synchronized void destroy() {
        if (!initialized) {
            return;
        }
        initialized = false;
        container.getInstance(Modules.class).destroy();
    }

}

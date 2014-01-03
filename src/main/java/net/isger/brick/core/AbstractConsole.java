package net.isger.brick.core;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import net.isger.brick.BrickConstants;
import net.isger.brick.BrickException;
import net.isger.brick.core.inject.Container;
import net.isger.brick.core.preparer.Preparers;
import net.isger.brick.raw.Artifact;
import net.isger.brick.raw.Depository;
import net.isger.brick.raw.Depot;
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
    private String name;

    @Alias(BrickConstants.KEY_BRICK_CONTAINER)
    private Container container;

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
        String path = name + ".json";
        // 加载应用配置
        Artifact artifact = Depository.wrap("json", Depot.LAB_FILE, path);
        if (artifact == null) {
            throw new BrickException("Not found the config by " + path);
        }
        Object res = artifact.use("transform");
        if (res instanceof Collection) {
            load((Collection<?>) res);
        } else if (res instanceof Map) {
            load((Map<String, Object>) res);
        } else {
            throw new BrickException("Unexpected load the modules by " + res);
        }
        Modules modules = container.getInstance(Modules.class);
        modules.initial();
    }

    public Container getContainer() {
        return container;
    }

    /**
     * 加载资源
     * 
     * @param res
     */
    @SuppressWarnings("unchecked")
    private void load(Collection<?> res) {
        Artifact artifact;
        for (Object config : res) {
            if (config instanceof String) {
                artifact = Depository.wrap("json", Depot.LAB_FILE,
                        (String) config);
                if (artifact == null) {
                    throw new BrickException("Not found the config by "
                            + config);
                }
                config = artifact.use("transform");
            }
            if (!(config instanceof Map)) {
                throw new BrickException(
                        "Unexpected load the modules for brick by " + config);
            }
            load((Map<String, Object>) config);
        }
    }

    /**
     * 加载配置
     * 
     * @param res
     */
    private void load(Map<String, Object> res) {
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
        initial();
        Preparers prepares = container.getInstance(Preparers.class);
        prepares.prepare();
        Context context = Context.getActionContext();
        Command cmd = context.getCommand();
        String name = Commands.getModuleName(cmd);
        if (name == null) {
            operate();
        } else {
            Modules modules = container.getInstance(Modules.class);
            Module module = modules.get(name);
            if (module == null) {
                throw new BrickException("Not found the module by " + name);
            }
            context.set(Context.MODULE, module);
            module.execute();
        }
        prepares.cleanup();
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
    }

}

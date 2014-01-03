package net.isger.brick.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.isger.brick.BrickConstants;
import net.isger.brick.plugin.PluginModule;
import net.isger.brick.stub.StubModule;
import net.isger.brick.util.anno.Ignore;
import net.isger.brick.util.hitcher.Director;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 模块容器
 * 
 * @author issing
 * 
 */
@Ignore
public final class Modules extends Director implements Iterable<Module> {

    private static final String KEY_MODULES = "brick.core.modules";

    private static final String MODULE_PATH = "net/isger/brick/core/module";

    private static final Logger LOG;

    private Map<String, Module> modules;

    static {
        LOG = LoggerFactory.getLogger(Modules.class);
    }

    public Modules() {
        modules = new HashMap<String, Module>();
        add(BrickConstants.MODULE_PLUGIN, new PluginModule());
        add(BrickConstants.MODULE_STUB, new StubModule());
        // 加载模块实例（便车引导方式）
        canonicalize(this);
    }

    protected String directHitchPath() {
        return directHitchPath(KEY_MODULES, MODULE_PATH);
    }

    /**
     * 新增模块
     * 
     * @param name
     * @param module
     */
    public void add(String name, Module module) {
        Module oldModule = modules.put(name, module);
        if (oldModule == null) {
            LOG.info("Binded the module ({}) for ({})", name, module);
        } else {
            LOG.info("Multiple binded the module ({}) with ({})", name, module);
        }
    }

    /**
     * 初始
     * 
     */
    public void initial() {
        for (Module module : modules.values()) {
            module.initial();
        }
    }

    /**
     * 获取指定模块
     * 
     * @param name
     * @return
     */
    public Module get(String name) {
        return modules.get(name);
    }

    /**
     * 迭代所有模块
     * 
     */
    public Iterator<Module> iterator() {
        return modules.values().iterator();
    }

    /**
     * 注销
     * 
     */
    public void destroy() {
        for (Module module : modules.values()) {
            module.destroy();
        }
        modules.clear();
    }

}

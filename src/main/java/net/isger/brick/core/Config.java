package net.isger.brick.core;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import net.isger.brick.BrickException;
import net.isger.brick.raw.Artifact;
import net.isger.brick.raw.Depository;
import net.isger.brick.raw.Depot;
import net.isger.brick.util.SimpleLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置对象（转头配置策略）
 * 
 * @author issing
 * 
 */
public class Config {

    public static final String CONF_FILE_CORE = "brick-core.json";

    public static final String CONF_CONSOLE = "console";

    public static final String CONF_MODULES = "modules";

    public static final String CONF_FILE_APP = "brick.json";

    private static final Logger LOG;

    static {
        LOG = LoggerFactory.getLogger(Config.class);
    }

    private Config() {
    }

    /**
     * 安装控制台
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    static Console setup() {
        // 加载核心配置
        Artifact artifact = Depository.wrap("json", Depot.LAB_FILE,
                CONF_FILE_CORE);
        if (artifact == null) {
            return new OldConsole();
        }
        // 检查控制台配置
        Object res = artifact.use("transform");
        if (!(res instanceof Map)) {
            LOG.warn("Unexpected setup the console by {}" + res);
            return new OldConsole();
        }
        Map<String, Object> configMap = (Map<String, Object>) res;
        // 生成控制台实例
        OldConsole console;
        res = configMap.get(CONF_CONSOLE);
        if (res == null) {
            console = new OldConsole();
        } else {
            res = SimpleLoader.load(res, Console.class);
            if (!(res instanceof Console)) {
                throw new BrickException("Unexpected setup the console by "
                        + res);
            }
            console = (OldConsole) res;
        }
        // 检查模块配置
        res = configMap.get(CONF_MODULES);
        if (res == null) {
            return console;
        } else if (!(res instanceof Map)) {
            LOG.warn("Unexpected setup the modules for console by " + res);
            return console;
        }
        configMap = (Map<String, Object>) configMap.get(CONF_MODULES);
        // 安装模块实例（核心配置文件加载方式）
        Modules modules = console.getModules();
        for (Entry<String, Object> mc : configMap.entrySet()) {
            res = SimpleLoader.load(mc.getValue(), Module.class);
            if (!(res instanceof Module)) {
                LOG.warn("Unexpected setup the module for console by {}", res);
                continue;
            }
            modules.add(mc.getKey(), (Module) res);
        }
        return console;
    }

    /**
     * 加载砖头
     * 
     * @param path
     */
    @SuppressWarnings("unchecked")
    public static void load(String path) {
        OldConsole console = OldConsole.getActionConsole();
        console.destroy();
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
        console.initial();
    }

    /**
     * 加载资源
     * 
     * @param res
     */
    @SuppressWarnings("unchecked")
    private static void load(Collection<?> res) {
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
    private static void load(Map<String, Object> res) {
        OldConsole console = OldConsole.getActionConsole();
        Modules modules = console.getModules();
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

}

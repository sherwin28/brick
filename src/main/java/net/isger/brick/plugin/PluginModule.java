package net.isger.brick.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.isger.brick.BrickException;
import net.isger.brick.core.AbstractModule;
import net.isger.brick.core.Command;
import net.isger.brick.core.Context;
import net.isger.brick.plugin.persist.PersistsConversion;
import net.isger.brick.plugin.service.ServicesConversion;
import net.isger.brick.util.Reflects;
import net.isger.brick.util.SimpleLoader;
import net.isger.brick.util.reflect.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 插件模块
 * 
 * @author issing
 * 
 */
public class PluginModule extends AbstractModule {

    private static final Logger LOG;

    static {
        LOG = LoggerFactory.getLogger(PluginModule.class);
        Converter converter = Converter.getConverter();
        converter.add(ServicesConversion.CONVERSION);
        converter.add(PersistsConversion.CONVERSION);
    }

    private Map<String, Plugin> plugins;

    public PluginModule() {
        this.plugins = new HashMap<String, Plugin>();
        this.loader = new SimpleLoader(BasePlugin.class) {
            @SuppressWarnings("unchecked")
            protected Object load(Map<String, Object> res) {
                List<Object> result = new ArrayList<Object>();
                Plugin plugin;
                String name;
                Object config;
                for (Entry<String, Object> entry : res.entrySet()) {
                    name = entry.getKey();
                    config = entry.getValue();
                    if (!(config instanceof Map)) {
                        LOG.warn("Skipped the plugin invalid config {}", name);
                        continue;
                    }
                    plugin = (Plugin) super.load((Map<String, Object>) config);
                    add(entry.getKey(), plugin);
                    result.add(plugin);
                }
                return result;
            }

            protected Object create(Class<?> clazz, Map<String, Object> res) {
                if (!Plugin.class.isAssignableFrom(clazz)) {
                    throw new BrickException("Invalid plugin class name "
                            + clazz.getName());
                }
                return Reflects.newInstance(clazz, res);
            }

            protected Object create(Object res) {
                throw new BrickException("Unexpected load the plugin by " + res);
            }

        };
    }

    public void add(String name, Plugin plugin) {
        Plugin oldPlugin = plugins.put(name, plugin);
        if (oldPlugin == null) {
            LOG.info("Binded the plugin [{}] with {}", name, plugin);
        } else {
            LOG.info("Multiple binded the plugin [{}] with {}", name, plugin);
        }
    }

    protected void call() {
        call(Context.getActionCommand());
    }

    public void call(Command cmd) {
        Context context = Context.getActionContext();
        String domain = cmd.getDomain(); // 获取域
        Plugin plugin = plugins.get(domain);
        if (plugin == null) {
            throw new BrickException("Not found the plugin by " + domain);
        }
        context.set(Context.PLUGIN, plugin);
        String operate = cmd.getOperate();
        if (operate == null || operate.equals(PluginCommand.OPERATE_SERVICE)) {
            plugin.service();
        } else if (operate.equals(PluginCommand.OPERATE_PERSIST)) {
            plugin.persist();
        } else {
            throw new BrickException("Unexpected the plugin operation "
                    + operate);
        }
    }

    public void initial() {
        for (Plugin plugin : plugins.values()) {
            plugin.initial();
        }
    }

    public void destroy() {
        for (Plugin plugin : plugins.values()) {
            plugin.destroy();
        }
        plugins.clear();
    }

}
